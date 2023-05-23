package jp.pongi.calendar.ui.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import jp.pongi.calendar.databinding.ItemDateBinding
import jp.pongi.calendar.model.DateItem

class CalendarAdapter : ListAdapter<DateItem, CalendarAdapter.ItemViewHolder>(ItemDiffCallback) {

    lateinit var onItemClick: (item: DateItem) -> Unit
    lateinit var onItemLongClick: (item: DateItem) -> Boolean

    inner class ItemViewHolder(private val binding: ItemDateBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: DateItem) {
            binding.onItemClick = onItemClick
            binding.onItemLongClick = onItemLongClick
            binding.item = item
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemDateBinding.inflate(inflater, parent, false)
        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    object ItemDiffCallback : DiffUtil.ItemCallback<DateItem>() {
        override fun areItemsTheSame(oldItem: DateItem, newItem: DateItem): Boolean {
            return oldItem.localDate.toEpochDay() == newItem.localDate.toEpochDay()
        }

        override fun areContentsTheSame(oldItem: DateItem, newItem: DateItem): Boolean {
            return oldItem == newItem
        }
    }
}