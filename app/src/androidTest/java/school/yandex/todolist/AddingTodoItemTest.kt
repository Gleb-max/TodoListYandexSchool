package school.yandex.todolist

import androidx.test.espresso.action.ViewActions
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import school.yandex.todolist.presentation.view.MainActivity

@RunWith(AndroidJUnit4::class)
class AddingTodoItemTest {

    @JvmField
    @Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun shouldAddTodoItemInRecycler() {
        TodoListInteractions.onAddButton().perform(ViewActions.click())
        TodoItemInteractions.onTitleEditText().perform(ViewActions.typeText(TODO_ITEM_TITLE))
        TodoItemInteractions.onSaveButton().perform(ViewActions.click())
        TodoListInteractions.onTodoListItem(TODO_ITEM_TITLE).perform(ViewActions.click())
    }

    companion object {

        private const val TODO_ITEM_TITLE = "gtrjgrtkjgmkrjtgmktr"
    }
}