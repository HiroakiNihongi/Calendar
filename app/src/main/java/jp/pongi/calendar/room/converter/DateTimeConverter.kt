package jp.pongi.calendar.room.converter

import androidx.room.TypeConverter
import java.time.DateTimeException
import java.time.Instant
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeFormatterBuilder
import java.time.format.SignStyle
import java.time.temporal.ChronoField

/**
 * Room で日時を扱うためのコンバーター。
 *
 * Room が使用する SQLite には日時型がない（[Datatypes In SQLite](https://www.sqlite.org/datatype3.html)）ため、
 * SQLite の `datetime` 関数（[Date And Time Functions](https://www.sqlite.org/lang_datefunc.html)）と同じ
 * `YYYY-MM-DD HH:MM:SS` 形式の文字列として扱う。
 *
 * 文字列での日時の精度が秒であることに注意。
 */
object DateTimeConverter {
    /**
     * [String] を [Instant] に変換する。
     */
    @TypeConverter
    fun stringToInstant(value: String): Instant =
        formatter.parse(value, Instant::from)

    /**
     * [Instant] を [String] に変換する。
     *
     * 秒未満の値は切り捨てられる。
     *
     * @throws DateTimeException 0 年未満や 10,000 年以上の場合。
     */
    @TypeConverter
    fun instantToString(value: Instant): String =
        formatter.format(value)

    /**
     * `YYYY-MM-DD HH:MM:SS` 形式の日時フォーマッター。
     *
     * 0 年未満や 10,000 年以上はエラーになる。
     */
    private val formatter: DateTimeFormatter =
    // DateTimeFormatter.ofPattern で生成したものだと
    // 0 年未満や 10,000 年以上でもエラーにならないため、
        // DateTimeFormatterBuilder で生成する。
        DateTimeFormatterBuilder()
            .appendValue(ChronoField.YEAR, 4, 4, SignStyle.NOT_NEGATIVE)
            // ^ 4 桁を超える場合や負の場合はエラーとする。
            .appendLiteral('-')
            .appendValue(ChronoField.MONTH_OF_YEAR, 2)
            .appendLiteral('-')
            .appendValue(ChronoField.DAY_OF_MONTH, 2)
            .appendLiteral(' ')
            .appendValue(ChronoField.HOUR_OF_DAY, 2)
            .appendLiteral(':')
            .appendValue(ChronoField.MINUTE_OF_HOUR, 2)
            .appendLiteral(':')
            .appendValue(ChronoField.SECOND_OF_MINUTE, 2)
            .toFormatter()
            .withZone(ZoneOffset.UTC) // タイムゾーンを UTC にする。
}
