package jp.pongi.calendar.model

import java.io.Serializable
import java.util.Calendar

data class DateItem(
    val calendar: Calendar,
    val today: Boolean = false
) : Serializable {
    val dayOfMonth: String = calendar.get(Calendar.DAY_OF_MONTH).toString()
    val dayOfWeek: String = calendar.get(Calendar.DAY_OF_WEEK).toString()
}