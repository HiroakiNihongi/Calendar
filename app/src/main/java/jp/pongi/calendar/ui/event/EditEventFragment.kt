package jp.pongi.calendar.ui.event

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import jp.pongi.calendar.R
import jp.pongi.calendar.model.DateItem
import jp.pongi.calendar.room.entities.Event
import jp.pongi.calendar.ui.MainViewModel
import java.time.Instant
import java.time.LocalDate
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

    private lateinit var dateItem: DateItem
    private var eventItem: Event? = null

    // Use the 'by activityViewModels()' Kotlin property delegate
    // from the fragment-ktx artifact
    private val mainViewModel: MainViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        startDateEdit = view.findViewById(R.id.start_date)
        endDateEdit = view.findViewById(R.id.end_date)
        titleTextView = view.findViewById(R.id.title)
        memoTextView = view.findViewById(R.id.memo)

        Log.d("DBG", "current = ${DateTimeFormatter
            .ofPattern("yyyy年 MM月 dd日")
            .format(mainViewModel.current.value)}")
        dateItem = args.dateItem
        eventItem = args.event
        eventItem?.let { event ->
            startDateEdit.setText(event.start.instantToDisplay())
            endDateEdit.setText(event.end.instantToDisplay())
            titleTextView.setText(event.title)
            memoTextView.setText(event.memo)
        } ?: run {
            val h = LocalTime.now().hour
            val m = if (LocalTime.now().minute < 30) 0 else 30
            val localTime = LocalTime.of(h, m)
            val localDate = LocalDate.now()
            // 開始日
            val start = LocalDateTime.of(localDate, localTime).plusMinutes(30)
            // 終了日
            val end = start.plusHours(1)

            startDateEdit.setText(start.instantToDisplay())
            endDateEdit.setText(end.instantToDisplay())
        }
        view.findViewById<Button>(R.id.btn_ok).setOnClickListener {
            apply()
        }
    }

    private fun apply() {
        val start = startDateEdit.text.toString().displayToInstant()
        val end = endDateEdit.text.toString().displayToInstant()
        val title = titleTextView.text.toString()
        val memo = memoTextView.text.toString()

        val updateItem = eventItem?.copy(
            start = start,
            end = end,
            title = title,
            memo = memo
        ) ?: Event(
            start = start,
            end = end,
            title = title,
            memo = memo
        )
        mainViewModel.update(updateItem)
        findNavController().popBackStack()
    }

    private fun LocalDateTime.instantToDisplay(): String =
        this.toInstant(ZoneOffset.UTC).instantToDisplay()

    private fun Instant.instantToDisplay(): String =
        displayFormatter.format(this)

    private fun String.displayToInstant(): Instant =
        displayFormatter.parse(this, Instant::from)

    private val displayFormatter = DateTimeFormatter
        .ofPattern("yyyy/MM/dd HH:mm")
        .withZone(ZoneOffset.UTC)

}
