package com.example.ngiu.ui.calendar

import android.annotation.SuppressLint
import android.app.AlertDialog
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
import com.example.ngiu.data.entities.Event
import com.example.ngiu.data.entities.Trans
import com.example.ngiu.functions.DateTimePicker
import com.example.ngiu.functions.getInternationalDateFromAmericanDate
import kotlinx.android.synthetic.main.fragment_calendar.*
import kotlinx.android.synthetic.main.fragment_record.*
import kotlinx.android.synthetic.main.popup_reminder_dialog.*
import kotlinx.android.synthetic.main.popup_title.view.*
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
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
                else -> true
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
        recyclerView_calendar.adapter = cAdapter
    }


    @SuppressLint("CutPasteId")
    private fun showReminderDialog(context: Context, event_ID: Long = 0L){

        val dialog = MaterialDialog(context)
            .noAutoDismiss()
            .customView(R.layout.popup_reminder_dialog, noVerticalPadding = true)



        // button text
        if (event_ID == 0L){
            // Add Mode
            dialog.button_left.text = getText(R.string.msg_button_cancel)
            // date
            dialog.reminder_date.text = LocalDate.now().format(
                DateTimeFormatter.ofPattern("MM/dd/yyyy"))
            // time
            dialog.reminder_time.text = LocalTime.now().format(
                DateTimeFormatter.ofPattern("HH:mm:ss"))

        } else {
            // Edit Mode
            dialog.button_left.text =
                getText(R.string.msg_button_delete)
            // load event from database
            val event = calendarViewModel.getEventRecord(context, event_ID)
            // memo
            dialog.reminder_memo.setText(event.Event_Memo)
            // date
            dialog.reminder_date.text = event.Event_Date.format(
                DateTimeFormatter.ofPattern("MM/dd/yyyy")
            )
            // time
            dialog.reminder_time.text = event.Event_Date.format(
                DateTimeFormatter.ofPattern("HH:mm:ss")
            )
        }
        // set text for save button
        dialog.button_right.text = getText(R.string.menu_save)


        // Delete or Cancel Button click
        dialog.button_left.setOnClickListener(){
            if (event_ID == 0L){

                // cancel button
                dialog.dismiss()

            }else{

                // delete button

                // popup confirm window
                // --------------------------------------------------------------------------
                val dialogBuilder = AlertDialog.Builder(activity)
                dialogBuilder.setMessage(getText(R.string.msg_content_event_delete))
                    .setCancelable(true)
                    .setPositiveButton(getText(R.string.msg_button_confirm)) { _, _ ->
                        // confirm delete
                        // exit
                        dialog.dismiss()
                        // delete
                        deleteEvent(context, event_ID)
                    }
                    .setNegativeButton(getText(R.string.msg_button_cancel)) { popupWindow, _ ->
                        // cancel
                        popupWindow.cancel()
                    }

                // set Title Style
                val titleView = layoutInflater.inflate(R.layout.popup_title,null)
                // set Title Text
                titleView.tv_popup_title_text.text = getText(R.string.msg_Title_prompt)

                val alert = dialogBuilder.create()
                alert.setCustomTitle(titleView)
                alert.show()
                // --------------------------------------------------------------------------
            }
        }

        // save button click
        dialog.button_right.setOnClickListener(){
            // save
            if (dialog.reminder_memo.text.toString().trim() ==""){

                // popup prompt window "Cannot save with no content"
                // --------------------------------------------------------------------------
                val dialogBuilder = AlertDialog.Builder(activity)
                dialogBuilder.setMessage(getText(R.string.msg_cannot_save_with_no_content))
                    .setCancelable(true)
                    .setPositiveButton(getText(R.string.msg_button_ok)){ popupWindow,_ ->
                        popupWindow.cancel()
                    }

                // set Title Style
                val titleView = layoutInflater.inflate(R.layout.popup_title,null)
                // set Title Text
                titleView.tv_popup_title_text.text = getText(R.string.msg_Title_prompt)

                val alert = dialogBuilder.create()
                //alert.setIcon(R.drawable.ic_baseline_delete_forever_24)
                alert.setCustomTitle(titleView)
                alert.show()
                // --------------------------------------------------------------------------

            } else{
                // create event
                val event = Event(
                    Event_ID = event_ID,
                    Event_Date = getInternationalDateFromAmericanDate(
                        dialog.reminder_date.text.toString() + " " +
                                dialog.reminder_time.text.toString()),
                    Event_Memo = dialog.reminder_memo.text.toString()
                )
                // save
                calendarViewModel.saveEventRecord(context, event)
                // close dialog
                dialog.dismiss()
                //refresh
                refreshCalendar()
            }
        }

        // date picker click
        dialog.reminder_date.setOnClickListener(){

            val date = LocalDate.parse(dialog.reminder_date.text.toString(), DateTimeFormatter.ofPattern("MM/dd/yyyy"))

            DateTimePicker(
                date.year,
                date.monthValue-1,
                date.dayOfMonth
            ).pickDate(context) { _, year, month, day ->
                // save date to textView
                dialog.reminder_date.text = LocalDate.of(year, month + 1, day)
                    .format(DateTimeFormatter.ofPattern("MM/dd/yyyy"))
            }
        }


        dialog.show()
    }

    private fun deleteEvent(context: Context, event_ID: Long){
        calendarViewModel.deleteEventRecord(context,event_ID)
        refreshCalendar()
    }

}

