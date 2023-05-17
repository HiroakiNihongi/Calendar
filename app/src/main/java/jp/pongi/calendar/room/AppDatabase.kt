package jp.pongi.calendar.room

import androidx.room.Database
import androidx.room.RoomDatabase
import jp.pongi.calendar.room.dao.EventDao
import jp.pongi.calendar.room.entities.Event

@Database(entities = [Event::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun scheduleDao(): EventDao
}