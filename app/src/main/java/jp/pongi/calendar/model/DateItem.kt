package jp.pongi.calendar.model

import java.io.Serializable
import java.time.LocalDate

data class DateItem(
    val localDate: LocalDate,
    val today: Boolean = false,
    val isCurrent: Boolean = false,
) : Serializable {
    val dayOfMonth: String = localDate.dayOfMonth.toString()
}