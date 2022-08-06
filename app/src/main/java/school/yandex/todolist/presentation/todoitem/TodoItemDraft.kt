package school.yandex.todolist.presentation.todoitem

import school.yandex.todolist.domain.entity.TodoItem
import school.yandex.todolist.domain.entity.TodoItemImportance
import java.text.SimpleDateFormat
import java.util.*

data class TodoItemDraft(
    val id: String? = null,
    var content: String = "",
    var importance: TodoItemImportance = TodoItemImportance.BASIC,
    var deadline: Date? = null,
    var isDone: Boolean = false,
    var createdAt: Date? = null,
    var lastUpdatedBy: Date? = null,
) {

    fun getReadableDeadline(): String? {
        val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        return deadline?.let { formatter.format(it) }
    }

    fun toEntity() = TodoItem(
        id = this.id ?: TodoItem.UNDEFINED_ID,
        content = this.content,
        importance = this.importance,
        deadline = this.deadline,
        isDone = this.isDone,
        createdAt = this.createdAt ?: Date(),
        lastUpdatedBy = this.lastUpdatedBy,
    )

    companion object {

        fun fromEntity(todoItem: TodoItem) = TodoItemDraft(
            id = todoItem.id,
            content = todoItem.content,
            importance = todoItem.importance,
            deadline = todoItem.deadline,
            isDone = todoItem.isDone,
            createdAt = todoItem.createdAt,
            lastUpdatedBy = todoItem.lastUpdatedBy,
        )
    }
}