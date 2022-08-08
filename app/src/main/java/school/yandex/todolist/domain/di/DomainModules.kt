package school.yandex.todolist.domain.di

import org.koin.dsl.module
import school.yandex.todolist.domain.usecase.*

val domainMoules
    get() = listOf(useCaseModule)

private val useCaseModule = module {
    factory { GetTodoListUseCase(get()) }
    factory { GetTodoItemUseCase(get()) }
    factory { EditTodoItemUseCase(get()) }
    factory { AddTodoItemUseCase(get()) }
    factory { DeleteTodoItemUseCase(get()) }
}