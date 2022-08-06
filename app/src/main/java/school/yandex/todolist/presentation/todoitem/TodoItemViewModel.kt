package school.yandex.todolist.presentation.todoitem

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import school.yandex.todolist.data.repository.TodoItemsRepositoryImpl
import school.yandex.todolist.domain.entity.TodoItem
import school.yandex.todolist.domain.entity.TodoItemImportance
import school.yandex.todolist.domain.usecase.*
import java.util.*

class TodoItemViewModel : ViewModel() {

    //todo: add di to pass usecases as parameters
    private val repository = TodoItemsRepositoryImpl()
    private val getTodoListUseCase = GetTodoListUseCase(repository)
    private val getTodoItemUseCase = GetTodoItemUseCase(repository)
    private val addTodoItemUseCase = AddTodoItemUseCase(repository)
    private val editTodoItemUseCase = EditTodoItemUseCase(repository)
    private val deleteTodoItemUseCase = DeleteTodoItemUseCase(repository)

    private val _todoItemDraft = MutableLiveData<TodoItemDraft?>()
    val todoItemDraft: LiveData<TodoItemDraft?>
        get() = _todoItemDraft

    fun getTodoItem(todoItemId: String) {
        viewModelScope.launch {
            val item = getTodoItemUseCase(todoItemId)
            if (item != null) _todoItemDraft.value = TodoItemDraft.fromEntity(item)
            else TODO("add logic when item is null (error / pop back / ...)")
        }
    }

    fun deleteTodoItem() {
//        viewModelScope.launch {
//            _todoItem.value?.let { deleteTodoItemUseCase(it) }
//        }
    }

    fun addTodoItem(content: String, importance: TodoItemImportance, deadline: Date) {
        addTodoItemUseCase(
            TodoItem(
                TodoItem.UNDEFINED_ID,
                content,
                importance,
                deadline,
                false,
                Calendar.getInstance().time,
                null
            )
        )
    }

    fun editTodoItem(content: String, importance: TodoItemImportance, deadline: Date) {
//        _todoItem.value?.let {
//            editTodoItemUseCase(
//                it.copy(
//                    content = content,
//                    importance = importance,
//                    deadline = deadline,
//                    lastUpdatedBy = Calendar.getInstance().time
//                )
//            )
//        }
    }
}