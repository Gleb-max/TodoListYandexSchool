package school.yandex.todolist.presentation.todolist.adapter

import androidx.recyclerview.widget.DiffUtil
import school.yandex.todolist.domain.entity.TodoItem

class TodoItemDiffCallback : DiffUtil.ItemCallback<TodoItem>() {
    override fun areItemsTheSame(oldItem: TodoItem, newItem: TodoItem): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: TodoItem, newItem: TodoItem): Boolean {
        return oldItem == newItem
    }
}