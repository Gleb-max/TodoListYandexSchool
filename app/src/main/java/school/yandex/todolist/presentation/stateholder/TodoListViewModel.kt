package school.yandex.todolist.presentation.stateholder

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import school.yandex.todolist.core.viewmodel.BaseViewModel
import school.yandex.todolist.domain.entity.TodoItem
import school.yandex.todolist.domain.usecase.*
import javax.inject.Inject

class TodoListViewModel @Inject constructor(
    private val getTodoListUseCase: GetTodoListUseCase,
    private val loadTodoListUseCase: LoadTodoListUseCase,
    private val patchTodoListUseCase: PatchTodoListUseCase,
    private val editTodoItemUseCase: EditTodoItemUseCase,
    private val deleteTodoItemUseCase: DeleteTodoItemUseCase,
) : BaseViewModel() {

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isError = MutableLiveData(false)
    val isError: LiveData<Boolean> = _isError

    private val _isAllItems = MutableLiveData(false)
    val isAllItems: LiveData<Boolean> = _isAllItems

    val allTodoItems = getTodoListUseCase()

    fun changeItemsVisibility() {
        _isAllItems.value = !isAllItems.value!!
    }

    fun fetchTodoList(
        onError: (() -> Unit)? = null,
        onSuccess: (() -> Unit)? = null
    ) {
        viewModelScope.execute(onSuccess, onError) {
            loadTodoListUseCase()
        }
    }

    fun patchTodoList() {
        viewModelScope.execute {
            patchTodoListUseCase()
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