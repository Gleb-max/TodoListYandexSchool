package school.yandex.todolist.data.source.local.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface TodoListDao {

    @Query("SELECT * FROM todo_items")
    fun getTodoList(): List<TodoItemDbModel>

    @Query("SELECT * FROM todo_items WHERE id=:todoItemId LIMIT 1")
    suspend fun getTodoItem(todoItemId: String): TodoItemDbModel

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addTodoItems(todoItems: List<TodoItemDbModel>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addTodoItem(todoItem: TodoItemDbModel)

    @Query("DELETE FROM todo_items WHERE id=:todoItemId")
    suspend fun deleteTodoItem(todoItemId: String)
}