package school.yandex.todolist.domain.usecase

import school.yandex.todolist.domain.repository.UserRepository
import javax.inject.Inject

class GetUserUseCase @Inject constructor(private val repository: UserRepository) {

    suspend operator fun invoke() = repository.getUser()
}