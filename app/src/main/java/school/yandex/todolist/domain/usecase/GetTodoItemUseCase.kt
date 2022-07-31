package school.yandex.todolist.domain.usecase

import school.yandex.todolist.domain.repository.TodoItemsRepository

class GetTodoItemUseCase(private val repository: TodoItemsRepository) {

    suspend operator fun invoke(todoItemId: Int) = repository.getTodoItem(todoItemId)
}