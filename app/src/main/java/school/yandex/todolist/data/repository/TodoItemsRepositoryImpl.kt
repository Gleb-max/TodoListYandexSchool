package school.yandex.todolist.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import school.yandex.todolist.data.mapper.TodoListMapper
import school.yandex.todolist.data.source.local.RevisionPreferences
import school.yandex.todolist.data.source.local.db.TodoListDao
import school.yandex.todolist.data.source.remote.api.TodoApi
import school.yandex.todolist.data.source.remote.model.request.TodoItemRequest
import school.yandex.todolist.data.source.remote.model.request.TodoItemsRequest
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

    override fun getTodoList(): LiveData<List<TodoItem>> =
        Transformations.map(local.getTodoList()) {
            it.map { todoItemDbModel ->
                mapper.mapDbModelToEntity(todoItemDbModel)
            }
        }

    override suspend fun loadTodoList() {
        val todoListResponse = remote.fetchTodoList()
        val lastRevision = todoListResponse.revision
        revisionPreferences.setRevision(lastRevision)
        local.addTodoItems(todoListResponse.todoItems.map {
            mapper.mapDTOToDbModel(it)
        })
    }

    override suspend fun patchTodoList() {
        val localTodos = local.getTodoListSync()
        val result = remote.patchTodoList(TodoItemsRequest(localTodos.map {
            mapper.mapEntityToDTO(
                mapper.mapDbModelToEntity(it)
            )
        }))
        val lastRevision = result.revision
        revisionPreferences.setRevision(lastRevision)
        local.addTodoItems(result.todoItems.map {
            mapper.mapDTOToDbModel(it)
        })
    }

    override suspend fun getTodoItem(todoItemId: String): TodoItem {
        // убрал тут загрузку по сети, так как элемент и так уже в бд будет на момент открытия

//        val todoItemResponse = remote.fetchTodoItem(todoItemId)
//        val lastRevision = todoItemResponse.revision
//        revisionPreferences.setRevision(lastRevision)
        val todoItem = local.getTodoItem(todoItemId)
        return mapper.mapDbModelToEntity(todoItem)
    }

    override suspend fun addTodoItem(todoItem: TodoItem) {
        val t = todoItem.copy(id = generateTodoItemId())
        local.addTodoItem(mapper.mapEntityToDbModel(t))
        try {
            val req = TodoItemRequest(mapper.mapEntityToDTO(t))
            remote.createTodoItem(req)
        } catch (exc: Exception) {
            exc.printStackTrace()
            //todo: add handling errors
        }
    }

    override suspend fun editTodoItem(todoItem: TodoItem) {
        local.addTodoItem(mapper.mapEntityToDbModel(todoItem))
        try {
            val req = TodoItemRequest(mapper.mapEntityToDTO(todoItem))
            remote.editTodoItem(todoItem.id, req)
        } catch (exc: Exception) {
            exc.printStackTrace()
            //todo: add handling errors
        }
    }

    override suspend fun deleteTodoItem(todoItemId: String) {
        local.deleteTodoItem(todoItemId)
        try {
            remote.deleteTodoItem(todoItemId)
        } catch (exc: Exception) {
            exc.printStackTrace()
            //todo: add handling errors
        }
    }

    private fun generateTodoItemId() = Random(
        System.currentTimeMillis()
    ).nextInt(0, 100000).toString()
}