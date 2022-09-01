package school.yandex.todolist

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText

object TodoListInteractions {

    fun onAddButton(): ViewInteraction = onView(
        withId(R.id.fab)
    )

    fun onTodoListItem(title: String): ViewInteraction = onView(
        withText(title)
    )
}