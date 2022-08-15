package school.yandex.todolist.di

import android.app.Application
import dagger.BindsInstance
import dagger.Component
import school.yandex.todolist.TodoApp
import school.yandex.todolist.presentation.view.MainActivity
import school.yandex.todolist.presentation.view.TodoItemFragment
import school.yandex.todolist.presentation.view.TodoListFragment

/**
 * главный компонент приложения
 */
@ApplicationScope
@Component(
    modules = [
        DataModule::class,
        ViewModelModule::class,
        WorkerModule::class
    ]
)
interface ApplicationComponent {

    fun inject(application: TodoApp)

    fun inject(activity: MainActivity)

    fun inject(todoListFragment: TodoListFragment)

    fun inject(todoItemFragment: TodoItemFragment)

    @Component.Factory
    interface Factory {

        fun create(
            @BindsInstance application: Application
        ): ApplicationComponent
    }
}