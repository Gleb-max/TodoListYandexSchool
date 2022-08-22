package school.yandex.todolist.data.repository

import kotlinx.coroutines.delay
import school.yandex.todolist.data.mapper.TodoListMapper
import school.yandex.todolist.data.source.local.RevisionPreferences
import school.yandex.todolist.data.source.remote.api.TodoApi
import school.yandex.todolist.domain.entity.TodoItem
import school.yandex.todolist.domain.entity.TodoItemImportance
import school.yandex.todolist.domain.repository.TodoItemsRepository
import java.util.*
import javax.inject.Inject

//todo: add local data source to constructor
class TodoItemsRepositoryImpl @Inject constructor(
    private val remote: TodoApi,
    private val revisionPreferences: RevisionPreferences,
    private val mapper: TodoListMapper
) : TodoItemsRepository {

    override suspend fun getTodoList(): List<TodoItem> {
        val todoListResponse = remote.fetchTodoList()
        val lastRevision = todoListResponse.revision
        revisionPreferences.setRevision(lastRevision)
        return listOf(
            TodoItem(
                "1",
                "Купить что-то",
                TodoItemImportance.LOW,
                null,
                true,
                Calendar.getInstance().time,
                null
            ),
            TodoItem(
                "2",
                "Купить что-то, где-то, зачем-то, но зачем не очень понятно",
                TodoItemImportance.BASIC,
                null,
                true,
                Calendar.getInstance().time,
                null
            ),
            TodoItem(
                "3",
                "Купить что-то, где-то, зачем-то, но зачем не очень понятно, но точно чтобы показать как обр…",
                TodoItemImportance.LOW,
                null,
                false,
                Calendar.getInstance().time,
                null
            ),
            TodoItem(
                "4",
                "Купить что-то важное",
                TodoItemImportance.IMPORTANT,
                null,
                true,
                Calendar.getInstance().time,
                null
            ),
            TodoItem(
                "5",
                "Купить что-то неважное",
                TodoItemImportance.IMPORTANT,
                null,
                false,
                Calendar.getInstance().time,
                null
            ),
            TodoItem(
                "6",
                "Купить что-то с датой",
                TodoItemImportance.LOW,
                Calendar.getInstance().time,
                false,
                Calendar.getInstance().time,
                null
            ),
            TodoItem(
                "7",
                "Купить что-то",
                TodoItemImportance.BASIC,
                null,
                false,
                Calendar.getInstance().time,
                null
            ),
            TodoItem(
                "8",
                "Купить что-то",
                TodoItemImportance.BASIC,
                null,
                false,
                Calendar.getInstance().time,
                null
            ),
            TodoItem(
                "9",
                "Купить что-то",
                TodoItemImportance.BASIC,
                null,
                false,
                Calendar.getInstance().time,
                null
            ),
            TodoItem(
                "10",
                "Купить что-то, где-то, зачем-то, но зачем не очень понятно",
                TodoItemImportance.BASIC,
                null,
                true,
                Calendar.getInstance().time,
                null
            ),
            TodoItem(
                "11",
                "Купить что-то, где-то, зачем-то, но зачем не очень понятно, но точно чтобы показать как обtgnjtyjingtiuhgtiugrthuigtrhrtiughgrtiuhrgtiuhgrtiuhgrtiuhgrtrt",
                TodoItemImportance.BASIC,
                null,
                false,
                Calendar.getInstance().time,
                null
            ),
            TodoItem(
                "12",
                "Купить что-то важное",
                TodoItemImportance.IMPORTANT,
                null,
                false,
                Calendar.getInstance().time,
                null
            ),
            TodoItem(
                "13",
                "Купить что-то неважное",
                TodoItemImportance.LOW,
                null,
                false,
                Calendar.getInstance().time,
                null
            ),
            TodoItem(
                "14",
                "Купить что-то с датой",
                TodoItemImportance.BASIC,
                Calendar.getInstance().time,
                false,
                Calendar.getInstance().time,
                null
            ),
            TodoItem(
                "15",
                "Купить что-то",
                TodoItemImportance.BASIC,
                null,
                false,
                Calendar.getInstance().time,
                null
            ),
            TodoItem(
                "16",
                "Купить что-то",
                TodoItemImportance.BASIC,
                null,
                false,
                Calendar.getInstance().time,
                null
            ),
            TodoItem(
                "17",
                "Купить что-то 97",
                TodoItemImportance.IMPORTANT,
                null,
                false,
                Calendar.getInstance().time,
                null
            ),
            TodoItem(
                "18",
                "Купить что-то 98",
                TodoItemImportance.LOW,
                null,
                false,
                Calendar.getInstance().time,
                null
            ),
            TodoItem(
                "19",
                "Купить что-то 99",
                TodoItemImportance.BASIC,
                null,
                false,
                Calendar.getInstance().time,
                null
            ),
            TodoItem(
                "20",
                "Купить что-то 100",
                TodoItemImportance.BASIC,
                null,
                false,
                Calendar.getInstance().time,
                null
            ),
        )
        return mapper.mapListDTOToListEntity(todoListResponse.todoItems)
    }

    override suspend fun getTodoItem(todoItemId: String): TodoItem {
        val todoItemResponse = remote.fetchTodoItem(todoItemId)
        val lastRevision = todoItemResponse.revision
        revisionPreferences.setRevision(lastRevision)
        return mapper.mapDTOToEntity(todoItemResponse.todoItem)
    }

    override suspend fun addTodoItem(todoItem: TodoItem) {
        remote.createTodoItem(mapper.mapEntityToDTO(todoItem))
    }

    override suspend fun editTodoItem(todoItem: TodoItem) {
        val todoItemDTO = mapper.mapEntityToDTO(todoItem)
        remote.editTodoItem(todoItemDTO.id, todoItemDTO)
    }

    override suspend fun deleteTodoItem(todoItemId: String) {
        remote.deleteTodoItem(todoItemId)
    }
}