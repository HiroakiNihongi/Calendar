package jp.pongi.calendar.ui.event

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import jp.pongi.calendar.MyApplication
import jp.pongi.calendar.R
import jp.pongi.calendar.room.entities.Event
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

class EditEventFragment : Fragment(R.layout.fragment_edit_event) {

    private val args: EditEventFragmentArgs by navArgs()

    private lateinit var startDateEdit: EditText
    private lateinit var endDateEdit: EditText
    private lateinit var titleTextView: EditText
    private lateinit var memoTextView: EditText

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val h = LocalTime.now().hour
        val m = if (LocalTime.now().minute < 30) 0 else 30
        val localTime = LocalTime.of(h, m)
        val localDate = args.item.localDate

        // 開始日
        val start = LocalDateTime.of(localDate, localTime).plusMinutes(30)
        // 終了日
        val end = start.plusHours(1)

        startDateEdit = view.findViewById(R.id.start_date)
        startDateEdit.setText(start.instantToDisplay())
        titleTextView = view.findViewById(R.id.title)
        memoTextView = view.findViewById(R.id.memo)

        endDateEdit = view.findViewById(R.id.end_date)
        endDateEdit.setText(end.instantToDisplay())

        view.findViewById<Button>(R.id.btn_ok).setOnClickListener {
            apply()
        }
    }

    private fun apply() {
        val start = startDateEdit.text.toString()
        val end = endDateEdit.text.toString()
        val title = titleTextView.text.toString()
        val memo = memoTextView.text.toString()
        val dao = MyApplication.appDatabase.scheduleDao()

        lifecycleScope.launch(Dispatchers.IO) {
            dao.insert(
                Event(
                    start = start.displayToInstant(),
                    end = end.displayToInstant(),
                    title = title,
                    memo = memo
                )
            )
        }
    }

    private fun LocalDateTime.instantToDisplay(): String =
        displayFormatter.format(this)

    private fun String.displayToInstant(): Instant =
        displayFormatter.parse(this, Instant::from)

    private val displayFormatter = DateTimeFormatter
        .ofPattern("yyyy/MM/dd HH:mm")
        .withZone(ZoneOffset.UTC)

}
