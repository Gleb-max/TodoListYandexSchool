package school.yandex.todolist.data.worker

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.WorkerParameters

/**
 * интерфейс, который должен реализовывать каждый воркер для
 * поддержки универсального создания воркеров с помощью фабрики
 */
interface ChildWorkerFactory {

    fun create(
        context: Context,
        workerParameters: WorkerParameters
    ): ListenableWorker
}