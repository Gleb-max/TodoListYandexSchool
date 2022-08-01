package school.yandex.todolist.domain.usecase

import school.yandex.todolist.domain.entity.TodoItem
import school.yandex.todolist.domain.repository.TodoItemsRepository

class EditTodoItemUseCase(private val repository: TodoItemsRepository) {

    operator fun invoke(todoItem: TodoItem) = repository.editTodoItem(todoItem)
}