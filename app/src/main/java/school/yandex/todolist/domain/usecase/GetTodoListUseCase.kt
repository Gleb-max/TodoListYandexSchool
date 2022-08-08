package school.yandex.todolist.domain.usecase

import school.yandex.todolist.domain.entity.TodoItem
import school.yandex.todolist.domain.repository.TodoItemsRepository

class GetTodoListUseCase(private val repository: TodoItemsRepository) {

    suspend operator fun invoke(): List<TodoItem> = repository.getTodoList()
}