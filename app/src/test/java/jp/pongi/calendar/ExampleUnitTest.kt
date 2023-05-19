package jp.pongi.calendar

import org.junit.Test
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.OffsetTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.time.temporal.TemporalAdjusters

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {

    private var current = LocalDate.now()

    @Test
    fun addition_isCorrect() {
        val begin = current.with(TemporalAdjusters.firstDayOfMonth())
            .with(TemporalAdjusters.previous(DayOfWeek.SUNDAY))

        val end = current.with(TemporalAdjusters.lastDayOfMonth())
            .with(TemporalAdjusters.next(DayOfWeek.SATURDAY))

        val localDateFormatter = DateTimeFormatter
            .ofPattern("uuuu/MM/dd")
            .withZone(ZoneOffset.UTC)

        println(""+ localDateFormatter.format(begin))
        println(""+ localDateFormatter.format(end))


        val start = LocalDateTime.from(current.atTime(OffsetTime.now()))

        val localDateTimeFormatter = DateTimeFormatter
            .ofPattern("uuuu/MM/dd HH:mm")
            .withZone(ZoneOffset.UTC)

        println(""+ localDateTimeFormatter.format(start))

    }
}