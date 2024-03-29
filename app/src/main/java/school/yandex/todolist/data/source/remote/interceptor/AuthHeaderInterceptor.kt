package school.yandex.todolist.data.source.remote.interceptor

import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

/**
 * авторизация запросов в хэдере
 */
class AuthHeaderInterceptor @Inject constructor() : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        // todo: добавить получение токена (локальное хранилище, запрос на авторизацию и тд)
        val token = "AilreElacyne"
        request = request.newBuilder()
            .addHeader("Authorization", "Bearer $token")
            .build()
        return chain.proceed(request)
    }
}