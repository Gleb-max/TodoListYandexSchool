package school.yandex.todolist.data.repository

import androidx.lifecycle.LiveData
import school.yandex.todolist.domain.entity.TodoItem
import school.yandex.todolist.domain.repository.TodoItemsRepository

class TodoItemsRepositoryImpl : TodoItemsRepository {

    override suspend fun getTodoList(): LiveData<List<TodoItem>> {
        TODO("Not yet implemented")
    }

    override suspend fun getTodoItem(todoItemId: Int): TodoItem {
        TODO("Not yet implemented")
    }

    override suspend fun addTodoItem(todoItem: TodoItem) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteTodoItem(todoItem: TodoItem) {
        TODO("Not yet implemented")
    }
}