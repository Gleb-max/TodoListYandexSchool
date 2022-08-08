package school.yandex.todolist.presentation

import android.app.Application
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.WorkManager
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.component.KoinComponent
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import org.koin.core.module.Module
import school.yandex.todolist.data.di.dataModules
import school.yandex.todolist.domain.di.domainMoules
import school.yandex.todolist.presentation.di.presentationModules
import school.yandex.todolist.presentation.worker.TodoSyncWorker
import java.util.*

class TodoApp : Application(), KoinComponent {

    override fun onCreate() {
        super.onCreate()
        initKoin()

        startWorker()
    }

    //todo перейти на dagger/hilt, главный минус койна знаю,
    // но хочу сначала послушать лекцию по даггеру
    private fun initKoin() {
        startKoin {
            androidLogger(Level.DEBUG)
            androidContext(this@TodoApp)
            modules(LinkedList<Module>().apply {
                addAll(dataModules)
                addAll(domainMoules)
                addAll(presentationModules)
            })
        }
    }

    private fun startWorker() {
        val workManager = WorkManager.getInstance(applicationContext)
        workManager.enqueueUniquePeriodicWork(
            TodoSyncWorker.WORK_NAME,
            ExistingPeriodicWorkPolicy.KEEP,
            TodoSyncWorker.makeRequest()
        )
    }
}