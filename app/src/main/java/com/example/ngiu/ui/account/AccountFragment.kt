package com.example.ngiu.ui.account

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ngiu.MainActivity
import com.example.ngiu.R
import com.example.ngiu.data.entities.Person
import com.example.ngiu.databinding.FragmentAccountBinding
import com.example.ngiu.functions.AccountListAdapter
import kotlinx.android.synthetic.main.fragment_account.*
import kotlinx.android.synthetic.main.fragment_account.toolbar_account
import kotlinx.android.synthetic.main.fragment_activity.*

class AccountFragment : Fragment() {

    private lateinit var accountViewModel: AccountViewModel
    private var _binding: FragmentAccountBinding? = null


    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        accountViewModel =
            ViewModelProvider(this).get(AccountViewModel::class.java)

        _binding = FragmentAccountBinding.inflate(inflater, container, false)
        val root: View = binding.root

        //val textView: TextView = binding.textDashboard
        //dashboardViewModel.text.observe(viewLifecycleOwner, Observer {
        //    textView.text = it
        //})

        return root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

     /*   //call readPerson function on the bottom of this class
        readPerson(view)*/

        // set up toolbar icon and click event
        // choose items to show
        toolbar_account.menu.findItem(R.id.action_add).isVisible = true

        // menu item clicked
        toolbar_account.setOnMenuItemClickListener{
            when (it.itemId) {
                R.id.action_add -> {

                    // hide nav bottom bar
                    (activity as MainActivity).setNavBottomBarVisibility(View.GONE)

                    // navigate to add record screen
                    view.findNavController().navigate(R.id.navigation_add_account)

                    true
                }

                else -> super.onOptionsItemSelected(it)
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

  /*  private fun readPerson(view: View) {

        val accountArr = ArrayList<Person>()

        Thread {
            val allRecord = accountViewModel.readData(this.activity) as List<Person>
            this.activity?.runOnUiThread {
                // for(int i = 0; i < allRecord.size; i++) {
                for (i in allRecord.indices) {
                    //personArr.add("Id: " + allRecord.get(i).ID + " Name: " + allRecord.get(i).Name)
                    accountArr.add(Person( allRecord[i].Person_ID , allRecord[i].Person_Name))


                    //val arrayAdapter = ArrayAdapter(
                    //    view.context,
                     //   android.R.layout.simple_list_item_1,
                     //   accountArr
                    //)
                    //binding.listView.adapter = arrayAdapter
                }

                val linearLayoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL,false)

           *//*     // specify the layout manager for recycler view
                recyclerView.layoutManager = linearLayoutManager

                // finally, data bind the recycler view with adapter
                recyclerView.adapter = AccountListAdapter(accountArr)*//*
            }
        }.start()
    }*/
}
