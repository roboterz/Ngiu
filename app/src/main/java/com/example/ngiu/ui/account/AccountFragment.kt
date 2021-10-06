package com.example.ngiu.ui.account

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ngiu.data.entities.Person
import com.example.ngiu.databinding.FragmentAccountBinding
import com.example.ngiu.functions.AccountListAdapter
import kotlinx.android.synthetic.main.fragment_account.*

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

                // specify the layout manager for recycler view
                recyclerView.layoutManager = linearLayoutManager

                // finally, data bind the recycler view with adapter
                recyclerView.adapter = AccountListAdapter(accountArr)
            }
        }.start()
    }
}
