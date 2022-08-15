package school.yandex.todolist.domain.usecase

import school.yandex.todolist.domain.repository.TodoItemsRepository
import javax.inject.Inject

class GetTodoItemUseCase @Inject constructor(private val repository: TodoItemsRepository) {

    suspend operator fun invoke(todoItemId: String) = repository.getTodoItem(todoItemId)
}