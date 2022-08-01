package school.yandex.todolist.presentation.todolist

import androidx.lifecycle.*
import kotlinx.coroutines.launch
import school.yandex.todolist.data.repository.TodoItemsRepositoryImpl
import school.yandex.todolist.domain.entity.TodoItem
import school.yandex.todolist.domain.usecase.*

class TodoListViewModel : ViewModel() {

    // add di to pass usecases as parameters
    private val repository = TodoItemsRepositoryImpl()
    private val getTodoListUseCase = GetTodoListUseCase(repository)
    private val getTodoItemUseCase = GetTodoItemUseCase(repository)
    private val addTodoItemUseCase = AddTodoItemUseCase(repository)
    private val editTodoItemUseCase = EditTodoItemUseCase(repository)
    private val deleteTodoItemUseCase = DeleteTodoItemUseCase(repository)

    val todoList = getTodoListUseCase()

    fun deleteTodoItem(todoItem: TodoItem) {
        viewModelScope.launch {
            deleteTodoItemUseCase(todoItem)
        }
    }

    fun changeStatusTodoItem(todoItem: TodoItem) {
        viewModelScope.launch {
            editTodoItemUseCase(todoItem.copy(isDone = !todoItem.isDone))
        }
    }
}