package school.yandex.todolist.presentation.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import school.yandex.todolist.presentation.main.MainViewModel
import school.yandex.todolist.presentation.todoitem.TodoItemViewModel
import school.yandex.todolist.presentation.todolist.TodoListViewModel

val presentationModules
    get() = listOf(
        todos
    )

private val todos = module {
    viewModel { TodoItemViewModel(get(), get(), get(), get()) }
    viewModel { TodoListViewModel(get(), get(), get()) }
    viewModel { MainViewModel(get(), get()) }
}