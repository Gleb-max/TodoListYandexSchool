package school.yandex.todolist.presentation.main

import android.content.Intent
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import com.auth0.android.jwt.JWT
import com.google.android.material.snackbar.Snackbar
import com.yandex.authsdk.YandexAuthLoginOptions
import com.yandex.authsdk.YandexAuthOptions
import com.yandex.authsdk.YandexAuthSdk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import school.yandex.todolist.R
import school.yandex.todolist.databinding.ActivityMainBinding
import school.yandex.todolist.domain.entity.TodoItem
import school.yandex.todolist.presentation.i.OnSnackBarShowListener
import school.yandex.todolist.presentation.todoitem.TodoItemFragment
import school.yandex.todolist.presentation.todolist.TodoListFragment
import school.yandex.todolist.presentation.todolist.TodoListFragmentDirections

class MainActivity : AppCompatActivity(),
    TodoListFragment.OnTodoListActionsListener,
    TodoItemFragment.OnTodoItemEditingFinishedListener,
    OnSnackBarShowListener {

    private val binding: ActivityMainBinding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val navHostFragment by lazy {
        supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main) as NavHostFragment
    }
    private val navController by lazy { navHostFragment.navController }

    private val yandexAuthSdk: YandexAuthSdk by lazy {
        YandexAuthSdk(
            this,
            YandexAuthOptions.Builder(this).build()
        )
    }
    private val yandexResultLauncher: ActivityResultLauncher<Intent> by lazy {
        this.registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            result.data?.let {
                getYandexAuthData(
                    result.resultCode,
                    it
                )
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val loginOptionsBuilder = YandexAuthLoginOptions.Builder()
        val yandexAuthIntent = yandexAuthSdk.createLoginIntent(loginOptionsBuilder.build())
        yandexResultLauncher.launch(yandexAuthIntent)
    }

    private fun getYandexAuthData(resultCode: Int, data: Intent) {
        val yandexAuthToken = yandexAuthSdk.extractToken(resultCode, data)
        if (yandexAuthToken == null) {
            //todo: обработать этот случай
            return
        }
        lifecycleScope.launch(Dispatchers.IO) {
            val jwt = JWT(yandexAuthSdk.getJwt(yandexAuthToken))
            val userId = jwt.getClaim("uid").asString()
            val userName = jwt.getClaim("name").asString()
            setupMainScreen()
            //todo добавить репо + юзкейс и сохранять данные юзера
        }
    }

    private fun setupMainScreen() {
        lifecycleScope.launch(Dispatchers.Main) {
            setContentView(binding.root)
        }
    }

    override fun onAddTodoItem() {
        navController.navigate(R.id.navigation_todo_item_details)
    }

    override fun onEditTodoItem(todoItem: TodoItem) {
        navController.navigate(
            TodoListFragmentDirections.actionNavigationTodoListToNavigationTodoItemDetails(
                todoItem.id
            )
        )
    }

    override fun onTodoItemEditingFinished() {
        navController.navigateUp()
    }

    override fun showSnackBarMessage(title: String, action: String?, actionCallBack: () -> Unit) {
        val snackBar = Snackbar.make(binding.root, title, Snackbar.LENGTH_LONG)
        if (action != null) {
            snackBar.setAction(action) {
                actionCallBack()
            }
        }
        snackBar.show()
    }
}