package school.yandex.todolist.data.source.local.db

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface TodoListDao {

    @Query("SELECT * FROM todo_items")
    fun getTodoList(): LiveData<List<TodoItemDbModel>>

    @Query("SELECT * FROM todo_items WHERE id=:todoItemId LIMIT 1")
    suspend fun getTodoItem(todoItemId: Int): TodoItemDbModel

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addTodoItem(todoItem: TodoItemDbModel)

    @Query("DELETE FROM todo_items WHERE id=:todoItemId")
    suspend fun deleteTodoItem(todoItemId: Int)
}