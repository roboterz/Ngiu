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
import android.widget.Button

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

        //Add Button
        val buttonAdd: Button = findViewById(R.id.btnAdd)
        buttonAdd.setOnClickListener{
            //do something
        }

        //Edit Button
        val buttonEdit: Button = findViewById(R.id.btnEdit)
        buttonEdit.setOnClickListener{
            val dialogBuilder = AlertDialog.Builder(this)
            dialogBuilder.setMessage("Crazy Kotlin!!!!!@#@#@#$!")

            val alert = dialogBuilder.create()
            alert.setTitle("DamDam")
            alert.show()
        }

        //Delete Button
        val buttonDel: Button = findViewById(R.id.btnDel)
        buttonDel.setOnClickListener{
            //do something
        }
    }
}