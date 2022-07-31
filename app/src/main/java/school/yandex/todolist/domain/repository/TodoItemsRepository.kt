package school.yandex.todolist.domain.repository

import androidx.lifecycle.LiveData
import school.yandex.todolist.domain.entity.TodoItem

interface TodoItemsRepository {

    suspend fun getTodoList(): LiveData<List<TodoItem>>

    suspend fun getTodoItem(todoItemId: Int): TodoItem

    suspend fun addTodoItem(todoItem: TodoItem)

    suspend fun deleteTodoItem(todoItem: TodoItem)
}