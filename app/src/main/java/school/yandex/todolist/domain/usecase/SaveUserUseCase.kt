package school.yandex.todolist.domain.usecase

import school.yandex.todolist.domain.entity.User
import school.yandex.todolist.domain.repository.UserRepository
import javax.inject.Inject

class SaveUserUseCase @Inject constructor(private val repository: UserRepository) {

    suspend operator fun invoke(user: User) = repository.saveUser(user)
}