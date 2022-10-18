package com.example.ngiu.ui.calendar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.ngiu.databinding.FragmentCalendarBinding
import com.example.ngiu.functions.DateTimePicker
import android.app.TimePickerDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ngiu.MainActivity
import com.example.ngiu.data.entities.Account
import com.example.ngiu.data.entities.returntype.CalendarModel
import com.example.ngiu.ui.activity.ActivityListAdapter
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


        //  get data fom edittext when user enter something

            //time picker
            //DateTimePicker().PickTime(context,TimePickerDialog.OnTimeSetListener { _, hour, minute ->
                //do something after time picked
                //Toast.makeText(context , hour.toString() + ":" +  minute.toString(), Toast.LENGTH_SHORT).show()
            //})



        //textview popup menu
        /*

        txtview.setOnClickListener{
            //do something

            val array = arrayOf("1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20","21","22","23","24","25","26","27","28")
            //val array = arrayOf("1","2","3","4")

            // Initialize a new instance of alert dialog builder object
            val builder = AlertDialog.Builder(requireContext())

            builder.setTitle("Statement Day")


            // Set items form alert dialog
            builder.setItems(array) { _, which ->
                // Get the dialog selected item
                val selected = array[which]
                Toast.makeText(context, "You Clicked : " + selected, Toast.LENGTH_SHORT).show()
            }

            // Create a new AlertDialog using builder object
            // Finally, display the alert dialog
            builder.create().show()



            /*
            val popupMenu: PopupMenu = PopupMenu(this,txtview)
            popupMenu.menuInflater.inflate(R.menu.popup_menu,popupMenu.menu)

            //add menu
            for (i in 1 until 20) {
                popupMenu.menu.add(i.toString())
            }

            //set click listener
            popupMenu.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener {    item ->
                Toast.makeText(this, "You Clicked : " + item.title, Toast.LENGTH_SHORT).show()
                true
            })

            //show popup menu
            popupMenu.show()

             */

         */





    }

    override fun onResume() {
        super.onResume()

        // Show bottom bar
        (activity as MainActivity).setNavBottomBarVisibility(View.VISIBLE)

        // load transaction list
        Thread {
            activity?.runOnUiThread {
                //vpAdapter?.stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY

                CAdapter?.setList(calendarViewModel.accountList)
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
                        override fun onItemClick(transID: Long) {
                            // switch to record fragment (Edit mode)

                        }
                    })
                }

                recyclerView_calendar.adapter = CAdapter
            }
        }.start()
    }

}

