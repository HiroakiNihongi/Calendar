package jp.pongi.calendar.ui.event

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import jp.pongi.calendar.R
import jp.pongi.calendar.model.DateItem
import jp.pongi.calendar.room.entities.Event
import jp.pongi.calendar.ui.EditEventViewModel
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

    private lateinit var dateItem: DateItem
    private var eventItem: Event? = null

    // Use the 'by activityViewModels()' Kotlin property delegate
    // from the fragment-ktx artifact
    private val editEventViewModel: EditEventViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        startDateEdit = view.findViewById(R.id.start_date)
        endDateEdit = view.findViewById(R.id.end_date)
        titleTextView = view.findViewById(R.id.title)
        memoTextView = view.findViewById(R.id.memo)

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
            val localDate = dateItem.localDate
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

        // 画面右上に Menu を表示させる
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu_edit_event, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                when(menuItem.itemId) {
                    R.id.menu_delete -> {
                        // イベントの削除
                        eventItem?.let { event ->
                            editEventViewModel.delete(event)
                            findNavController().popBackStack()
                        }
                    }
                }
                return true
            }

        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
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
        editEventViewModel.update(updateItem)
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
