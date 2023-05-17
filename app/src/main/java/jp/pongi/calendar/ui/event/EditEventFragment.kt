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
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class EditEventFragment : Fragment(R.layout.fragment_edit_event) {

    private val args: EditEventFragmentArgs by navArgs()
    private val formatter = SimpleDateFormat("yyyy/MM/dd HH:mm", Locale.JAPAN)

    private lateinit var startDateEdit: EditText
    private lateinit var endDateEdit: EditText
    private lateinit var titleTextView: EditText
    private lateinit var memoTextView: EditText

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // 開始日
        val start = args.item.calendar.clone() as Calendar
        start.apply {
            set(Calendar.HOUR_OF_DAY, Calendar.getInstance().get(Calendar.HOUR_OF_DAY) + 1)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        startDateEdit = view.findViewById(R.id.start_date)
        startDateEdit.setText(formatter.format(start.time))
        titleTextView = view.findViewById(R.id.title)
        memoTextView = view.findViewById(R.id.memo)

        // 終了日
        val end = start.clone() as Calendar
        end.add(Calendar.HOUR_OF_DAY, 1)
        endDateEdit = view.findViewById(R.id.end_date)
        endDateEdit.setText(formatter.format(end.time))

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
                    start = start,
                    end = end,
                    title = title,
                    memo = memo
                )
            )
        }
    }
}