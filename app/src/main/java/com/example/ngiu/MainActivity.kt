package com.example.ngiu


import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.navigation.Navigation.findNavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.ngiu.databinding.ActivityMainBinding
import com.example.ngiu.ui.activity.ActivityFragment
/*import com.example.ngiu.data.DBManager
import com.example.ngiu.data.Record*/
import com.example.ngiu.ui.record.RecordFragment
import com.example.ngiu.ui.setting.SettingFragment
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity: AppCompatActivity() {

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
                R.id.navigation_activity, R.id.navigation_account, R.id.navigation_calender, R.id.navigation_setting
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        //....
        supportActionBar?.hide()


    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)

        if (hasFocus){
            //nav_view.visibility=View.GONE
        }
    }

    public fun setNavBottomBarVisibility(ID:Int){
        nav_view.visibility = ID
    }






}


