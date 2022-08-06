package school.yandex.todolist.presentation.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import school.yandex.todolist.R
import school.yandex.todolist.databinding.ActivityMainBinding
import school.yandex.todolist.domain.entity.TodoItem
import school.yandex.todolist.presentation.todoitem.TodoItemFragment
import school.yandex.todolist.presentation.todolist.TodoListFragment
import school.yandex.todolist.presentation.todolist.TodoListFragmentDirections

class MainActivity : AppCompatActivity(),
    TodoListFragment.OnTodoListActionsListener,
    TodoItemFragment.OnTodoItemEditingFinishedListener {

    private val binding: ActivityMainBinding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val navHostFragment by lazy {
        supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main) as NavHostFragment
    }
    private val navController by lazy { navHostFragment.navController }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
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
}