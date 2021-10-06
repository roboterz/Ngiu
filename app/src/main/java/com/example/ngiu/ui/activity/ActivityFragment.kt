package com.example.ngiu.ui.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.*
import androidx.navigation.findNavController
import com.example.ngiu.MainActivity
import com.example.ngiu.R
import com.example.ngiu.data.entities.list.TransactionDetail
import com.example.ngiu.databinding.FragmentActivityBinding
import com.example.ngiu.functions.TransListAdapter
import kotlinx.android.synthetic.main.fragment_activity.*


class ActivityFragment : Fragment() {
    private lateinit var activityViewModel: ActivityViewModel
    private var _binding: FragmentActivityBinding? = null
    //val personArr = ArrayList<Person>()

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
            activityViewModel =
                ViewModelProvider(this).get(ActivityViewModel::class.java)

            _binding = FragmentActivityBinding.inflate(inflater, container, false)
            val root: View = binding.root

            //super.onCreate(savedInstanceState)
            //setHasOptionsMenu(true)

            return root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        // set up toolbar icon and click event
        // choose items to show
        toolbar_activity.menu.findItem(R.id.action_add).isVisible = true

        // menu item clicked
        toolbar_activity.setOnMenuItemClickListener{
            when (it.itemId) {
                R.id.action_add -> {

                    // hide nav bottom bar
                    (activity as MainActivity).setNavBottomBarVisibility(View.GONE)

                    // navigate to add record screen
                    view.findNavController().navigate(R.id.navigation_record)

                    true
                }

                else -> super.onOptionsItemSelected(it)
            }
        }


        // floating Add transaction button
        val fab: View = view.findViewById(R.id.floatingAddTransactionButton)
        fab.setOnClickListener { it ->

            // hide nav bottom bar
            (activity as MainActivity).setNavBottomBarVisibility(View.GONE)

            // navigate to add record screen
            it.findNavController().navigate(R.id.navigation_record)

        }



        // call readPerson function on the bottom of this class
        readTransaction(view)
        //Toast.makeText(context, "read", Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }



    // load data to RecyclerView
    private fun readTransaction(view: View) {
        // list of Transaction
        val transactionList = ArrayList<TransactionDetail>()

        Thread {
            val allRecord = activityViewModel.readData(activity) as List<TransactionDetail>

            this.activity?.runOnUiThread {

                for (i in allRecord.indices) {
                    transactionList.add( TransactionDetail(
                        allRecord[i].Transaction_ID ,
                        allRecord[i].TransactionType_Name,
                        allRecord[i].SubCategory_Name,
                        allRecord[i].Account_Name,
                        allRecord[i].AccountRecipient_Name,
                        allRecord[i].Transaction_Amount,
                        allRecord[i].Transaction_Date,
                        allRecord[i].Person_Name,
                        allRecord[i].Merchant_Name,
                        allRecord[i].Transaction_Memo,
                        allRecord[i].Project_Name,
                        allRecord[i].Transaction_ReimburseStatus,
                        allRecord[i].Period_ID,
                        )
                    )
                }

                val linearLayoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL,false)

                // specify the layout manager for recycler view
                recyclerView.layoutManager = linearLayoutManager

                // finally, data bind the recycler view with adapter
                recyclerView.adapter = TransListAdapter(transactionList)
            }
        }.start()
    }
}
