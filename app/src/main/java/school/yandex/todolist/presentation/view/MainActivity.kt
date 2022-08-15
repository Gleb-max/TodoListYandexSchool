package school.yandex.todolist.presentation.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
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
import school.yandex.todolist.TodoApp
import school.yandex.todolist.core.ext.toast
import school.yandex.todolist.databinding.ActivityMainBinding
import school.yandex.todolist.domain.entity.TodoItem
import school.yandex.todolist.domain.entity.User
import school.yandex.todolist.presentation.i.OnSnackBarShowListener
import school.yandex.todolist.presentation.stateholder.MainViewModel
import school.yandex.todolist.presentation.stateholder.ViewModelFactory
import javax.inject.Inject

class MainActivity : AppCompatActivity(),
    TodoListFragment.OnTodoListActionsListener,
    TodoItemFragment.OnTodoItemEditingFinishedListener,
    AuthFragment.OnAuthActionsListener,
    OnSnackBarShowListener {

    private val binding: ActivityMainBinding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private lateinit var viewModel: MainViewModel

    private val component by lazy {
        (application as TodoApp).component
    }

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
    private val yandexResultLauncher: ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            result.data?.let {
                lifecycleScope.launch(Dispatchers.IO) {
                    runCatching {
                        val user = getYandexUser(result.resultCode, it)
                        if (user == null) {
                            lifecycleScope.launch(Dispatchers.Main) {
                                toast(getString(R.string.error_yandex_auth))
                            }
                        } else {
                            viewModel.saveUser(user)
                            //todo выпилю это, когда перенесу сохранение юзера в рум
                            // (там будет автоматически обновляться user во viewmodel)
                            lifecycleScope.launch(Dispatchers.Main) {
                                openMainScreen()
                            }
                        }
                    }.onFailure {
                        it.printStackTrace()
                        lifecycleScope.launch(Dispatchers.Main) {
                            toast(getString(R.string.error_yandex_auth))
                        }
                    }
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        component.inject(this)

        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this, viewModelFactory)[MainViewModel::class.java]

        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.user.observe(this) {
            if (it != null) {
                if (it.uid == null) openAuthScreen()
                else openMainScreen()
            }
        }
    }

    private fun getYandexUser(resultCode: Int, data: Intent): User? {
        val yandexAuthToken = yandexAuthSdk.extractToken(resultCode, data) ?: return null
        val jwt = JWT(yandexAuthSdk.getJwt(yandexAuthToken))
        val userId = jwt.getClaim("uid").asString()
        val userName = jwt.getClaim("name").asString()
        return User(userId, userName.toString())
    }

    private fun openAuthScreen() {
        navController.navigate(R.id.action_navigation_start_to_navigation_auth)
    }

    private fun openMainScreen() {
        navController.navigate(R.id.action_navigation_todo_list)
    }

    override fun onLogin() {
        val loginOptionsBuilder = YandexAuthLoginOptions.Builder()
        val yandexAuthIntent = yandexAuthSdk.createLoginIntent(loginOptionsBuilder.build())
        yandexResultLauncher.launch(yandexAuthIntent)
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