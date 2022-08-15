package school.yandex.todolist.data.worker

import android.content.Context
import android.util.Log
import androidx.work.*
import school.yandex.todolist.domain.usecase.GetTodoListUseCase
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class TodoSyncWorker(
    context: Context,
    workerParameters: WorkerParameters,
    private val getTodoListUseCase: GetTodoListUseCase
) : CoroutineWorker(context, workerParameters) {

    override suspend fun doWork(): Result {
        val todoItems = getTodoListUseCase()
        //todo: добавить сохранение в рум
        Log.d("todo list", todoItems.toString())
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

    class Factory @Inject constructor(
        private val getTodoListUseCase: GetTodoListUseCase
    ) : ChildWorkerFactory {

        override fun create(
            context: Context,
            workerParameters: WorkerParameters
        ): ListenableWorker {
            return TodoSyncWorker(
                context,
                workerParameters,
                getTodoListUseCase
            )
        }
    }
}