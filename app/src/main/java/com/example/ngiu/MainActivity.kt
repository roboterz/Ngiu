package com.example.ngiu

import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.ngiu.databinding.ActivityMainBinding
import android.app.AlertDialog
import android.content.ContentValues
import android.content.Context
import android.icu.text.AlphabeticIndex
import android.widget.*
import com.example.ngiu.data.AppDatabase
/*import com.example.ngiu.data.DBManager
import com.example.ngiu.data.Record*/
import com.example.ngiu.data.entities.Account
import com.example.ngiu.data.entities.AccountType

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
            //do something

        }
    }


}