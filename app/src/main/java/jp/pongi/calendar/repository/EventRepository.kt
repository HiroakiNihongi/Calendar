package jp.pongi.calendar.repository

import jp.pongi.calendar.MyApplication
import jp.pongi.calendar.room.entities.Event
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.Instant

class EventRepository(private val dispatchers: CoroutineDispatcher = Dispatchers.IO) {

    private val dao = MyApplication.appDatabase.eventDao()

    suspend fun delete(event: Event) {
        withContext(dispatchers) {
            dao.delete(event)
        }
    }

    suspend fun getAll(): List<Event> {
        return withContext(dispatchers) {
            dao.getAll()
        }
    }

    suspend fun getEvent(start: Instant, end: Instant): List<Event> {
        return withContext(dispatchers) {
            dao.getEvent(start, end)
        }
    }

    suspend fun insertOrUpdate(event: Event) {
        withContext(dispatchers) {
            if (event.id == 0) {
                dao.insert(event)
            } else {
                dao.update(event)
            }
        }
    }
}