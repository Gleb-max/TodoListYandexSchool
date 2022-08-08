package school.yandex.todolist.presentation.todolist.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import school.yandex.todolist.databinding.LayoutTodoItemBinding
import school.yandex.todolist.domain.entity.TodoItem

class TodoListAdapter : ListAdapter<TodoItem, TodoItemViewHolder>(TodoItemDiffCallback()) {

    var onTodoItemClickListener: ((TodoItem) -> Unit)? = null
    var onTodoItemCheckListener: ((TodoItem) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoItemViewHolder {
        val binding = LayoutTodoItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return TodoItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TodoItemViewHolder, position: Int) {
        val todoItem = getItem(position)
        holder.bind(todoItem)
        val binding = holder.binding
        binding.root.setOnClickListener {
            onTodoItemClickListener?.invoke(todoItem)
        }
        binding.ibInfo.setOnClickListener {
            onTodoItemClickListener?.invoke(todoItem)
        }
        binding.checkbox.setOnCheckedChangeListener { _, _ ->
            onTodoItemCheckListener?.invoke(todoItem)
        }
    }
}