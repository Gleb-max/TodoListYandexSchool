package school.yandex.todolist.presentation.todolist.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import school.yandex.todolist.R
import school.yandex.todolist.core.ext.getReadableDate
import school.yandex.todolist.databinding.LayoutTodoItemBinding
import school.yandex.todolist.domain.entity.TodoItem
import school.yandex.todolist.domain.entity.TodoItemImportance

class TodoItemViewHolder(
    val binding: LayoutTodoItemBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(todoItem: TodoItem) {
        binding.tvContent.text = todoItem.content
        binding.tvDate.visibility = if (todoItem.deadline == null) View.GONE else View.VISIBLE
        binding.tvDate.text = todoItem.deadline?.getReadableDate()
        binding.checkbox.isChecked = todoItem.isDone
//        binding.checkbox.buttonTintList = ColorStateList.valueOf(
//            ContextCompat.getColor(
//                binding.root.context,
//                R.color.green
//            )
//        )
        if (todoItem.importance == TodoItemImportance.BASIC) {
            binding.ivPriority.visibility = View.GONE
        } else {
            binding.ivPriority.setImageResource(
                if (todoItem.importance == TodoItemImportance.LOW) R.drawable.ic_priority_low
                else R.drawable.ic_priority_high
            )
        }

//        val states = arrayOf(intArrayOf(android.R.attr.state_checked))
//        int states[][] = {{android.R.attr.state_checked}, {}}
//        val colors = intArrayOf(R.color.green, R.color.gray)
//        CompoundButtonCompat.setButtonTintList(binding.checkbox, ColorStateList(states, colors))
    }
}