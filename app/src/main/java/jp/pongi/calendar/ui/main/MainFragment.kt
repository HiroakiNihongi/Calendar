package jp.pongi.calendar.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import jp.pongi.calendar.databinding.FragmentMainBinding
import jp.pongi.calendar.model.DateItem
import jp.pongi.calendar.ui.MainViewModel
import java.time.LocalDate

class MainFragment : Fragment() {

    private var _binding: FragmentMainBinding? = null
    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    // Use the 'by activityViewModels()' Kotlin property delegate
    // from the fragment-ktx artifact
    private val mainViewModel: MainViewModel by activityViewModels()

    private lateinit var calendarAdapter: CalendarAdapter
    private lateinit var eventListAdapter: EventListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = this@MainFragment.viewLifecycleOwner
            viewModel = mainViewModel
            calendarLayout.monthlyTable.apply {
                calendarAdapter = CalendarAdapter()
                calendarAdapter.onItemClick = { item ->
                    mainViewModel.setCurrent(item)
                }
                calendarAdapter.onItemLongClick = { item ->
                    mainViewModel.setCurrent(item)
                    mainViewModel.selectedItemPos = calendarAdapter.selectedItemPos
                    val action = MainFragmentDirections.actionMainToEditEvent(item)
                    findNavController().navigate(action)
                    true
                }
                adapter = calendarAdapter
            }
            eventsTable.apply {
                eventListAdapter = EventListAdapter()
                adapter = eventListAdapter
                eventListAdapter.onItemClick = { event ->
                    mainViewModel.setEvent(event)
                    mainViewModel.selectedItemPos = calendarAdapter.selectedItemPos
                    val current = mainViewModel.current.value ?: LocalDate.now()
                    val isToday = current == LocalDate.now()
                    val item = DateItem(current, isToday)
                    val action = MainFragmentDirections.actionMainToEditEvent(item, event)
                    findNavController().navigate(action)
                }

            }
        }
        mainViewModel.setCurrent()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainViewModel.itemList.observe(viewLifecycleOwner) {
            calendarAdapter.submitList(it)
        }
        mainViewModel.eventList.observe(viewLifecycleOwner) {
            eventListAdapter.submitList(it)
        }
    }
}