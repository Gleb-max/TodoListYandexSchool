package school.yandex.todolist

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.matcher.ViewMatchers.withId

object TodoItemInteractions {

    fun onTitleEditText(): ViewInteraction = onView(
        withId(R.id.et_content)
    )

    fun onSaveButton(): ViewInteraction = onView(
        withId(R.id.btn_save)
    )
}