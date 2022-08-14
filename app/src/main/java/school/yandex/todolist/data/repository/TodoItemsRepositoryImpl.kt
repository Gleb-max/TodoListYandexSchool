package school.yandex.todolist.data.repository

import school.yandex.todolist.data.local.RevisionPreferences
import school.yandex.todolist.data.mapper.TodoListMapper
import school.yandex.todolist.data.remote.api.TodoApi
import school.yandex.todolist.domain.entity.TodoItem
import school.yandex.todolist.domain.repository.TodoItemsRepository

//todo: add local data source to constructor
class TodoItemsRepositoryImpl(
    private val remote: TodoApi,
    private val revisionPreferences: RevisionPreferences
) : TodoItemsRepository {

    private val mapper = TodoListMapper()

    override suspend fun getTodoList(): List<TodoItem> {
        val todoListResponse = remote.fetchTodoList()
        val lastRevision = todoListResponse.revision
        revisionPreferences.setRevision(lastRevision)
        return mapper.mapListDTOToListEntity(todoListResponse.todoItems)
    }

    override suspend fun getTodoItem(todoItemId: String): TodoItem {
        val todoItemResponse = remote.fetchTodoItem(todoItemId)
        val lastRevision = todoItemResponse.revision
        revisionPreferences.setRevision(lastRevision)
        return mapper.mapDTOToEntity(todoItemResponse.todoItem)
    }

    override suspend fun addTodoItem(todoItem: TodoItem) {
        remote.createTodoItem(mapper.mapEntityToDTO(todoItem))
    }

    override suspend fun editTodoItem(todoItem: TodoItem) {
        val todoItemDTO = mapper.mapEntityToDTO(todoItem)
        remote.editTodoItem(todoItemDTO.id, todoItemDTO)
    }

    override suspend fun deleteTodoItem(todoItemId: String) {
        remote.deleteTodoItem(todoItemId)
    }
}