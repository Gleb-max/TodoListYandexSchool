package school.yandex.todolist.data.source.local

import android.app.Application
import android.content.Context
import com.google.gson.Gson
import school.yandex.todolist.domain.entity.User
import javax.inject.Inject

//todo save in room
class UserPreferences @Inject constructor(context: Application) {
    // todo: можно подумать, что тут может случиться утечка контекста,
    //  но из di тут всегда будет application, поэтому придумать как сделать чище это место
    private val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    private val gson = Gson()

    fun getUser(): User = try {
        gson.fromJson(prefs.getString(KEY_USER, ""), User::class.java)
    } catch (exc: Exception) {
        exc.printStackTrace()
        User.emptyUser()
    }

    fun saveUser(user: User) = prefs.edit().apply {
        putString(KEY_USER, gson.toJson(user))
    }.apply()

    companion object {

        private const val PREF_NAME = "user_prefs"
        private const val KEY_USER = "user"
    }
}