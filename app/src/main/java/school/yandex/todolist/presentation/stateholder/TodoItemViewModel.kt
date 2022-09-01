package school.yandex.todolist.presentation.stateholder

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import school.yandex.todolist.core.viewmodel.BaseViewModel
import school.yandex.todolist.domain.entity.TodoItemImportance
import school.yandex.todolist.domain.usecase.AddTodoItemUseCase
import school.yandex.todolist.domain.usecase.DeleteTodoItemUseCase
import school.yandex.todolist.domain.usecase.EditTodoItemUseCase
import school.yandex.todolist.domain.usecase.GetTodoItemUseCase
import school.yandex.todolist.presentation.view.TodoItemDraft
import java.util.*
import javax.inject.Inject

class TodoItemViewModel @Inject constructor(
    private val getTodoItemUseCase: GetTodoItemUseCase,
    private val editTodoItemUseCase: EditTodoItemUseCase,
    private val addTodoItemUseCase: AddTodoItemUseCase,
    private val deleteTodoItemUseCase: DeleteTodoItemUseCase,
) : BaseViewModel() {

    private val _todoItemDraft = MutableLiveData(TodoItemDraft.empty())
    val todoItemDraft: LiveData<TodoItemDraft>
        get() = _todoItemDraft

    fun updateDraft(
        content: String? = null,
        deadline: Date? = null,
        importance: TodoItemImportance? = null
    ) {
        //todo возможно есть способ сделать лучше
        // не стал делать поля var, так как посчитал, что тогда вью сможет их изменять
        if (content != null) _todoItemDraft.value = todoItemDraft.value?.copy(content = content)
        if (deadline != null) _todoItemDraft.value = todoItemDraft.value?.copy(deadline = deadline)
        if (importance != null) _todoItemDraft.value =
            todoItemDraft.value?.copy(importance = importance)
    }

    fun getTodoItem(
        todoItemId: String,
        onError: (() -> Unit)? = null,
        onSuccess: (() -> Unit)? = null
    ) {
        viewModelScope.execute(onSuccess, onError) {
            val item = getTodoItemUseCase(todoItemId)
            _todoItemDraft.postValue(TodoItemDraft.fromEntity(item))
        }
    }

    fun deleteTodoItem(
        todoItemId: String,
        onError: (() -> Unit)? = null,
        onSuccess: (() -> Unit)? = null
    ) {
        viewModelScope.execute(onSuccess, onError) {
            deleteTodoItemUseCase(todoItemId)
        }
    }

    fun addTodoItem(
        onError: (() -> Unit)? = null,
        onSuccess: (() -> Unit)? = null
    ) {
        viewModelScope.execute(onSuccess, onError) {
            //todo: возможно кидать здесь какую-нибудь ошибку если null
            val draftTodoItem = todoItemDraft.value ?: return@execute
            addTodoItemUseCase(
                draftTodoItem.toEntity()
            )
        }
    }

    fun editTodoItem(
        onError: (() -> Unit)? = null,
        onSuccess: (() -> Unit)? = null
    ) {
        viewModelScope.execute(onSuccess, onError) {
            //todo: возможно кидать здесь какую-нибудь ошибку если null
            val draftTodoItem = todoItemDraft.value ?: return@execute
            editTodoItemUseCase(
                draftTodoItem.toEntity()
            )
        }
    }
}