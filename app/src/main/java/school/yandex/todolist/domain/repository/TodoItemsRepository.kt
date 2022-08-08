package school.yandex.todolist.domain.repository

import school.yandex.todolist.domain.entity.TodoItem

interface TodoItemsRepository {

    suspend fun getTodoList(): List<TodoItem>

    suspend fun getTodoItem(todoItemId: String): TodoItem

    suspend fun addTodoItem(todoItem: TodoItem)

    suspend fun editTodoItem(todoItem: TodoItem)

    suspend fun deleteTodoItem(todoItemId: String)
}