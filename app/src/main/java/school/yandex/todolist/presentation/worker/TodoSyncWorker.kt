package school.yandex.todolist.presentation.worker

import android.content.Context
import androidx.work.*
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import school.yandex.todolist.domain.usecase.GetTodoListUseCase
import java.util.concurrent.TimeUnit

class TodoSyncWorker(
    context: Context,
    workerParameters: WorkerParameters
) : CoroutineWorker(context, workerParameters), KoinComponent {

    private val getTodoListUseCase: GetTodoListUseCase by inject()

    override suspend fun doWork(): Result {
        val todoItems = getTodoListUseCase()
        //todo: добавить сохранение в рум
        return Result.success()
    }

    companion object {

        const val WORK_NAME = "SYNC_TODO_LIST"

        fun makeRequest(): PeriodicWorkRequest {
            return PeriodicWorkRequestBuilder<TodoSyncWorker>(8L, TimeUnit.HOURS)
                .setConstraints(makeConstraints())
                .build()

            //todo добавить задержку при запуске, чтобы при входе в прилу не было 2 запроса
            // или наоборот не добавлять и при входе по ревизии или
            // хедерам чекать актуальны данные в руме или нет
        }

        private fun makeConstraints() = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.UNMETERED) // лучше использовать вместо NetworkType.CONNECTED
            .build()
    }
}