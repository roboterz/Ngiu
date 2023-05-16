package com.example.ngiu.ui.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.*
import androidx.navigation.fragment.findNavController
import com.example.ngiu.MainActivity
import com.example.ngiu.R
import com.example.ngiu.databinding.FragmentActivityBinding
import com.example.ngiu.functions.switchToRecordFragment
import kotlinx.android.synthetic.main.fragment_activity.*


class ActivityFragment : Fragment() {
    //


    private lateinit var activityViewModel: ActivityViewModel
    private var _binding: FragmentActivityBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private var vpAdapter: ActivityListAdapter? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


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
        //Thread {
        activityViewModel.loadDataToRam(requireContext())
        //}.start()


        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // init Adapter
        initAdapter(view, this)


        // set up toolbar icon and click event
        // choose items to show
        //toolbar_activity.menu.findItem(R.id.action_add).isVisible = true
        toolbar_activity.menu.findItem(R.id.action_reimburse).isVisible = true
        toolbar_activity.menu.findItem(R.id.action_chart).isVisible = true
        toolbar_activity.menu.findItem(R.id.action_settings).isVisible = true

        // menu item clicked
        toolbar_activity.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.action_add -> {
                    // switch to record fragment
                    //navigateToRecordFragment()
                    switchToRecordFragment(view, this)
                    true
                }

                R.id.action_reimburse -> {
                    // switch to reimburse fragment
                    navigateToReimburseFragment()
                    true
                }

                R.id.action_chart -> {
                    // switch to chart fragment
                    navigateToReportFragment()
                    true
                }

                R.id.action_settings -> {
                    //switch to setting fragment
                    navigateToSettingFragment()
                    true
                }

                else -> true
            }
        }

        // floating Add transaction button
        val fab: View = view.findViewById(R.id.floatingAddTransactionButton)
        fab.setOnClickListener {
            // switch to record fragment
            //navigateToRecordFragment()
            switchToRecordFragment(view, this)
        }

        // call readPerson function on the bottom of this class

    }


    override fun onResume() {
        super.onResume()

        // Show bottom bar
        (activity as MainActivity).setNavBottomBarVisibility(View.VISIBLE)

        // load transaction list
        Thread {
            activity?.runOnUiThread {
                //vpAdapter?.stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY

                vpAdapter?.setList(activityViewModel.transactionDetail)
                //recyclerView_activity.adapter = vpAdapter
            }
        }.start()

        // show the info at title
        tvCurrentMonthExpenseBalance.text = "$" + "%.2f".format(activityViewModel.monthExpense)
        tvCurrentMonthIncomeBalance.text = "$" + "%.2f".format(activityViewModel.monthIncome)
        tvBudgetBalance.text = "$" + "%.2f".format(activityViewModel.budget)

    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    /** init Adapter **/
    private fun initAdapter(view: View, fragment: Fragment) {

        Thread {
            this.activity?.runOnUiThread {

                recyclerView_activity.layoutManager =
                    LinearLayoutManager(context, RecyclerView.VERTICAL, false)
                vpAdapter = this.context?.let {
                    ActivityListAdapter(object : ActivityListAdapter.OnClickListener {
                        // catch the item click event from adapter
                        override fun onItemClick(transID: Long) {
                            // switch to record fragment (Edit mode)
                            //navigateToRecordFragment(transID)
                            switchToRecordFragment(view, fragment, transID)
                        }
                    })
                }
                recyclerView_activity.adapter = vpAdapter
            }
        }.start()
    }


    /*    private fun navigateToRecordFragment(transID: Long = 0){
        val bundle = Bundle().apply {
            putLong(KEY_RECORD_TRANSACTION_ID, transID)
        }
        // switch to record fragment
        findNavController().navigate(R.id.navigation_record, bundle)
    }*/

    private fun navigateToSettingFragment() {
        findNavController().navigate(R.id.navigation_setting)
    }

    private fun navigateToReportFragment() {
        findNavController().navigate(R.id.navigation_report)
    }

    private fun navigateToReimburseFragment() {
        findNavController().navigate(R.id.navigation_reimburse)

    }
}