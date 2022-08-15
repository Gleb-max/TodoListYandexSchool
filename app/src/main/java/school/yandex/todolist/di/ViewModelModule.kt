package school.yandex.todolist.di

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import school.yandex.todolist.presentation.stateholder.MainViewModel
import school.yandex.todolist.presentation.stateholder.TodoItemViewModel
import school.yandex.todolist.presentation.stateholder.TodoListViewModel

@Module
interface ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel::class)
    fun bindMainViewModel(viewModel: MainViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(TodoListViewModel::class)
    fun bindTodoListViewModel(viewModel: TodoListViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(TodoItemViewModel::class)
    fun bindTodoItemViewModel(viewModel: TodoItemViewModel): ViewModel
}