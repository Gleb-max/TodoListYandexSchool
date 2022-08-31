package school.yandex.todolist

import android.app.Application
import androidx.work.Configuration
import com.yandex.metrica.YandexMetrica
import com.yandex.metrica.YandexMetricaConfig
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

        val config = YandexMetricaConfig.newConfigBuilder(BuildConfig.YANDEX_APP_METRICA).build()
        YandexMetrica.activate(applicationContext, config)
        YandexMetrica.enableActivityAutoTracking(this)

        // todo: вынести логику отправки ивентов в спомогательный класс
        YandexMetrica.reportEvent("app_open")
    }

    override fun getWorkManagerConfiguration(): Configuration {
        return Configuration.Builder().setWorkerFactory(todoSyncWorkerFactory).build()
    }
}