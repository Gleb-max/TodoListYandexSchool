package school.yandex.todolist.domain.usecase

import school.yandex.todolist.domain.repository.UserRepository

class GetUserUseCase(private val repository: UserRepository) {

    suspend operator fun invoke() = repository.getUser()
}