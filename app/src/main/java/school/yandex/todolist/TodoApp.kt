package school.yandex.todolist

import android.app.Application
import androidx.work.Configuration
import school.yandex.todolist.data.worker.TodoWorkerFactory
import school.yandex.todolist.di.DaggerApplicationComponent
import javax.inject.Inject

class TodoApp : Application(), Configuration.Provider {

    @Inject
    lateinit var todoSyncWorkerFactory: TodoWorkerFactory

    val component by lazy {
        DaggerApplicationComponent.factory().create(this)
    }

    override fun onCreate() {
        component.inject(this)
        super.onCreate()
    }

    override fun getWorkManagerConfiguration(): Configuration {
        return Configuration.Builder().setWorkerFactory(todoSyncWorkerFactory).build()
    }
}