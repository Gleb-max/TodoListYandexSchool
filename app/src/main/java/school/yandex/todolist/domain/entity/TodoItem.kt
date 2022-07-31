package school.yandex.todolist.domain.entity

import java.util.*

data class TodoItem(
    val id: String,
    val content: String,
    val importance: TodoItemImportance,
    val deadline: Date?,
    val isDone: Boolean,
    val createdAt: Date,
    val lastUpdatedBy: Date?,
)