package school.yandex.todolist.data.remote.model

import com.google.gson.annotations.SerializedName
import school.yandex.todolist.domain.entity.TodoItemImportance

data class TodoItemDTO(
    @SerializedName("id") val id: String,
    @SerializedName("text") val text: String,
    @SerializedName("importance") val importance: String,
    @SerializedName("deadline") val deadline: Long?,
    @SerializedName("done") val isDone: Boolean,
    @SerializedName("color") val color: String?,
    @SerializedName("created_at") val createdAt: Long,
    @SerializedName("changed_at") val changedAt: Long?,
    @SerializedName("last_updated_by") val lastUpdatedBy: String,
)