package school.yandex.todolist.presentation.todolist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import school.yandex.todolist.domain.entity.TodoItem
import school.yandex.todolist.domain.usecase.DeleteTodoItemUseCase
import school.yandex.todolist.domain.usecase.EditTodoItemUseCase
import school.yandex.todolist.domain.usecase.GetTodoListUseCase

class TodoListViewModel(
    private val getTodoListUseCase: GetTodoListUseCase,
    private val editTodoItemUseCase: EditTodoItemUseCase,
    private val deleteTodoItemUseCase: DeleteTodoItemUseCase,
) : ViewModel() {

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isError = MutableLiveData(false)
    val isError: LiveData<Boolean> = _isError

    private val _todoList = MutableLiveData<List<TodoItem>>(listOf())
    val todoList: LiveData<List<TodoItem>> = _todoList

    fun fetchTodoList() {
//        viewModelScope.launch {
//            withContext(Dispatchers.IO) {
//                _todoList.value = getTodoListUseCase()
//            }
//        }
        viewModelScope.runCatching {
            viewModelScope.launch {
                _todoList.value = getTodoListUseCase()
            }
        }
    }

    fun deleteTodoItem(todoItem: TodoItem) {
        viewModelScope.launch {
            deleteTodoItemUseCase(todoItem.id)
        }
    }

    fun changeStatusTodoItem(todoItem: TodoItem) {
        viewModelScope.launch {
            editTodoItemUseCase(todoItem.copy(isDone = !todoItem.isDone))
        }
    }
}