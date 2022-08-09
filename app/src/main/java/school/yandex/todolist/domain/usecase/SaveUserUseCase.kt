package school.yandex.todolist.domain.usecase

import school.yandex.todolist.domain.entity.User
import school.yandex.todolist.domain.repository.UserRepository

class SaveUserUseCase(private val repository: UserRepository) {

    suspend operator fun invoke(user: User) = repository.saveUser(user)
}