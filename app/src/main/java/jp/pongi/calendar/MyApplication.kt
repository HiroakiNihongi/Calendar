package jp.pongi.calendar

import android.app.Application
import androidx.room.Room
import jp.pongi.calendar.room.AppDatabase

class MyApplication : Application() {

    companion object {
        lateinit var appDatabase: AppDatabase
    }

    override fun onCreate() {
        super.onCreate()
        appDatabase = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "database"
        ).build()
    }
}
