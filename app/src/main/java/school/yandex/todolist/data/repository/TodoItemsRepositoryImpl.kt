package school.yandex.todolist.data.repository

import school.yandex.todolist.data.mapper.TodoListMapper
import school.yandex.todolist.data.source.local.RevisionPreferences
import school.yandex.todolist.data.source.local.db.TodoListDao
import school.yandex.todolist.data.source.remote.api.TodoApi
import school.yandex.todolist.data.source.remote.model.request.TodoItemRequest
import school.yandex.todolist.domain.entity.TodoItem
import school.yandex.todolist.domain.repository.TodoItemsRepository
import javax.inject.Inject
import kotlin.random.Random

class TodoItemsRepositoryImpl @Inject constructor(
    private val remote: TodoApi,
    private val local: TodoListDao,
    private val revisionPreferences: RevisionPreferences,
    private val mapper: TodoListMapper
) : TodoItemsRepository {

    override suspend fun getTodoList(): List<TodoItem> {
        try {
            val todoListResponse = remote.fetchTodoList()
            val lastRevision = todoListResponse.revision
            revisionPreferences.setRevision(lastRevision)
            local.addTodoItems(todoListResponse.todoItems.map {
                mapper.mapDTOToDbModel(it)
            })
        } catch (exc: Exception) {
            //todo: add handling errors
        }
        return local.getTodoList().map {
            mapper.mapDbModelToEntity(it)
        }
    }

    override suspend fun getTodoItem(todoItemId: String): TodoItem {
        val todoItemResponse = remote.fetchTodoItem(todoItemId)
        val lastRevision = todoItemResponse.revision
        revisionPreferences.setRevision(lastRevision)
        return mapper.mapDTOToEntity(todoItemResponse.todoItem)
    }

    override suspend fun addTodoItem(todoItem: TodoItem) {
        val t = todoItem.copy(id = generateTodoItemId())
        val req = TodoItemRequest(mapper.mapEntityToDTO(t))
        remote.createTodoItem(req)
    }

    override suspend fun editTodoItem(todoItem: TodoItem) {
        val req = TodoItemRequest(mapper.mapEntityToDTO(todoItem))
        remote.editTodoItem(todoItem.id, req)
    }

    override suspend fun deleteTodoItem(todoItemId: String) {
        remote.deleteTodoItem(todoItemId)
    }

    private fun generateTodoItemId() = Random(
        System.currentTimeMillis()
    ).nextInt(0, 100000).toString()
}