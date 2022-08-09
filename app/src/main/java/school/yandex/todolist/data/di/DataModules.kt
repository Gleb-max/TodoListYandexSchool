package school.yandex.todolist.data.di

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import school.yandex.todolist.data.local.RevisionPreferences
import school.yandex.todolist.data.local.UserPreferences
import school.yandex.todolist.data.remote.api.TodoApi
import school.yandex.todolist.data.remote.interceptor.AuthHeaderInterceptor
import school.yandex.todolist.data.remote.interceptor.LastRevisionInterceptor
import school.yandex.todolist.data.repository.TodoItemsRepositoryImpl
import school.yandex.todolist.data.repository.UsersRepositoryImpl
import school.yandex.todolist.domain.repository.TodoItemsRepository
import school.yandex.todolist.domain.repository.UserRepository
import java.util.concurrent.TimeUnit

val dataModules
    get() = listOf(
        repositoryModule,
        networkModule
    )

val repositoryModule = module {
    single<TodoItemsRepository> { TodoItemsRepositoryImpl(get(), get()) }
    single<UserRepository> { UsersRepositoryImpl(get()) }

    single { RevisionPreferences(androidApplication()) }
    single { UserPreferences(androidApplication()) }
}

//todo: получать base_url из бэка или из ресурсов/билд конфига
const val BASE_URL = "https://beta.mrdekk.ru/todobackend/"

private val networkModule = module {
    single { get<Retrofit>().create(TodoApi::class.java) }

    single {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(get()))
            .client(get())
            .build()
    }

    single {
        OkHttpClient.Builder().apply {
            //todo: настроить таймауты
            readTimeout(30L, TimeUnit.SECONDS)
            connectTimeout(60L, TimeUnit.SECONDS)
            writeTimeout(120L, TimeUnit.SECONDS)
            retryOnConnectionFailure(true)
            with(interceptors()) {
                addInterceptor(AuthHeaderInterceptor())
                addInterceptor(LastRevisionInterceptor(get()))
                addInterceptor(HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BASIC
                })
            }
        }.build()
    }

    single {
        GsonBuilder().apply {
            setLenient()
        }.create()
    }
}