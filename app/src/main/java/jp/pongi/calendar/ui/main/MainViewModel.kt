package jp.pongi.calendar.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import jp.pongi.calendar.MyApplication
import jp.pongi.calendar.model.DateItem
import kotlinx.coroutines.Dispatchers
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.time.temporal.TemporalAdjusters

class MainViewModel : ViewModel() {

    var current = MutableLiveData<LocalDate>()

    val currentMonth: LiveData<String> = current.map { current ->
        DateTimeFormatter
            .ofPattern("yyyy年 MM月")
            .format(current)
    }

    val itemList: LiveData<List<DateItem>> = current.map { current ->
        retrieveCalendar(current)
    }

    val eventList = current.switchMap { date ->
        liveData(viewModelScope.coroutineContext + Dispatchers.IO) {
            val start = LocalDateTime.of(date, LocalTime.MIN).toInstant(ZoneOffset.UTC)
            val end = LocalDateTime.of(date, LocalTime.MAX).toInstant(ZoneOffset.UTC)
            val dao = MyApplication.appDatabase.eventDao()
            val result = runCatching { dao.getEvent(start, end) }.getOrElse { emptyList() }
            emit(result)
        }
    }

    fun setCurrent(date: LocalDate) {
        current.value = date
    }

    private fun getCalendar(): List<DateItem> =
        current.value?.let {
            retrieveCalendar(it)
        } ?: emptyList()

    private fun retrieveCalendar(current: LocalDate): List<DateItem> {
        val items = mutableListOf<DateItem>()
        val beginOfMonth = current.with(TemporalAdjusters.firstDayOfMonth())
            .with(TemporalAdjusters.previous(DayOfWeek.SUNDAY))

        val endOfMonth = current.with(TemporalAdjusters.lastDayOfMonth())
            .with(TemporalAdjusters.next(DayOfWeek.SATURDAY))

        var tmp = beginOfMonth
        while (tmp <= endOfMonth) {
            items.add(DateItem(tmp, tmp == LocalDate.now()))
            tmp = tmp.plusDays(1)
        }
        return items
    }

    fun moveToNextMonth() {
        current.postValue(current.value?.plusMonths(1))
        getCalendar()
    }

    fun moveToTodayMonth() {
        current.postValue(LocalDate.now())
        getCalendar()
    }

    fun moveToPrevMonth() {
        current.postValue(current.value?.minusMonths(1))
        getCalendar()
    }
}