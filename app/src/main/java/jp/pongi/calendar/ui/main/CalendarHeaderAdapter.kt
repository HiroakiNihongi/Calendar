package jp.pongi.calendar.ui.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import jp.pongi.calendar.R
import jp.pongi.calendar.databinding.ItemDateHeaderBinding
import jp.pongi.calendar.ui.main.CalendarHeaderAdapter.HeaderViewHolder

class CalendarHeaderAdapter : RecyclerView.Adapter<HeaderViewHolder>() {

    inner class HeaderViewHolder(private val binding: ItemDateHeaderBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(dayOfWeek: String) {
            binding.dayOfWeek = dayOfWeek
        }
    }

    private val labelDayOfWeek = listOf(
        R.string.sunday,
        R.string.monday,
        R.string.tuesday,
        R.string.wednesday,
        R.string.thursday,
        R.string.friday,
        R.string.saturday,
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HeaderViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemDateHeaderBinding.inflate(inflater, parent, false)
        return HeaderViewHolder(binding)
    }

    override fun getItemCount(): Int = 7

    override fun onBindViewHolder(holder: HeaderViewHolder, position: Int) {
        val context = holder.itemView.context
        holder.bind(context.getString(labelDayOfWeek[position]))
    }
}