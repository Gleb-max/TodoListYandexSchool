package school.yandex.todolist.data.source.remote.interceptor

import okhttp3.Interceptor
import okhttp3.Response
import school.yandex.todolist.data.source.local.RevisionPreferences
import javax.inject.Inject

/**
 * подстановка значения ревизии в запросы
 */
class LastRevisionInterceptor @Inject constructor(
    private val revisionPreferences: RevisionPreferences
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        val revision = revisionPreferences.getRevision()
        if (revision != 0) {
            request = request.newBuilder()
                .addHeader("X-Last-Known-Revision", revision.toString())
                .build()
        }
        return chain.proceed(request)
    }
}