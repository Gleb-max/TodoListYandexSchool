package school.yandex.todolist.domain.repository

import androidx.lifecycle.LiveData
import school.yandex.todolist.domain.entity.TodoItem

interface TodoItemsRepository {

    fun getTodoList(): LiveData<List<TodoItem>>

    suspend fun getTodoItem(todoItemId: String): TodoItem

    suspend fun addTodoItem(todoItem: TodoItem)

    suspend fun editTodoItem(todoItem: TodoItem)

    suspend fun deleteTodoItem(todoItemId: String)

    suspend fun loadTodoList()

    suspend fun patchTodoList()
}