package school.yandex.todolist.presentation.view

import android.animation.AnimatorInflater
import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.AppBarLayout
import school.yandex.todolist.R
import school.yandex.todolist.TodoApp
import school.yandex.todolist.databinding.FragmentTodoListBinding
import school.yandex.todolist.domain.entity.TodoItem
import school.yandex.todolist.presentation.i.OnSnackBarShowListener
import school.yandex.todolist.presentation.stateholder.TodoListViewModel
import school.yandex.todolist.presentation.stateholder.ViewModelFactory
import school.yandex.todolist.presentation.view.adapter.TodoListAdapter
import javax.inject.Inject
import kotlin.math.abs

class TodoListFragment : Fragment() {

    private var _binding: FragmentTodoListBinding? = null
    private val binding: FragmentTodoListBinding
        get() = _binding ?: throw RuntimeException("FragmentTodoListBinding == null")

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private lateinit var viewModel: TodoListViewModel
    private val component by lazy {
        (requireActivity().application as TodoApp).component
    }

    private val scaleAnimation by lazy(mode = LazyThreadSafetyMode.NONE) {
        AnimatorInflater.loadAnimator(
            context, R.animator.pulse_animator
        )
    }

    private lateinit var todoListAdapter: TodoListAdapter

    private var onSnackBarShowListener: OnSnackBarShowListener? = null

    private val connectivityManager: ConnectivityManager by lazy {
        requireActivity().getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager
    }
    private val networkCallback = object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            viewModel.patchTodoList()
        }
    }

    override fun onAttach(context: Context) {
        component.inject(this)
        super.onAttach(context)
        if (context is OnSnackBarShowListener) onSnackBarShowListener = context
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTodoListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this, viewModelFactory)[TodoListViewModel::class.java]
        observeViewModel()

        with(binding) {
            fab.setOnClickListener {
                onAddTodoItem()
            }
            swipeToRefresh.setOnRefreshListener {
                updateTodoList()
            }
            btnAddTodoItem.setOnClickListener {
                onAddTodoItem()
            }
            ibTodoVisibility.setOnClickListener {
                viewModel.changeItemsVisibility()
            }
            ibSettings.setOnClickListener {
                onOpenSettings()
            }
        }

        setupAppBar()
        setupRecyclerView()

        updateTodoList()

        connectivityManager.registerDefaultNetworkCallback(networkCallback)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        connectivityManager.unregisterNetworkCallback(networkCallback)
        _binding = null
    }

    private fun onEditTodoItem(todoItem: TodoItem) {
        findNavController().navigate(
            TodoListFragmentDirections.actionNavigationTodoListToNavigationTodoItemDetails(
                todoItem.id
            )
        )
    }

    private fun onAddTodoItem() {
        findNavController().navigate(R.id.navigation_todo_item_details)
    }

    private fun onOpenSettings() {
        findNavController().navigate(R.id.navigation_settings)
    }

    private fun observeViewModel() {
        viewModel.isLoading.observe(viewLifecycleOwner) {
            binding.swipeToRefresh.isRefreshing = it
        }
        viewModel.todoItems.observe(viewLifecycleOwner) {
            val doneCount = it.count { item -> item.isDone }
            binding.tvDoneCount.text = getString(R.string.items_done_subtitle, doneCount)

            val currentList =
                if (viewModel.isAllItems.value!!) it else it.filter { todoItem -> !todoItem.isDone }
            todoListAdapter.submitList(currentList)

            if (currentList.isEmpty()) {
                scaleAnimation.setTarget(binding.fab)
                scaleAnimation.start()
            } else {
                scaleAnimation.end()
            }
        }
        viewModel.isAllItems.observe(viewLifecycleOwner) {
            val imageResource = if (it) R.drawable.ic_visibility_off else R.drawable.ic_visibility
            binding.ibTodoVisibility.setImageResource(imageResource)

            val currentList = if (it) viewModel.todoItems.value else viewModel.todoItems.value?.filter { todoItem -> !todoItem.isDone }
            todoListAdapter.submitList(currentList)
        }
    }

    private fun setupAppBar() {
        // todo переделать покрасивше, используя анимацию для скрытия подтекста
        binding.appBar.addOnOffsetChangedListener(
            AppBarLayout.OnOffsetChangedListener { appBarLayout, verticalOffset ->
                if (abs(verticalOffset) <= appBarLayout.totalScrollRange / 2) {
                    binding.tvDoneCount.visibility = View.VISIBLE
                } else {
                    binding.tvDoneCount.visibility = View.GONE
                }
            }
        )
    }

    private fun setupRecyclerView() {
        todoListAdapter = TodoListAdapter()
        with(binding.rvTodoList) {
            adapter = todoListAdapter
        }
        setupClickListener()
        setupSwipeListeners(binding.rvTodoList)
    }

    private fun setupClickListener() {
        todoListAdapter.onTodoItemClickListener = {
            onEditTodoItem(it)
        }
        todoListAdapter.onTodoItemCheckListener = {
            viewModel.changeStatusTodoItem(it)
        }
    }

    private fun setupSwipeListeners(rvShopList: RecyclerView) {
        val swipeCallback = object : ItemTouchHelper.SimpleCallback(
            0,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT,
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val item = todoListAdapter.currentList[viewHolder.adapterPosition]
                if (direction == ItemTouchHelper.LEFT) deleteTodoItem(item)
                else if (direction == ItemTouchHelper.RIGHT) changeStatusTodoItem(item)
            }
        }
        val itemTouchHelper = ItemTouchHelper(swipeCallback)
        itemTouchHelper.attachToRecyclerView(rvShopList)
    }

    private fun updateTodoList() {
        viewModel.fetchTodoList(
            onSuccess = {
                binding.swipeToRefresh.isRefreshing = false
            },
            onError = {
                onSnackBarShowListener?.showSnackBarMessage(
                    getString(R.string.cant_load_data),
                    getString(R.string.retry),
                    this::updateTodoList
                )
                binding.swipeToRefresh.isRefreshing = false
            })
    }

    private fun deleteTodoItem(item: TodoItem) {
        viewModel.deleteTodoItem(item, onError = {
            onSnackBarShowListener?.showSnackBarMessage(
                getString(R.string.cant_save_data),
                getString(R.string.retry)
            ) { deleteTodoItem(item) }
        })
    }

    private fun changeStatusTodoItem(item: TodoItem) {
        viewModel.changeStatusTodoItem(item, onError = {
            onSnackBarShowListener?.showSnackBarMessage(
                getString(R.string.cant_save_data),
                getString(R.string.retry)
            ) { changeStatusTodoItem(item) }
        })
    }
}