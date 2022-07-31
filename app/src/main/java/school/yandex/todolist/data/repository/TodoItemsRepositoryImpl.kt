package school.yandex.todolist.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import school.yandex.todolist.data.repository.mock.TodoMock
import school.yandex.todolist.domain.entity.TodoItem
import school.yandex.todolist.domain.repository.TodoItemsRepository
import java.lang.RuntimeException

class TodoItemsRepositoryImpl : TodoItemsRepository {

    private val todoListLD = MutableLiveData<List<TodoItem>>()
    private val todoList = sortedSetOf<TodoItem>({ o1, o2 -> o1.createdAt.compareTo(o2.createdAt) })

    private var autoIncrementId = 0

    init {

        todoList.addAll(TodoMock.todoList)
    }

    override suspend fun getTodoList(): LiveData<List<TodoItem>> {
        return todoListLD
    }

    override suspend fun getTodoItem(todoItemId: String): TodoItem {
        return todoList.find {
            it.id == todoItemId
        } ?: throw RuntimeException("Element with id $todoItemId not found")
    }

    override suspend fun addTodoItem(todoItem: TodoItem) {
        val item = if (todoItem.id == TodoItem.UNDEFINED_ID) {
            todoItem.copy(id = autoIncrementId++.toString())
        } else todoItem
        todoList.add(item)
        updateList()
    }

    override suspend fun editTodoItem(todoItem: TodoItem) {
        val oldElement = getTodoItem(todoItem.id)
        todoList.remove(oldElement)
        addTodoItem(todoItem)
    }

    override suspend fun deleteTodoItem(todoItem: TodoItem) {
        todoList.remove(todoItem)
        updateList()
    }

    private fun updateList() {
        todoListLD.value = todoList.toList()
    }
}