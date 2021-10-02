package com.example.ngiu


import android.app.TimePickerDialog
import android.os.Bundle
import android.view.View
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.ngiu.databinding.ActivityMainBinding
import android.widget.*
/*import com.example.ngiu.data.DBManager
import com.example.ngiu.data.Record*/
import android.widget.PopupMenu
import androidx.appcompat.app.AlertDialog
import com.example.ngiu.functions.DateTimePicker


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

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
        val buttonDel: Button = findViewById(R.id.btnDel)
        buttonDel.setOnClickListener{


            //time picker
            DateTimePicker().PickTime(this,TimePickerDialog.OnTimeSetListener { _, hour, minute ->
                //do something after time picked
                Toast.makeText(this , hour.toString() + ":" +  minute.toString(), Toast.LENGTH_SHORT).show()
            })

        }

        //textview popup menu
        val txtview: TextView = findViewById(R.id.text_home)
        txtview.setOnClickListener{
            //do something

            val array = arrayOf("1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20","21","22","23","24","25","26","27","28")
            //val array = arrayOf("1","2","3","4")

            // Initialize a new instance of alert dialog builder object
            val builder = AlertDialog.Builder(this)

            builder.setTitle("Statement Day")


            // Set items form alert dialog
            builder.setItems(array,{_, which ->
                // Get the dialog selected item
                val selected = array[which]
                Toast.makeText(this, "You Clicked : " + selected, Toast.LENGTH_SHORT).show()

            })

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



}


