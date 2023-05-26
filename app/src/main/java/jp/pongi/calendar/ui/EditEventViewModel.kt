package jp.pongi.calendar.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import jp.pongi.calendar.repository.EventRepository
import jp.pongi.calendar.room.entities.Event
import kotlinx.coroutines.launch

class EditEventViewModel : ViewModel() {

    private val repo = EventRepository()

    fun update(event: Event) {
        viewModelScope.launch {
            repo.insertOrUpdate(event)
        }
    }

    fun delete(event: Event) {
        viewModelScope.launch {
            repo.delete(event)
        }
    }
}