package school.yandex.todolist.data.repository.mock

import school.yandex.todolist.domain.entity.TodoItem
import school.yandex.todolist.domain.entity.TodoItemImportance
import java.util.*

class TodoMock {


    companion object {

        val todoList = listOf(
            TodoItem(
                "1",
                "Купить что-то",
                TodoItemImportance.BASIC,
                null,
                false,
                Calendar.getInstance().time,
                null
            ),
            TodoItem(
                "2",
                "Купить что-то, где-то, зачем-то, но зачем не очень понятно",
                TodoItemImportance.BASIC,
                null,
                false,
                Calendar.getInstance().time,
                null
            ),
            TodoItem(
                "3",
                "Купить что-то, где-то, зачем-то, но зачем не очень понятно, но точно чтобы показать как обр…",
                TodoItemImportance.BASIC,
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
                false,
                Calendar.getInstance().time,
                null
            ),
            TodoItem(
                "5",
                "Купить что-то неважное",
                TodoItemImportance.LOW,
                null,
                false,
                Calendar.getInstance().time,
                null
            ),
            TodoItem(
                "6",
                "Купить что-то с датой",
                TodoItemImportance.BASIC,
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
                false,
                Calendar.getInstance().time,
                null
            ),
            TodoItem(
                "11",
                "Купить что-то, где-то, зачем-то, но зачем не очень понятно, но точно чтобы показать как обр…",
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
    }
}