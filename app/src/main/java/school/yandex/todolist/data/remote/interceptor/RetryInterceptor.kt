package school.yandex.todolist.data.remote.interceptor

import okhttp3.Interceptor
import okhttp3.Response

class RetryInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        var response = chain.proceed(request)
        var retryCount = 0

        while (!response.isSuccessful && retryCount < RETRY_COUNT) {
            response.close()
            response = chain.proceed(request)
            retryCount++
        }
        return response
    }

    companion object {

        private const val RETRY_COUNT = 2
    }
}