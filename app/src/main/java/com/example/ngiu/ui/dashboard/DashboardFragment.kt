package com.example.ngiu.ui.dashboard

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ngiu.data.entities.Person
import com.example.ngiu.databinding.FragmentDashboardBinding
import com.example.ngiu.functions.AccountListAdapter
import kotlinx.android.synthetic.main.fragment_dashboard.*
import kotlinx.coroutines.flow.Flow

class DashboardFragment : Fragment() {

    private lateinit var dashboardViewModel: DashboardViewModel
    private var _binding: FragmentDashboardBinding? = null
    val personArr = ArrayList<Person>()

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dashboardViewModel =
            ViewModelProvider(this).get(DashboardViewModel::class.java)

        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root

        //val textView: TextView = binding.textDashboard
        //dashboardViewModel.text.observe(viewLifecycleOwner, Observer {
        //    textView.text = it
        //})

        return root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //call readPerson function on the bottom of this class
        readPerson(view)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    // arrayadapter for listview
    //  Log.e("TAG", "readPerson: "+i+" "+allRecord.get(i) )
    // TODO make UI for recycleview and switch to recyleview
    private fun readPerson(view: View) {
        Thread {
            val allRecord = dashboardViewModel.readData(activity) as List<Person>
            activity?.runOnUiThread {
                // for(int i = 0; i < allRecord.size; i++) {
                for (i in 0 until allRecord.size) {
                    //personArr.add("Id: " + allRecord.get(i).ID + " Name: " + allRecord.get(i).Name)
                    personArr.add(Person( allRecord.get(i).ID , allRecord.get(i).Name))
                    val arrayAdapter = ArrayAdapter(
                        view.context,
                        android.R.layout.simple_list_item_1,
                        personArr
                    )
                    //binding.listView.adapter = arrayAdapter
                }

                val linearLayoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL,false)

                // specify the layout manager for recycler view
                recyclerView.layoutManager = linearLayoutManager

                // finally, data bind the recycler view with adapter
                recyclerView.adapter = AccountListAdapter(personArr)
            }
        }.start()
    }
}
