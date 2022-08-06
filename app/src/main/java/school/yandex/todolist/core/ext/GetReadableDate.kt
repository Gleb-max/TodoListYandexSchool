package school.yandex.todolist.core.ext

import java.text.SimpleDateFormat
import java.util.*

fun Date.getReadableDate(): String {
    val formatter = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
    return formatter.format(this)
}