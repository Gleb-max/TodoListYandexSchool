package school.yandex.todolist.data.source.remote.model.request

import com.google.gson.annotations.SerializedName
import school.yandex.todolist.data.source.remote.model.TodoItemDTO

class TodoItemsRequest(
    @SerializedName("list") val todos: List<TodoItemDTO>,
    @SerializedName("status") val status: String = "ok",
)