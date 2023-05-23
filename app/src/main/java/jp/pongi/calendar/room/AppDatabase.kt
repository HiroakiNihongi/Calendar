package jp.pongi.calendar.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import jp.pongi.calendar.room.converter.DateTimeConverter
import jp.pongi.calendar.room.dao.EventDao
import jp.pongi.calendar.room.entities.Event

@Database(entities = [Event::class], version = 1)
@TypeConverters(DateTimeConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun eventDao(): EventDao
}