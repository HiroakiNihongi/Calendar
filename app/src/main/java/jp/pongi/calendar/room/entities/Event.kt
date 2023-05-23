package jp.pongi.calendar.room.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable
import java.time.Instant

@Entity(tableName = "events")
data class Event(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo(name = "start")
    val start: Instant,
    @ColumnInfo(name = "end")
    val end: Instant,
    @ColumnInfo(name = "title")
    val title: String,
    @ColumnInfo(name = "description")
    val description: String? = null,
    @ColumnInfo(name = "memo")
    val memo: String? = null,
    @ColumnInfo(name = "done")
    val done: Boolean = false
): Serializable