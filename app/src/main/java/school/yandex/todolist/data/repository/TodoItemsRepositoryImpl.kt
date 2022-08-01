package school.yandex.todolist.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import school.yandex.todolist.data.repository.mock.TodoMock
import school.yandex.todolist.domain.entity.TodoItem
import school.yandex.todolist.domain.repository.TodoItemsRepository

class TodoItemsRepositoryImpl : TodoItemsRepository {

    private val todoListLD = MutableLiveData<List<TodoItem>>()
    private val todoList = sortedSetOf<TodoItem>(
        { o1, o2 -> o1.id.toInt().compareTo(o2.id.toInt()) }
    )

    private var autoIncrementId = 0

    init {

        TodoMock.todoList.forEach {
            addTodoItem(it)
        }
        autoIncrementId = todoList.last().id.toInt() + 1
    }

    override fun getTodoList(): LiveData<List<TodoItem>> {
        return todoListLD
    }

    override fun getTodoItem(todoItemId: String): TodoItem {
        return todoList.find {
            it.id == todoItemId
        } ?: throw RuntimeException("Element with id $todoItemId not found")
    }

    override fun addTodoItem(todoItem: TodoItem) {
        val item = if (todoItem.id == TodoItem.UNDEFINED_ID) {
            todoItem.copy(id = autoIncrementId++.toString())
        } else todoItem
        todoList.add(item)
        updateList()
    }

    override fun editTodoItem(todoItem: TodoItem) {
        val oldElement = getTodoItem(todoItem.id)
        todoList.remove(oldElement)
        addTodoItem(todoItem)
    }

    override fun deleteTodoItem(todoItem: TodoItem) {
        todoList.remove(todoItem)
        updateList()
    }

    private fun updateList() {
        todoListLD.value = todoList.toList()
    }
}