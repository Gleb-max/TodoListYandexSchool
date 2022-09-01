package school.yandex.todolist.data.mapper

import android.os.Build
import school.yandex.todolist.data.source.local.db.TodoItemDbModel
import school.yandex.todolist.data.source.remote.model.TodoItemDTO
import school.yandex.todolist.domain.entity.TodoItem
import school.yandex.todolist.domain.entity.TodoItemImportance
import java.util.*
import javax.inject.Inject

class TodoListMapper @Inject constructor() {

    fun mapEntityToDTO(todoItem: TodoItem) = TodoItemDTO(
        id = todoItem.id,
        text = todoItem.content,
        importance = todoItem.importance.name.lowercase(),
        deadline = todoItem.deadline?.time,
        isDone = todoItem.isDone,
        color = "#FFFFFF",
        createdAt = todoItem.createdAt.time,
        changedAt = todoItem.changedAt?.time,
        lastUpdatedBy = Build.DEVICE,
    )

    fun mapDTOToEntity(todoItemDTO: TodoItemDTO) = TodoItem(
        id = todoItemDTO.id!!,
        content = todoItemDTO.text,
        importance = TodoItemImportance.values().firstOrNull {
            it.value == todoItemDTO.importance
        } ?: TodoItemImportance.BASIC,
        deadline = todoItemDTO.deadline?.let { Date(it) },
        isDone = todoItemDTO.isDone,
        createdAt = Date(todoItemDTO.createdAt),
        changedAt = todoItemDTO.changedAt?.let { Date(it) },
    )

    fun mapDbModelToEntity(todoItem: TodoItemDbModel) = TodoItem(
        id = todoItem.id,
        content = todoItem.content,
        importance = todoItem.importance,
        deadline = todoItem.deadline?.let { Date(it) },
        isDone = todoItem.isDone,
        createdAt = Date(todoItem.createdAt),
        changedAt = todoItem.changedAt?.let { Date(it) },
    )

    fun mapDTOToDbModel(todoItemDTO: TodoItemDTO) = TodoItemDbModel(
        id = todoItemDTO.id!!,
        content = todoItemDTO.text,
        importance = TodoItemImportance.values().firstOrNull {
            it.value == todoItemDTO.importance
        } ?: TodoItemImportance.BASIC,
        deadline = todoItemDTO.deadline,
        isDone = todoItemDTO.isDone,
        createdAt = todoItemDTO.createdAt,
        changedAt = todoItemDTO.changedAt,
    )

    fun mapEntityToDbModel(todoItem: TodoItem) = TodoItemDbModel(
        id = todoItem.id,
        content = todoItem.content,
        importance = todoItem.importance,
        deadline = todoItem.deadline?.time,
        isDone = todoItem.isDone,
        createdAt = todoItem.createdAt.time,
        changedAt = todoItem.changedAt?.time,
    )
}