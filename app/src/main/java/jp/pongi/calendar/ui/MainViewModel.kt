package jp.pongi.calendar.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import jp.pongi.calendar.model.DateItem
import jp.pongi.calendar.repository.EventRepository
import kotlinx.coroutines.Dispatchers
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.time.temporal.TemporalAdjusters

class MainViewModel : ViewModel() {

    private val repo = EventRepository()

    var current = MutableLiveData(LocalDate.now())

    private val _currentMonth = MutableLiveData(LocalDate.now())
    val currentMonth: LiveData<String> = _currentMonth.map { current ->
        DateTimeFormatter
            .ofPattern("yyyy年 MM月")
            .format(current)
    }

    val itemList = MediatorLiveData<List<DateItem>>().apply {
        addSource(_currentMonth) {
            this.value = (retrieveCalendar())
        }
        addSource(current) {
            this.value = retrieveCalendar()
        }
    }

    val eventList = current.switchMap { date ->
        liveData(viewModelScope.coroutineContext + Dispatchers.IO) {
            val start = LocalDateTime.of(date, LocalTime.MIN).toInstant(ZoneOffset.UTC)
            val end = LocalDateTime.of(date, LocalTime.MAX).toInstant(ZoneOffset.UTC)
            val result = runCatching { repo.getEvent(start, end) }.getOrElse { emptyList() }
            emit(result)
        }
    }

    private fun retrieveCalendar(): List<DateItem> {
        val items = mutableListOf<DateItem>()
        val beginOfMonth = _currentMonth.value?.with(TemporalAdjusters.firstDayOfMonth())
            ?.with(TemporalAdjusters.previous(DayOfWeek.SUNDAY)) ?: return emptyList()
        val endOfMonth = _currentMonth.value?.with(TemporalAdjusters.lastDayOfMonth())
            ?.with(TemporalAdjusters.next(DayOfWeek.SATURDAY)) ?: return emptyList()
        var tmp = beginOfMonth
        while (tmp <= endOfMonth) {
            items.add(DateItem(tmp, tmp == LocalDate.now(), tmp == current.value))
            tmp = tmp.plusDays(1)
        }
        return items
    }

    fun moveToNextMonth() {
        _currentMonth.postValue(_currentMonth.value?.plusMonths(1))
    }

    fun moveToTodayMonth() {
        _currentMonth.postValue(LocalDate.now())
    }

    fun moveToPrevMonth() {
        _currentMonth.postValue(_currentMonth.value?.minusMonths(1))
    }
}