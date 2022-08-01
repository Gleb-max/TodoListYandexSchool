package school.yandex.todolist.presentation.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import school.yandex.todolist.R
import school.yandex.todolist.databinding.ActivityMainBinding
import school.yandex.todolist.domain.entity.TodoItem
import school.yandex.todolist.presentation.todoitem.TodoItemFragment
import school.yandex.todolist.presentation.todolist.TodoListFragment

class MainActivity : AppCompatActivity(),
    TodoListFragment.OnTodoListActionsListener,
    TodoItemFragment.OnTodoItemEditingFinishedListener {

    private val binding: ActivityMainBinding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }

    private fun launchFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.main_container, fragment)
            .addToBackStack(null)
            .commit()
    }

    override fun onAddTodoItem() {
        launchFragment(TodoItemFragment.newInstanceAddItem())
    }

    override fun onEditTodoItem(todoItem: TodoItem) {
        launchFragment(TodoItemFragment.newInstanceEditItem(todoItem.id))
    }

    override fun onTodoItemEditingFinished() {
        supportFragmentManager.popBackStack()
    }
}