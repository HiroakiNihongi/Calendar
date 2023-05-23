package jp.pongi.calendar.model

import jp.pongi.calendar.room.entities.Event
import java.io.Serializable
import java.time.LocalDate

data class DateItem(
    val localDate: LocalDate,
    val today: Boolean = false,
    val event: Event? = null
) : Serializable {
    val dayOfMonth: String = localDate.dayOfMonth.toString()
}