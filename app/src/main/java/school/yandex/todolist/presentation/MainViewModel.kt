package school.yandex.todolist.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import school.yandex.todolist.data.repository.TodoItemsRepositoryImpl
import school.yandex.todolist.domain.entity.TodoItem
import school.yandex.todolist.domain.usecase.AddTodoItemUseCase
import school.yandex.todolist.domain.usecase.DeleteTodoItemUseCase
import school.yandex.todolist.domain.usecase.GetTodoListUseCase

class MainViewModel : ViewModel() {

    // add di to pass usecases as parameters
    private val repository = TodoItemsRepositoryImpl()
    private val getTodoListUseCase = GetTodoListUseCase(repository)
    private val getTodoItemUseCase = GetTodoListUseCase(repository)
    private val addTodoItemUseCase = AddTodoItemUseCase(repository)
    private val deleteTodoItemUseCase = DeleteTodoItemUseCase(repository)

    private var _todoList: LiveData<List<TodoItem>> = MutableLiveData(listOf())
    val todoList = _todoList

    init {

        viewModelScope.launch {
            _todoList = getTodoListUseCase()
        }
    }
}