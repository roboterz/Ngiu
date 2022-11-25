package com.example.ngiu.ui.calendar

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.ngiu.databinding.FragmentCalendarBinding
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import com.example.ngiu.MainActivity
import com.example.ngiu.R
import kotlinx.android.synthetic.main.fragment_calendar.*
import kotlinx.android.synthetic.main.popup_title.view.*
import java.time.LocalDate
import java.time.format.DateTimeFormatter


class CalendarFragment : Fragment() {

    private var recordId: Long = 0

    private lateinit var calendarViewModel: CalendarViewModel
    private var _binding: FragmentCalendarBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private var cAdapter: CalendarAdapter? = null


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
        toolbar_calendar.setOnMenuItemClickListener{
            when (it.itemId) {
                R.id.action_add -> {
                    // switch to Event fragment
                    //navigateToEventFragment(0L)
                    showReminderDialog(view.context)
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

                cAdapter?.setList(calendarViewModel.calendarDetail)
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
                cAdapter = this.context?.let {
                    CalendarAdapter(object: CalendarAdapter.OnClickListener {
                        // catch the item click event from adapter
                        override fun onItemClick(ID: Long, type: Int) {
                            // Open/switch to account detail

                            when (type){

                                // show reminder dialog
                                3 -> showReminderDialog(requireContext(),ID)
                            }


                            // refresh
                            //refreshCalendar()
                        }
                    })
                }

                recyclerView_calendar.adapter = cAdapter
            }
        }.start()
    }


    // refresh Calendar
    private fun refreshCalendar(){

        calendarViewModel.loadDataToRam(requireContext())
        cAdapter?.setList(calendarViewModel.calendarDetail)
    }

    // switch to page
    private fun navigateToRecordFragment(transID: Long = 0){
        val bundle = Bundle().apply {
            putLong("Transaction_ID", transID)
        }
        // switch to record fragment
        findNavController().navigate(R.id.navigation_record, bundle)
    }



    private fun showReminderDialog(context: Context, event_ID: Long = 0L){

        val dialog = MaterialDialog(context)
            .noAutoDismiss()
            .customView(R.layout.popup_reminder_dialog)


        // button text
        if (event_ID == 0L){
            // Add Mode
            dialog.findViewById<TextView>(R.id.button_left).text = getText(R.string.msg_button_cancel)
            // date
            dialog.findViewById<TextView>(R.id.reminder_date).text = LocalDate.now().format(
                DateTimeFormatter.ofPattern("MM/dd/yyyy"))
        } else {
            // Edit Mode
            dialog.findViewById<TextView>(R.id.button_left).text = getText(R.string.msg_button_delete)
            // load event from database
            val event = calendarViewModel.getEventRecord(context, event_ID)
            // memo
            dialog.findViewById<EditText>(R.id.reminder_memo).setText(event.Event_Memo)
            // date
            dialog.findViewById<TextView>(R.id.reminder_date).text = event.Event_Date.format(
                DateTimeFormatter.ofPattern("MM/dd/yyyy"))
        }
        dialog.findViewById<TextView>(R.id.button_right).text = getText(R.string.menu_save)


        // todo button Event
        dialog.findViewById<TextView>(R.id.button_left).setOnClickListener(){
            if (event_ID == 0L){
                // cancel
                dialog.dismiss()
            }else{
                // delete
            }
        }
        dialog.findViewById<TextView>(R.id.button_right).setOnClickListener(){
            // save
        }

        dialog.show()
    }


}

