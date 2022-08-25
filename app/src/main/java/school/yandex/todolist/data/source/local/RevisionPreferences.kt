package school.yandex.todolist.data.source.local

import android.app.Application
import android.content.Context
import javax.inject.Inject


//todo save in room
class RevisionPreferences @Inject constructor(context: Application) {
    // todo: можно подумать, что тут может случиться утечка контекста,
    //  но из di тут всегда будет application, поэтому придумать как сделать чище это место
    private val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    fun setRevision(revision: Int) {
        prefs.edit().apply {
            putInt(KEY_REVISION, revision)
        }.apply()
    }

    fun getRevision() = prefs.getInt(KEY_REVISION, 0)

    companion object {

        private const val PREF_NAME = "revision_prefs"
        private const val KEY_REVISION = "revision"
    }
}