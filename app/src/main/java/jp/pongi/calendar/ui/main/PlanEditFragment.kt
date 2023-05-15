package jp.pongi.calendar.ui.main

import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import jp.pongi.calendar.R
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class PlanEditFragment : Fragment(R.layout.fragment_plan_edit) {

    private val args: PlanEditFragmentArgs by navArgs()
    private val formatter = SimpleDateFormat("yyyy/MM/dd HH:mm", Locale.JAPAN)

    lateinit var startDateEdit: EditText
    lateinit var endDateEdit: EditText

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

        // 終了日
        val end = start.clone() as Calendar
        end.add(Calendar.HOUR_OF_DAY, 1)
        endDateEdit = view.findViewById(R.id.end_date)
        endDateEdit.setText(formatter.format(end.time))
    }
}