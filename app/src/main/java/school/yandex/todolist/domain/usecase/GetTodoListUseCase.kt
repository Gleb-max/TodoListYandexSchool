package school.yandex.todolist.domain.usecase

import androidx.lifecycle.LiveData
import school.yandex.todolist.domain.entity.TodoItem
import school.yandex.todolist.domain.repository.TodoItemsRepository
import javax.inject.Inject

class GetTodoListUseCase @Inject constructor(private val repository: TodoItemsRepository) {

    operator fun invoke(): LiveData<List<TodoItem>> = repository.getTodoList()
}