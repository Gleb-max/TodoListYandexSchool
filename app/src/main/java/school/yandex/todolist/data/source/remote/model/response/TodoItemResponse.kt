package school.yandex.todolist.data.source.remote.model.response

import com.google.gson.annotations.SerializedName
import school.yandex.todolist.data.source.remote.model.TodoItemDTO

data class TodoItemResponse(
    @SerializedName("status") val status: String,
    @SerializedName("element") val todoItem: TodoItemDTO,
    @SerializedName("revision") val revision: Int,
)