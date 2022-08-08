package school.yandex.todolist.presentation.todoitem

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import school.yandex.todolist.domain.entity.TodoItem
import school.yandex.todolist.domain.entity.TodoItemImportance
import school.yandex.todolist.domain.usecase.AddTodoItemUseCase
import school.yandex.todolist.domain.usecase.DeleteTodoItemUseCase
import school.yandex.todolist.domain.usecase.EditTodoItemUseCase
import school.yandex.todolist.domain.usecase.GetTodoItemUseCase
import java.util.*

class TodoItemViewModel(
    private val getTodoItemUseCase: GetTodoItemUseCase,
    private val editTodoItemUseCase: EditTodoItemUseCase,
    private val addTodoItemUseCase: AddTodoItemUseCase,
    private val deleteTodoItemUseCase: DeleteTodoItemUseCase,
) : ViewModel() {

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

    fun getTodoItem(todoItemId: String) {
        viewModelScope.launch {
            val item = getTodoItemUseCase(todoItemId)
            _todoItemDraft.value = TodoItemDraft.fromEntity(item)
        }
    }

    fun deleteTodoItem(todoItemId: String) {
        viewModelScope.launch {
            deleteTodoItemUseCase(todoItemId)
        }
    }

    fun addTodoItem() {
        viewModelScope.launch {
            //todo: возможно кидать здесь какую-нибудь ошибку если null
            val draftTodoItem = todoItemDraft.value ?: return@launch
            addTodoItemUseCase(
                //todo: переписать здесь также на мапперы как в data слое маппер entityToDTO
                TodoItem(
                    draftTodoItem.id ?: TodoItem.UNDEFINED_ID,
                    draftTodoItem.content,
                    draftTodoItem.importance,
                    draftTodoItem.deadline,
                    draftTodoItem.isDone,
                    draftTodoItem.createdAt ?: Calendar.getInstance().time,
                    draftTodoItem.changedAt
                )
            )
        }
    }

    fun editTodoItem() {
        viewModelScope.launch {
            //todo: возможно кидать здесь какую-нибудь ошибку если null
            val draftTodoItem = todoItemDraft.value ?: return@launch
            editTodoItemUseCase(
                //todo: переписать здесь также на мапперы как в data слое маппер entityToDTO
                TodoItem(
                    draftTodoItem.id ?: TodoItem.UNDEFINED_ID,
                    draftTodoItem.content,
                    draftTodoItem.importance,
                    draftTodoItem.deadline,
                    draftTodoItem.isDone,
                    draftTodoItem.createdAt ?: Calendar.getInstance().time,
                    draftTodoItem.changedAt
                )
            )
        }
    }
}