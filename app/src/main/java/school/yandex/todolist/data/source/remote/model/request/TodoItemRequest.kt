package school.yandex.todolist.data.source.remote.model.request

import com.google.gson.annotations.SerializedName
import school.yandex.todolist.data.source.remote.model.TodoItemDTO

class TodoItemRequest(
    @SerializedName("element") val todo: TodoItemDTO,
    @SerializedName("status") val status: String = "ok",
)