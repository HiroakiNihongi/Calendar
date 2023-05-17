package jp.pongi.calendar.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import jp.pongi.calendar.room.entities.Event

@Dao
interface EventDao {
    @Insert
    fun insert(event: Event)

    @Delete
    fun delete(event: Event)

    @Query("SELECT * FROM events")
    fun getAll(): List<Event>

}