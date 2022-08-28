package school.yandex.todolist.presentation.view.adapter

import android.graphics.Paint
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.color.MaterialColors
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
        binding.checkbox.buttonTintList = ContextCompat.getColorStateList(
            binding.root.context,
            if (todoItem.importance == TodoItemImportance.IMPORTANT) {
                R.color.selector_checkbox_red
            } else {
                R.color.selector_checkbox_gray
            }
        )
        if (todoItem.importance == TodoItemImportance.BASIC || todoItem.isDone) {
            binding.ivPriority.visibility = View.GONE
        } else {
            binding.ivPriority.setImageResource(
                if (todoItem.importance == TodoItemImportance.LOW) R.drawable.ic_priority_low
                else R.drawable.ic_priority_high
            )
        }
        if (todoItem.isDone) {
            with(binding.tvContent) {
                paintFlags = paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                setTextColor(MaterialColors.getColor(this, R.attr.tertiaryLabelColor))
            }
        } else {
            with(binding.tvContent) {
                paintFlags = paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
                setTextColor(MaterialColors.getColor(this, R.attr.primaryLabelColor))
            }
        }
    }
}