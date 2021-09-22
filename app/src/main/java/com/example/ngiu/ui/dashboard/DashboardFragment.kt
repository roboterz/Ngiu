package com.example.ngiu.ui.dashboard

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
import com.example.ngiu.data.entities.Person
import com.example.ngiu.databinding.FragmentDashboardBinding
import kotlinx.coroutines.flow.Flow

class DashboardFragment : Fragment() {

    private lateinit var dashboardViewModel: DashboardViewModel
    private var _binding: FragmentDashboardBinding? = null
    val personArr = ArrayList<String>()

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

        val textView: TextView = binding.textDashboard
        dashboardViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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
                for(i in 0 until allRecord.size ) {
                    personArr.add("Id: "+ allRecord.get(i).ID+" Name: "+allRecord.get(i).Name)
                    val arrayAdapter = ArrayAdapter(view.context,android.R.layout.simple_list_item_1,personArr)
                    binding.listView.adapter=arrayAdapter
                }
            }
        }.start()
    }
}