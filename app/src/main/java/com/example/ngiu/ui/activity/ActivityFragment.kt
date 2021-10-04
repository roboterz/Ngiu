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
import com.example.ngiu.data.entities.Trans
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
        fab.setOnClickListener { view ->

            // hide nav bottom bar
            (activity as MainActivity).setNavBottomBarVisibility(View.GONE)

            // navigate to add record screen
            view.findNavController().navigate(R.id.navigation_record)

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
        val transArr = ArrayList<Trans>()

        Thread {
            val allRecord = activityViewModel.readData(activity) as List<Trans>

            this.activity?.runOnUiThread {

                for (i in allRecord.indices) {
                    transArr.add( Trans(
                        allRecord[i].ID ,
                        allRecord[i].TransTypeID,
                        allRecord[i].SubCategoryID,
                        allRecord[i].PayerID,
                        allRecord[i].RecipientID,
                        allRecord[i].Amount,
                        allRecord[i].Date,
                        allRecord[i].PersonID,
                        allRecord[i].MerchantID,
                        allRecord[i].Memo,
                        allRecord[i].ProjectID,
                        allRecord[i].ReimburseStatus,
                        allRecord[i].PeriodID,
                        )
                    )
                }

                val linearLayoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL,false)

                // specify the layout manager for recycler view
                recyclerView.layoutManager = linearLayoutManager

                // finally, data bind the recycler view with adapter
                recyclerView.adapter = TransListAdapter(transArr)
            }
        }.start()
    }
}
