package school.yandex.todolist.presentation.todolist

import android.content.Context
import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import school.yandex.todolist.R
import school.yandex.todolist.databinding.FragmentTodoListBinding
import school.yandex.todolist.domain.entity.TodoItem
import school.yandex.todolist.presentation.TodoListAdapter

class TodoListFragment : Fragment() {

    private var _binding: FragmentTodoListBinding? = null
    private val binding: FragmentTodoListBinding
        get() = _binding ?: throw RuntimeException("FragmentTodoListBinding == null")

    private val viewModel: TodoListViewModel by lazy {
        ViewModelProvider(this)[TodoListViewModel::class.java]
    }

    private lateinit var todoListAdapter: TodoListAdapter

    private lateinit var onTodoListActionsListener: OnTodoListActionsListener

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (context is OnTodoListActionsListener) onTodoListActionsListener = context
        else throw RuntimeException("Activity must implement OnTodoListActionsListener")
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
        (activity as AppCompatActivity).setSupportActionBar(binding.toolbar)
        binding.fab.setOnClickListener {
            onTodoListActionsListener.onAddTodoItem()
        }

        setupRecyclerView()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun observeViewModel() {
        viewModel.todoList.observe(viewLifecycleOwner) {
            todoListAdapter.submitList(it)
        }
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

    interface OnTodoListActionsListener {

        fun onAddTodoItem()

        fun onEditTodoItem(todoItem: TodoItem)
    }
}