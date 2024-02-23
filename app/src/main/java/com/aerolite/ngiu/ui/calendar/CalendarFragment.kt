package com.aerolite.ngiu.ui.calendar

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.aerolite.ngiu.databinding.FragmentCalendarBinding
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import com.aerolite.ngiu.MainActivity
import com.aerolite.ngiu.R
import com.aerolite.ngiu.data.entities.Event
import com.aerolite.ngiu.data.entities.returntype.CalendarDetail
import com.aerolite.ngiu.functions.DateTimePicker
import com.aerolite.ngiu.functions.EVENT_MODE_EVERY_DAY
import com.aerolite.ngiu.functions.EVENT_MODE_EVERY_MONTH
import com.aerolite.ngiu.functions.EVENT_MODE_EVERY_WEEK
import com.aerolite.ngiu.functions.get2DigitFormat
import com.aerolite.ngiu.functions.getInternationalDateFromAmericanDate
//import kotlinx.android.synthetic.main.fragment_calendar.*
//import kotlinx.android.synthetic.main.fragment_record.*
//import kotlinx.android.synthetic.main.popup_reminder_dialog.*
//import kotlinx.android.synthetic.main.popup_title.view.*
import java.time.LocalDate
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
        binding.toolbarCalendar.menu.findItem(R.id.action_add).isVisible = true


        // toolbar menu item clicked
        binding.toolbarCalendar.setOnMenuItemClickListener{
            when (it.itemId) {
                R.id.action_add -> {
                    // switch to Event fragment
                    //navigateToEventFragment(0L)
                    showReminderDialog(view.context,  CalendarDetail())
                    true
                }
                else -> true
            }
        }

        // pending payment
        binding.tvCalendarPendingPaymentAmount.text = get2DigitFormat(calendarViewModel.pendingPayment)

        // pending receivable
        binding.tvCalendarPendingReceivableAmount.text = get2DigitFormat(calendarViewModel.pendingIncome)

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

                binding.recyclerViewCalendar.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL,false)
                cAdapter = this.context?.let {
                    CalendarAdapter(object: CalendarAdapter.OnClickListener {
                        // catch the item click event from adapter
                        override fun onItemClick(cDetail: CalendarDetail) {
                            // Open/switch to account detail

                            when (cDetail.type){

                                // show reminder dialog
                                3 -> showReminderDialog(requireContext(),cDetail)
                            }


                            // refresh
                            //refreshCalendar()
                        }
                    })
                }

                binding.recyclerViewCalendar.adapter = cAdapter
            }
        }.start()
    }


    // refresh Calendar
    private fun refreshCalendar(){

        calendarViewModel.loadDataToRam(requireContext())
        cAdapter?.setList(calendarViewModel.calendarDetail)
        binding.recyclerViewCalendar.adapter = cAdapter
    }



    /** Reminder Window **/
    @SuppressLint("CutPasteId")
    private fun showReminderDialog(context: Context, cDetail: CalendarDetail){

        var blnPeriod: Boolean = false
        var periodMode: Int = 0

        val dialog = MaterialDialog(context)
            .noAutoDismiss()
            .customView(R.layout.popup_reminder_dialog, noVerticalPadding = true)



        /** initial text **/
        if (cDetail.id == 0L){
            /** Add Mode **/
            // left button
            dialog.findViewById<TextView>(R.id.button_left).text = getText(R.string.msg_button_cancel)
            // date
            dialog.findViewById<TextView>(R.id.reminder_date).text = LocalDate.now().format(
                DateTimeFormatter.ofPattern("MM/dd/yyyy"))
            // time
            dialog.findViewById<TextView>(R.id.reminder_time).text = LocalTime.now().format(
                DateTimeFormatter.ofPattern("HH:mm:ss"))
            // period
            // period intervals
            // period intervals day

        } else {
            /** Edit Mode **/
            // left button
            dialog.findViewById<TextView>(R.id.button_left).text = getText(R.string.msg_button_delete)
            // load event from database
            //val event = calendarViewModel.getEventRecord(context, cDetail.id)
            // memo
            dialog.findViewById<TextView>(R.id.reminder_memo).setText(cDetail.memo )
            // date
            dialog.findViewById<TextView>(R.id.reminder_date).text =cDetail.date.format(
                DateTimeFormatter.ofPattern("MM/dd/yyyy")
            )
            // time
            dialog.findViewById<TextView>(R.id.reminder_time).text = cDetail.date.format(
                DateTimeFormatter.ofPattern("HH:mm:ss")
            )
            // period
            blnPeriod = cDetail.type == 4
            setPeriodOptionIcon(dialog.findViewById<TextView>(R.id.reminder_period), blnPeriod)
            // period layout
            setPeriodLayout(dialog.findViewById<TextView>(R.id.ly_reminder_period), blnPeriod)
            // period intervals
            getPeriodIntervalsOption(context, periodMode)
            // todo period intervals day

        }

        /** set text for save button **/
        dialog.findViewById<TextView>(R.id.button_right).text = getText(R.string.menu_save)


        /** Delete or Cancel Button click **/
        dialog.findViewById<TextView>(R.id.button_left).setOnClickListener(){
            if (cDetail.id == 0L){

                // cancel button
                dialog.dismiss()

            }else{

                // delete button
                // popup confirm window
                // --------------------------------------------------------------------------

                // set Title Style
                val titleView = layoutInflater.inflate(R.layout.popup_title,null)
                // set Title Text
                titleView.findViewById<TextView>(R.id.tv_popup_title_text).text = getText(R.string.msg_Title_prompt)


                val msgDialog = AlertDialog.Builder(activity)
                            .setMessage(getText(R.string.msg_content_event_delete))
                            .setCancelable(true)
                            // Confirm Button
                            .setPositiveButton(getText(R.string.msg_button_confirm)) { _, _ ->
                                // confirm delete
                                // exit
                                dialog.dismiss()
                                // delete
                                deleteEvent(context, cDetail.id)
                            }
                            // Cancel Button
                            .setNegativeButton(getText(R.string.msg_button_cancel)) { popupWindow, _ ->
                                // cancel
                                popupWindow.cancel()
                            }
                            .create()

                // title view
                msgDialog.setCustomTitle(titleView)
                // show dialog
                msgDialog.show()

                // button text color
                msgDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(context, R.color.app_button_text_highlight))
                msgDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(ContextCompat.getColor(context, R.color.app_button_text))

                // --------------------------------------------------------------------------
            }
        }

        /** save button click **/
        dialog.findViewById<TextView>(R.id.button_right).setOnClickListener(){
            // save
            if (dialog.findViewById<TextView>(R.id.reminder_memo).text.toString().trim() ==""){

                // popup prompt window "Cannot save with no content"
                // --------------------------------------------------------------------------
                // set Title Style
                val titleView = layoutInflater.inflate(R.layout.popup_title,null)
                // set Title Text
                titleView.findViewById<TextView>(R.id.tv_popup_title_text).text = getText(R.string.msg_Title_prompt)

                val msgDialog = AlertDialog.Builder(activity)
                            .setMessage(getText(R.string.msg_cannot_save_with_no_content))
                            .setCancelable(true)
                            .setPositiveButton(getText(R.string.msg_button_ok)){ popupWindow,_ ->
                                popupWindow.cancel()
                            }
                            .create()

                // title view
                msgDialog.setCustomTitle(titleView)
                // show dialog
                msgDialog.show()

                // button text color
                msgDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(context, R.color.app_button_text_highlight))
                msgDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(ContextCompat.getColor(context, R.color.app_button_text))
                // --------------------------------------------------------------------------



            } else{
                // create event
                val event = Event(
                    Event_ID = cDetail.id,
                    Event_Date = getInternationalDateFromAmericanDate(
                        dialog.findViewById<TextView>(R.id.reminder_date).text.toString() + " " +
                                dialog.findViewById<TextView>(R.id.reminder_time).text.toString()),
                    Event_Memo = dialog.findViewById<TextView>(R.id.reminder_memo).text.toString()
                )
                // save
                calendarViewModel.saveEventRecord(context, event)
                // close dialog
                dialog.dismiss()
                //refresh
                refreshCalendar()
            }
        }

        /** date picker click **/
        dialog.findViewById<TextView>(R.id.reminder_date).setOnClickListener(){

            val date = LocalDate.parse(dialog.findViewById<TextView>(R.id.reminder_date).text.toString(), DateTimeFormatter.ofPattern("MM/dd/yyyy"))

            DateTimePicker(
                date.year,
                date.monthValue-1,
                date.dayOfMonth
            ).pickDate(context) { _, year, month, day ->
                // save date to textView
                dialog.findViewById<TextView>(R.id.reminder_date).text = LocalDate.of(year, month + 1, day)
                    .format(DateTimeFormatter.ofPattern("MM/dd/yyyy"))
            }
        }

        /** Period **/
        dialog.findViewById<TextView>(R.id.reminder_period).setOnClickListener{
            blnPeriod = !blnPeriod
            // period icon
            setPeriodOptionIcon(dialog.findViewById<TextView>(R.id.reminder_period), blnPeriod)
            // period layout
            setPeriodLayout(dialog.findViewById<TextView>(R.id.ly_reminder_period), blnPeriod)
            // end date text
            setPeriodLayout(dialog.findViewById<TextView>(R.id.reminder_text_end), blnPeriod)
        }

        /** Period Mode **/
        dialog.findViewById<TextView>(R.id.reminder_period_mode).setOnClickListener {
            when(periodMode){
                EVENT_MODE_EVERY_DAY, EVENT_MODE_EVERY_WEEK, EVENT_MODE_EVERY_MONTH -> {
                    periodMode++
                }
                else -> {
                    periodMode = 0
                }
            }
            dialog.findViewById<TextView>(R.id.reminder_period_mode).text = getPeriodIntervalsOption(requireContext(), periodMode)
        }

        dialog.show()
    }




    private fun deleteEvent(context: Context, event_ID: Long){
        calendarViewModel.deleteEventRecord(context,event_ID)
        refreshCalendar()
    }


    private fun getPeriodIntervalsOption(context: Context, int: Int):String{
        val array: Array<String> = context.resources.getStringArray(R.array.data_period_array)
        return array[int]
    }


    private fun setPeriodOptionIcon(textView: TextView, period: Boolean) {
        if (period){
            textView.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.ic_baseline_check_box_24,0)
        }else{
            textView.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.ic_baseline_check_box_outline_blank_24,0)
        }
    }
    private fun setPeriodLayout(view: View, period: Boolean) {
        if (period){
            view.visibility = View.VISIBLE
        }else{
            view.visibility = View.GONE
        }
    }
}

