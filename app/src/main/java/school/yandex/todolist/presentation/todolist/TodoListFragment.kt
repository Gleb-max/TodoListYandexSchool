package school.yandex.todolist.presentation.todolist

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.AppBarLayout
import org.koin.android.ext.android.inject
import school.yandex.todolist.R
import school.yandex.todolist.databinding.FragmentTodoListBinding
import school.yandex.todolist.domain.entity.TodoItem
import school.yandex.todolist.presentation.i.OnSnackBarShowListener
import school.yandex.todolist.presentation.todolist.adapter.TodoListAdapter
import kotlin.math.abs

class TodoListFragment : Fragment() {

    private var _binding: FragmentTodoListBinding? = null
    private val binding: FragmentTodoListBinding
        get() = _binding ?: throw RuntimeException("FragmentTodoListBinding == null")

    private val viewModel: TodoListViewModel by inject()

    private lateinit var todoListAdapter: TodoListAdapter

    private lateinit var onTodoListActionsListener: OnTodoListActionsListener
    private var onSnackBarShowListener: OnSnackBarShowListener? = null

    val connectivityManager: ConnectivityManager by lazy {
        requireActivity().getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager
    }
    val networkCallback = object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            viewModel.fetchTodoList()
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (context is OnTodoListActionsListener) onTodoListActionsListener = context
        else throw RuntimeException("Activity must implement OnTodoListActionsListener")

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
        observeViewModel()

        binding.fab.setOnClickListener {
            onTodoListActionsListener.onAddTodoItem()
        }

        binding.swipeToRefresh.setOnRefreshListener {
            updateTodoList()
        }

        setupAppBar()
        setupRecyclerView()

        updateTodoList()

        connectivityManager.registerDefaultNetworkCallback(networkCallback)
    }

    override fun onPause() {
        super.onPause()
        connectivityManager.unregisterNetworkCallback(networkCallback)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun observeViewModel() {
        viewModel.isLoading.observe(viewLifecycleOwner) {
            binding.swipeToRefresh.isRefreshing = it
        }
        viewModel.todoList.observe(viewLifecycleOwner) {
            todoListAdapter.submitList(it)

            val doneCount = it.count { item -> item.isDone }
            binding.tvDoneCount.text = getString(R.string.items_done_subtitle, doneCount)
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
            onTodoListActionsListener.onEditTodoItem(it)
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
                if (direction == ItemTouchHelper.LEFT) viewModel.deleteTodoItem(item)
                else if (direction == ItemTouchHelper.RIGHT) viewModel.changeStatusTodoItem(item)
            }
        }
        val itemTouchHelper = ItemTouchHelper(swipeCallback)
        itemTouchHelper.attachToRecyclerView(rvShopList)
    }

    private fun updateTodoList() {
        //todo допилить эту логику, она немного не так работает
        try {
            viewModel.fetchTodoList()
        } catch (exc: Exception) {
            exc.printStackTrace()
            onSnackBarShowListener?.showSnackBarMessage(
                getString(R.string.cant_load_data),
                getString(R.string.retry),
                this::updateTodoList
            )
        }
        binding.swipeToRefresh.isRefreshing = false
    }

    interface OnTodoListActionsListener {

        fun onAddTodoItem()

        fun onEditTodoItem(todoItem: TodoItem)
    }
}