package school.yandex.todolist.data.mapper

import school.yandex.todolist.data.source.remote.model.TodoItemDTO
import school.yandex.todolist.domain.entity.TodoItem
import school.yandex.todolist.domain.entity.TodoItemImportance
import java.util.*
import javax.inject.Inject

class TodoListMapper @Inject constructor() {

    //todo: добавить инициализацию полей color и lastUpdatedBy
    fun mapEntityToDTO(todoItem: TodoItem) = TodoItemDTO(
        id = todoItem.id,
        text = todoItem.content,
        importance = todoItem.importance.name.lowercase(),
        deadline = todoItem.deadline?.time,
        isDone = todoItem.isDone,
        color = null,
        createdAt = todoItem.createdAt.time,
        changedAt = todoItem.changedAt?.time,
        lastUpdatedBy = "",
    )

    fun mapDTOToEntity(todoItemDTO: TodoItemDTO) = TodoItem(
        id = todoItemDTO.id,
        content = todoItemDTO.text,
        importance = TodoItemImportance.valueOf(todoItemDTO.importance),
        deadline = todoItemDTO.deadline?.let { Date(it) },
        isDone = todoItemDTO.isDone,
        createdAt = Date(todoItemDTO.createdAt),
        changedAt = todoItemDTO.changedAt?.let { Date(it) },
    )

    fun mapListDTOToListEntity(list: List<TodoItemDTO>) = list.map {
        mapDTOToEntity(it)
    }
}