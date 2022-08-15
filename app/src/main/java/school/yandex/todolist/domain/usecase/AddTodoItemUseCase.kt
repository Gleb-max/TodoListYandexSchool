package school.yandex.todolist.domain.usecase

import school.yandex.todolist.domain.entity.TodoItem
import school.yandex.todolist.domain.repository.TodoItemsRepository
import javax.inject.Inject

class AddTodoItemUseCase @Inject constructor(private val repository: TodoItemsRepository) {

    suspend operator fun invoke(todoItem: TodoItem) = repository.addTodoItem(todoItem)
}