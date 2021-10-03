package com.example.ngiu.ui.calendar

import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.ngiu.databinding.FragmentCalendarBinding
import androidx.appcompat.app.AlertDialog
import com.example.ngiu.functions.DateTimePicker
import android.app.TimePickerDialog


class CalendarFragment : Fragment() {

    private var recordId: Long = 0

    private lateinit var calendarViewModel: CalendarViewModel
    private var _binding: FragmentCalendarBinding? = null
    private val TAG = "HomeFragment"

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        calendarViewModel =
            ViewModelProvider(this).get(CalendarViewModel::class.java)

        _binding = FragmentCalendarBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textHome
        calendarViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        //  get data fom edittext when user enter something
        binding.btnAdd.setOnClickListener {
            val strName = binding.etxtName.text
            val strDate = binding.etxtDate.text
            val strMemo = binding.etxtMemo.text
            Log.e(
                TAG,
                "onViewCreated: " + "Name: " + strName + " Date: " + strDate + " Memo: " + strMemo
            )

            performAddingTask(strName, strDate, strMemo)
        }


        //Open Database
        /*  val context = this
          var db: DBManager = DBManager(context)

          //Add Button
          val buttonAdd: Button = findViewById(R.id.btnAdd)
          buttonAdd.setOnClickListener{
              //do something
              val txtName: EditText = findViewById(R.id.etxtName)
              var at = android.content.ContentValues()
              at.put( "Name", txtName.text.toString())
              db.insertData(at)

              txtName.text.clear()
          }*/

        /*  //Edit Button
          val buttonEdit: Button = findViewById(R.id.btnEdit)
          buttonEdit.setOnClickListener{
              //val dialogBuilder = AlertDialog.Builder(this)
              //dialogBuilder.setMessage("Crazy Kotlin!!!!!@#@#@#$!")

              //val alert = dialogBuilder.create()
              //alert.setTitle("DamDam")
              //alert.show()
              val data = db.readData()
              var txt: EditText = findViewById(R.id.etxtMemo)
              txt.text = null
              for (i in 0 until data.size) {
                  txt.append(
                      data[i].id.toString() + " " + data[i].Name + "\n"
                  )
              }
          }*/


        //Delete Button
        val buttonDel: Button = binding.btnDel
        buttonDel.setOnClickListener{


            //time picker
            DateTimePicker().PickTime(context,TimePickerDialog.OnTimeSetListener { _, hour, minute ->
                //do something after time picked
                Toast.makeText(context , hour.toString() + ":" +  minute.toString(), Toast.LENGTH_SHORT).show()
            })

        }

        //textview popup menu
        val txtview: TextView = binding.textHome

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

        }



    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    // Thread is used to separate from the main thread of a component
    private fun performAddingTask(
        strName: Editable,
        strDate: Editable,
        strMemo: Editable
    ) {
        Thread {
            recordId = calendarViewModel.insertData(activity, strName, strDate, strMemo)

            activity?.runOnUiThread {
                if (recordId > 0) {
                    Toast.makeText(activity, "Record Saved", Toast.LENGTH_SHORT).show()
                    strName.clear()
                    strDate.clear()
                    strMemo.clear()
                }
            }
        }.start()
    }
}