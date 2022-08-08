package school.yandex.todolist.data.repository.remote.api

import retrofit2.http.*
import school.yandex.todolist.data.repository.remote.model.TodoItemDTO
import school.yandex.todolist.data.repository.remote.model.response.TodoItemResponse
import school.yandex.todolist.data.repository.remote.model.response.TodoListResponse

interface TodoApi {

    @GET("list")
    suspend fun fetchTodoList(): TodoListResponse

    @PATCH("list")
    suspend fun patchTodoList(): TodoListResponse

    @GET("list/{item_id}")
    suspend fun fetchTodoItem(@Path("item_id") todoItemId: String): TodoItemResponse

    @POST("list")
    suspend fun createTodoItem(@Body todoItem: TodoItemDTO)

    @PUT("list/{item_id}")
    suspend fun editTodoItem(@Path("item_id") todoItemId: String, @Body todoItem: TodoItemDTO)

    @DELETE("list/{item_id}")
    suspend fun deleteTodoItem(@Path("item_id") todoItemId: String)
}