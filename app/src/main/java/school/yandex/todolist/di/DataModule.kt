package school.yandex.todolist.di

import android.app.Application
import com.google.gson.GsonBuilder
import dagger.Binds
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import school.yandex.todolist.data.repository.TodoItemsRepositoryImpl
import school.yandex.todolist.data.repository.UsersRepositoryImpl
import school.yandex.todolist.data.source.local.db.AppDatabase
import school.yandex.todolist.data.source.remote.api.TodoApi
import school.yandex.todolist.data.source.remote.interceptor.AuthHeaderInterceptor
import school.yandex.todolist.data.source.remote.interceptor.LastRevisionInterceptor
import school.yandex.todolist.data.source.remote.interceptor.RetryInterceptor
import school.yandex.todolist.domain.repository.TodoItemsRepository
import school.yandex.todolist.domain.repository.UserRepository
import java.util.concurrent.TimeUnit

//todo: получать base_url из бэка / из ресурсов / билд конфига / еще откуда-то
const val BASE_URL = "https://beta.mrdekk.ru/todobackend/"

@Module
interface DataModule {

    @Binds
    @ApplicationScope
    fun bindTodoItemsRepository(impl: TodoItemsRepositoryImpl): TodoItemsRepository

    @Binds
    @ApplicationScope
    fun bindUsersRepository(impl: UsersRepositoryImpl): UserRepository

    companion object {

        @Provides
        @ApplicationScope
        fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
            return HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }
        }

        @Provides
        @ApplicationScope
        fun provideGsonConverterFactory(): GsonConverterFactory {
            return GsonConverterFactory.create(GsonBuilder().apply {
                setLenient()
            }.create())
        }

        @Provides
        @ApplicationScope
        fun provideOkHttpClient(
            authHeaderInterceptor: AuthHeaderInterceptor,
            lastRevisionInterceptor: LastRevisionInterceptor,
            retryInterceptor: RetryInterceptor,
            httpLoggingInterceptor: HttpLoggingInterceptor
        ): OkHttpClient {
            return OkHttpClient.Builder().apply {
                readTimeout(30L, TimeUnit.SECONDS)
                connectTimeout(60L, TimeUnit.SECONDS)
                writeTimeout(120L, TimeUnit.SECONDS)
                retryOnConnectionFailure(true)
                with(interceptors()) {
                    addInterceptor(authHeaderInterceptor)
                    addInterceptor(lastRevisionInterceptor)
                    addInterceptor(retryInterceptor)
                    addInterceptor(httpLoggingInterceptor)
                }
            }.build()
        }

        @Provides
        @ApplicationScope
        fun provideRetrofit(
            client: OkHttpClient,
            gsonConverterFactory: GsonConverterFactory
        ): Retrofit {
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(gsonConverterFactory)
                .client(client)
                .build()
        }

        @Provides
        @ApplicationScope
        fun provideTodoApi(retrofit: Retrofit): TodoApi {
            return retrofit.create(TodoApi::class.java)
        }

        @Provides
        @ApplicationScope
        fun provideAppDataBase(application: Application): AppDatabase {
            return AppDatabase.getInstance(application)
        }
    }
}