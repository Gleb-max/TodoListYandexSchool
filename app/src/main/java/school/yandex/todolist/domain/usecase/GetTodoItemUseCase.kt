package school.yandex.todolist.domain.usecase

import school.yandex.todolist.domain.repository.TodoItemsRepository

class GetTodoItemUseCase(private val repository: TodoItemsRepository) {

    operator fun invoke(todoItemId: String) = repository.getTodoItem(todoItemId)
}