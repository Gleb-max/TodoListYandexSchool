package school.yandex.todolist.domain.entity

import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter
import java.util.*

data class TodoItem(
    val id: String,
    val content: String,
    val importance: TodoItemImportance,
    val deadline: Date?,
    val isDone: Boolean,
    val createdAt: Date,
    val lastUpdatedBy: Date?,
) {

    // неправильно сюда функции помещать, но во-первых не успеваю уже, а во-вторых не проходили
    fun getReadableDeadline(): String? {
        val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        return deadline?.let { formatter.format(it) }
    }

    companion object {

        const val UNDEFINED_ID = ""
    }
}