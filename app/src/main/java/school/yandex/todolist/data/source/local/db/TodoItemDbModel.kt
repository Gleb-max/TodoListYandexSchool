package school.yandex.todolist.data.source.local.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import school.yandex.todolist.domain.entity.TodoItemImportance
import java.util.*

@Entity(tableName = "todo_items")
data class TodoItemDbModel(
    @PrimaryKey
    val id: String,
    val content: String,
    val importance: TodoItemImportance,
    val deadline: Int?,
    val isDone: Boolean,
    val createdAt: Int,
    val changedAt: Int?,
)