package school.yandex.todolist.domain.usecase

import school.yandex.todolist.domain.repository.TodoItemsRepository

class DeleteTodoItemUseCase(private val repository: TodoItemsRepository) {

    suspend operator fun invoke(todoItemId: String) = repository.deleteTodoItem(todoItemId)
}