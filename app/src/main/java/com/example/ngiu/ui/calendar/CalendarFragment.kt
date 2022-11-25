package com.example.ngiu.ui.calendar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation.findNavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment.findNavController
import androidx.navigation.fragment.findNavController
import com.example.ngiu.databinding.FragmentCalendarBinding
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ngiu.MainActivity
import com.example.ngiu.R
import kotlinx.android.synthetic.main.fragment_activity.*
import kotlinx.android.synthetic.main.fragment_calendar.*


class CalendarFragment : Fragment() {

    private var recordId: Long = 0

    private lateinit var calendarViewModel: CalendarViewModel
    private var _binding: FragmentCalendarBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private var CAdapter: CalendarAdapter? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        calendarViewModel =
            ViewModelProvider(this).get(CalendarViewModel::class.java)

        _binding = FragmentCalendarBinding.inflate(inflater, container, false)

        //
        calendarViewModel.loadDataToRam(requireContext())

        initAdapter()

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // toolbar
        // show add button
        toolbar_calendar.menu.findItem(R.id.action_add).isVisible = true


        // toolbar menu item clicked
        toolbar_activity.setOnMenuItemClickListener{
            when (it.itemId) {
                R.id.action_add -> {
                    // switch to Event fragment
                    navigateToEventFragment()
                    true
                }
                else -> super.onOptionsItemSelected(it)
            }
        }


    }


    override fun onResume() {
        super.onResume()

        // Show bottom bar
        (activity as MainActivity).setNavBottomBarVisibility(View.VISIBLE)

        // load transaction list
        Thread {
            activity?.runOnUiThread {
                //vpAdapter?.stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY

                CAdapter?.setList(calendarViewModel.calendarDetail)
                //recyclerView_activity.adapter = vpAdapter
            }
        }.start()

    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    // init Adapter
    private fun initAdapter() {

        Thread {
            this.activity?.runOnUiThread {

                recyclerView_calendar.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL,false)
                CAdapter = this.context?.let {
                    CalendarAdapter(object: CalendarAdapter.OnClickListener {
                        // catch the item click event from adapter
                        override fun onItemClick(ID: Long, type: Int) {
                            // Open/switch to account detail



                            // refresh
                            //refreshCalendar()
                        }
                    })
                }

                recyclerView_calendar.adapter = CAdapter
            }
        }.start()
    }


    // refresh Calendar
    private fun refreshCalendar(){

        calendarViewModel.loadDataToRam(requireContext())
        CAdapter?.setList(calendarViewModel.calendarDetail)
    }

    // switch to page
    private fun navigateToRecordFragment(transID: Long = 0){
        val bundle = Bundle().apply {
            putLong("Transaction_ID", transID)
        }
        // switch to record fragment
        findNavController().navigate(R.id.navigation_record, bundle)
    }


    private fun navigateToEventFragment(eventID: Long = 0) {
        TODO("Event Fragment")
        val bundle = Bundle().apply {
            putLong("Event_ID", eventID)
        }
        // switch to event fragment
        //findNavController().navigate(R.id.navigation_record, bundle)
    }
}

