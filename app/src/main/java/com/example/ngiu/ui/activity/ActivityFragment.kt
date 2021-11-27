package com.example.ngiu.ui.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.*
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.findNavController
import com.example.ngiu.MainActivity
import com.example.ngiu.R
import com.example.ngiu.databinding.FragmentActivityBinding
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_activity.*


class ActivityFragment : Fragment() {
    //


    private lateinit var activityViewModel: ActivityViewModel
    private var _binding: FragmentActivityBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private var vpAdapter: TransListAdapter? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        initAdapter()
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        activityViewModel =
            ViewModelProvider(this).get(ActivityViewModel::class.java)

        _binding = FragmentActivityBinding.inflate(inflater, container, false)


        // load data to ram
        Thread {
            activityViewModel.loadDataToRam(activity)
        }.start()

        return binding.root
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
                    // switch to record fragment
                    navigateToRecordFragment()
                    true
                }
                else -> super.onOptionsItemSelected(it)
            }
        }

        // floating Add transaction button
        val fab: View = view.findViewById(R.id.floatingAddTransactionButton)
        fab.setOnClickListener {
            // switch to record fragment
            navigateToRecordFragment()
        }

        // call readPerson function on the bottom of this class

    }


    override fun onResume() {
        super.onResume()

        // Show bottom bar
        (activity as MainActivity).setNavBottomBarVisibility(View.VISIBLE)

        Thread {
            activity?.runOnUiThread {
                recyclerView_activity.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL,false)
                //vpAdapter?.stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY

                vpAdapter?.setList(activityViewModel.getData())
                recyclerView_activity.adapter = vpAdapter
            }
        }.start()

    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }



    // init Adapter
    private fun initAdapter() {
        
        Thread {
            this.activity?.runOnUiThread {

                vpAdapter = this.context?.let {
                    TransListAdapter(object: TransListAdapter.OnClickListener {
                        // catch the item click event from adapter
                        override fun onItemClick(transID: Long) {
                            // switch to record fragment (Edit mode)
                            navigateToRecordFragment(transID)
                        }
                    })
                }
                //recyclerView_activity.adapter = vpAdapter
            }
        }.start()
    }


    private fun navigateToRecordFragment(transID: Long = 0){
        // hide nav bottom bar
        //(activity as MainActivity).setNavBottomBarVisibility(View.GONE)

        //parentFragmentManager.setFragmentResult("requestKey", bundleOf("bundleKey" to transactionList[position].Transaction_ID))
        if (transID >0) setFragmentResult("record_edit_mode", bundleOf("rID" to transID))
        // switch to record fragment
        findNavController().navigate(R.id.navigation_record)
    }
}
