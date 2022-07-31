package school.yandex.todolist.domain.usecase

import androidx.lifecycle.LiveData
import school.yandex.todolist.domain.entity.TodoItem
import school.yandex.todolist.domain.repository.TodoItemsRepository

class GetTodoListUseCase(private val repository: TodoItemsRepository) {

    suspend operator fun invoke(): LiveData<List<TodoItem>> = repository.getTodoList()
}