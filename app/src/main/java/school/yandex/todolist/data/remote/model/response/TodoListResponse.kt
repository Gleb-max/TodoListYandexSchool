package school.yandex.todolist.data.remote.model.response

import com.google.gson.annotations.SerializedName
import school.yandex.todolist.data.remote.model.TodoItemDTO

data class TodoListResponse(
    @SerializedName("status") val status: String,
    @SerializedName("list") val todoItems: List<TodoItemDTO>,
    @SerializedName("revision") val revision: Int,
)