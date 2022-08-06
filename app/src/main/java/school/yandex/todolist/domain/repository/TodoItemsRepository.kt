package school.yandex.todolist.domain.repository

import androidx.lifecycle.LiveData
import school.yandex.todolist.domain.entity.TodoItem

interface TodoItemsRepository {

    fun getTodoList(): LiveData<List<TodoItem>>

    fun getTodoItem(todoItemId: String): TodoItem?

    fun addTodoItem(todoItem: TodoItem)

    fun editTodoItem(todoItem: TodoItem)

    fun deleteTodoItem(todoItem: TodoItem)
}