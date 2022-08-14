package school.yandex.todolist.domain.entity

data class User(
    val uid: String? = null,
    val name: String,
) {

    companion object {

        fun emptyUser() = User(null, "")
    }
}