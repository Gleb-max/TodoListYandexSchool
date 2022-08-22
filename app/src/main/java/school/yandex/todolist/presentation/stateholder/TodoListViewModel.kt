package school.yandex.todolist.presentation.stateholder

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import school.yandex.todolist.core.viewmodel.BaseViewModel
import school.yandex.todolist.domain.entity.TodoItem
import school.yandex.todolist.domain.usecase.DeleteTodoItemUseCase
import school.yandex.todolist.domain.usecase.EditTodoItemUseCase
import school.yandex.todolist.domain.usecase.GetTodoListUseCase
import javax.inject.Inject

class TodoListViewModel @Inject constructor(
    private val getTodoListUseCase: GetTodoListUseCase,
    private val editTodoItemUseCase: EditTodoItemUseCase,
    private val deleteTodoItemUseCase: DeleteTodoItemUseCase,
) : BaseViewModel() {

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isError = MutableLiveData(false)
    val isError: LiveData<Boolean> = _isError

    private val _isAllItems = MutableLiveData(false)
    val isAllItems: LiveData<Boolean> = _isAllItems

    private val _allTodoItems = MutableLiveData<List<TodoItem>>(listOf())
    val allTodoItems: LiveData<List<TodoItem>> = _allTodoItems

    private val _todoList = MutableLiveData<List<TodoItem>>(listOf())
    val todoList: LiveData<List<TodoItem>> = _todoList

    fun changeItemsVisibility() {
        _isAllItems.value = !isAllItems.value!!
        applyFilters()
    }

    private fun applyFilters() {
        _todoList.postValue(if (_isAllItems.value!!) {
            _allTodoItems.value
        } else {
            _allTodoItems.value!!.filter { !it.isDone }
        })
    }

    fun fetchTodoList(
        onError: (() -> Unit)? = null,
        onSuccess: (() -> Unit)? = null
    ) {
        viewModelScope.execute(onSuccess, onError) {
            val todoList = getTodoListUseCase()
            _allTodoItems.postValue(todoList)
            applyFilters()
        }
    }

    fun deleteTodoItem(
        todoItem: TodoItem,
        onError: (() -> Unit)? = null,
        onSuccess: (() -> Unit)? = null
    ) {
        viewModelScope.execute(onSuccess, onError) {
            deleteTodoItemUseCase(todoItem.id)
        }
    }

    fun changeStatusTodoItem(
        todoItem: TodoItem,
        onError: (() -> Unit)? = null,
        onSuccess: (() -> Unit)? = null
    ) {
        viewModelScope.execute(onSuccess, onError) {
            editTodoItemUseCase(todoItem.copy(isDone = !todoItem.isDone))
        }
    }
}