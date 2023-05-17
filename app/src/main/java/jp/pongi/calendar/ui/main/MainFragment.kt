package jp.pongi.calendar.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import jp.pongi.calendar.databinding.FragmentMainBinding

class MainFragment : Fragment() {

    private var _binding: FragmentMainBinding? = null
    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    // Use the 'by activityViewModels()' Kotlin property delegate
    // from the fragment-ktx artifact
    private val mainViewModel: MainViewModel by activityViewModels()

    private lateinit var calendarAdapter: CalendarAdapter
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
                        val action = MainFragmentDirections.actionMainToEditEvent(item)
                        findNavController().navigate(action)
                }
                adapter = calendarAdapter
            }
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainViewModel.itemList.observe(viewLifecycleOwner) {
            calendarAdapter.submitList(it)
        }
    }
}