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

    var selectedItemPos = -1

    inner class ItemViewHolder(private val binding: ItemDateBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: DateItem, position: Int) {
            binding.onItemClick = {
                onItemClick.invoke(it)
                val lastItemSelectedPos = selectedItemPos
                selectedItemPos = adapterPosition
                if (lastItemSelectedPos != -1) {
                    notifyItemChanged(lastItemSelectedPos)
                }
                notifyItemChanged(selectedItemPos)
            }
            binding.onItemLongClick = onItemLongClick
            binding.item = item
            if (selectedItemPos == -1) {
                binding.container.isSelected = item.today
                if (item.today) {
                    selectedItemPos = position
                }
            } else {
                binding.container.isSelected = (position == selectedItemPos)
            }
        }
    }

    init {
        setHasStableIds(true)
    }

    override fun getItemId(position: Int): Long {
        return getItem(position).localDate.toEpochDay()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemDateBinding.inflate(inflater, parent, false)
        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(getItem(position), position)
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