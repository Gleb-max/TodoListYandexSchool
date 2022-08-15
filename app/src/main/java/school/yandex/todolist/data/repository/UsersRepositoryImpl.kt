package school.yandex.todolist.data.repository

import school.yandex.todolist.data.source.local.UserPreferences
import school.yandex.todolist.domain.entity.User
import school.yandex.todolist.domain.repository.UserRepository
import javax.inject.Inject

class UsersRepositoryImpl @Inject constructor(
    private val local: UserPreferences
) : UserRepository {

    override suspend fun getUser(): User = local.getUser()

    override suspend fun saveUser(user: User) = local.saveUser(user)
}