package school.yandex.todolist.domain.usecase

import school.yandex.todolist.domain.repository.TodoItemsRepository
import javax.inject.Inject

class LoadTodoListUseCase @Inject constructor(private val repository: TodoItemsRepository) {

    suspend operator fun invoke() = repository.loadTodoList()
}