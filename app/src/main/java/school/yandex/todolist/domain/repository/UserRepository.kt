package school.yandex.todolist.domain.repository

import school.yandex.todolist.domain.entity.User

interface UserRepository {

    suspend fun getUser(): User

    suspend fun saveUser(user: User)
}