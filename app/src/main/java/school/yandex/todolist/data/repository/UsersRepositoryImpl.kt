package school.yandex.todolist.data.repository

import school.yandex.todolist.data.local.UserPreferences
import school.yandex.todolist.domain.entity.User
import school.yandex.todolist.domain.repository.UserRepository

class UsersRepositoryImpl(
    private val local: UserPreferences
) : UserRepository {

    override suspend fun getUser(): User = local.getUser()

    override suspend fun saveUser(user: User) = local.saveUser(user)
}