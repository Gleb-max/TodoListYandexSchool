package school.yandex.todolist.di

import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import school.yandex.todolist.data.worker.ChildWorkerFactory
import school.yandex.todolist.data.worker.TodoSyncWorker

@Module
interface WorkerModule {

    @Binds
    @IntoMap
    @WorkerKey(TodoSyncWorker::class)
    fun bindTodoSyncWorkerFactory(worker: TodoSyncWorker.Factory): ChildWorkerFactory
}