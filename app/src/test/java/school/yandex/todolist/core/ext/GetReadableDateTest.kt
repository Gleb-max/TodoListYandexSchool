package school.yandex.todolist.core.ext

import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.LocalDate
import java.time.ZoneId
import java.util.*

class GetReadableDateTest {

    @Test
    fun `should get correct human readable date`() {
        val date = Date.from(
            LocalDate.of(
                1970,
                1,
                1
            ).atStartOfDay(ZoneId.of("Europe/Moscow")).toInstant()
        )
        assertEquals(date.getReadableDate(), EXPECTED_DATE)
    }

    companion object {

        private const val EXPECTED_DATE = "1 января 1970"
    }
}