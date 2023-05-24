package jp.pongi.calendar.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import jp.pongi.calendar.room.entities.Event
import java.time.Instant

@Dao
interface EventDao {
    @Insert
    fun insert(event: Event)

    @Update
    fun update(event: Event)

    @Delete
    fun delete(event: Event)

    @Query("SELECT * FROM events")
    fun getAll(): List<Event>

    @Query("SELECT * FROM events WHERE events.start BETWEEN :start AND :end ORDER BY start ASC")
    fun getEvent(start: Instant, end: Instant): List<Event>

    fun insertOrUpdate(event: Event) {
        if (event.id == 0) {
            insert(event)
        } else {
            update(event)
        }
    }
}