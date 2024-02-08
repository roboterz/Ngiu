package com.aerolite.ngiu.ui.activity

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.*
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.aerolite.ngiu.MainActivity
import com.aerolite.ngiu.R
import com.aerolite.ngiu.databinding.FragmentActivityBinding
import com.aerolite.ngiu.functions.switchToRecordFragment
//import kotlinx.android.synthetic.main.fragment_activity.*


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


    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // init Adapter
        initAdapter(view, this)


        // set up toolbar icon and click event
        // choose items to show
        //toolbar_activity.menu.findItem(R.id.action_add).isVisible = true
        binding.toolbarActivity.menu.findItem(R.id.action_reimburse).isVisible = true
        binding.toolbarActivity.menu.findItem(R.id.action_chart).isVisible = true
        binding.toolbarActivity.menu.findItem(R.id.action_settings).isVisible = true
        binding.toolbarActivity.menu.findItem(R.id.action_template).isVisible = true

        // menu item clicked
        binding.toolbarActivity.setOnMenuItemClickListener {
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

                R.id.action_template -> {
                    // switch to template fragment
                    navigateToTemplateFragment()
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


        // show the info at title
        binding.tvCurrentMonthExpenseBalance.text = "" + "%.2f".format(activityViewModel.monthExpense)
        binding.tvCurrentMonthIncomeBalance.text = "" + "%.2f".format(activityViewModel.monthIncome)
        binding.tvBudgetBalance.text = "" + "%.2f".format(activityViewModel.budget)

    }


    override fun onResume() {
        super.onResume()

        // Show bottom bar
        (activity as MainActivity).setNavBottomBarVisibility(View.VISIBLE)

        /*// load transaction list
        activityViewModel.getTransDetail().observe(this, Observer { it ->
            vpAdapter?.setList(it)
        })*/


    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    /** init Adapter **/
    @SuppressLint("CutPasteId")
    private fun initAdapter(view: View, fragment: Fragment) {

        binding.recyclerViewActivity.layoutManager =
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
        binding.recyclerViewActivity.adapter = vpAdapter

        // load transaction list with LiveData
        activityViewModel.getTransDetail().observe(viewLifecycleOwner, Observer { it ->
            vpAdapter?.setList(it)
        })
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

    private fun navigateToTemplateFragment() {
        findNavController().navigate(R.id.navigation_template_list)

    }
}