package com.example.ngiu.ui.account.detail

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowId
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ngiu.MainActivity
import com.example.ngiu.R
import com.example.ngiu.databinding.FragmentAccountRecordsBinding
import com.example.ngiu.ui.activity.TransListAdapter
import kotlinx.android.synthetic.main.fragment_account.*
import kotlinx.android.synthetic.main.fragment_account_add_cash.*
import kotlinx.android.synthetic.main.fragment_account_p_r_detail.*
import kotlinx.android.synthetic.main.fragment_account_records.*
import kotlinx.android.synthetic.main.fragment_activity.*

class AccountGeneralDetailFragment : Fragment() {

    private lateinit var  accountPRDetailViewModel:AccountPRDetailViewModel
    private var _binding: FragmentAccountRecordsBinding? = null


    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private var accountPRDetailAdapter: AccountPRDetailAdapter? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        accountPRDetailViewModel =
            ViewModelProvider(this).get(AccountPRDetailViewModel::class.java)

        _binding = FragmentAccountRecordsBinding.inflate(inflater, container, false)

        // load data to ram
        Thread {
            accountPRDetailViewModel.loadDataToRam(requireContext(), 1L)
        }.start()

        initAdapter()


        return binding.root
    }

    private fun initAdapter() {
        Thread {
            this.activity?.runOnUiThread {

                recyclerview_account_pr_detail.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL,false)
                accountPRDetailAdapter = this.context?.let {
                    AccountPRDetailAdapter(object: AccountPRDetailAdapter.OnClickListener {
                        // catch the item click event from adapter
                        override fun onItemClick(transID: Long) {
                            // switch to record fragment (Edit mode)
                            navigateToRecordFragment(transID, true)
                        }
                    })
                }
                recyclerview_account_pr_detail.adapter = accountPRDetailAdapter
            }
        }.start()
    }


    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // set up toolbar icon and click event
        // choose items to show

        toolbar_account_normal_details.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        toolbar_account_normal_details.menu.findItem(R.id.action_edit).isVisible = true
        toolbar_account_normal_details.menu.findItem(R.id.action_add).isVisible = true

        // menu item clicked
        toolbar_account_normal_details.setOnMenuItemClickListener{
            when (it.itemId) {
                R.id.action_add -> {
                    // navigate to add record screen
                    navigateToRecordFragment()
                    true
                }
                R.id.action_edit -> {
                    (activity as MainActivity).setNavBottomBarVisibility(View.GONE)
                    // navigate to edit account
                    // todo swtich to edit account
                    true
                }

                else -> super.onOptionsItemSelected(it)
            }
        }


    }

    @SuppressLint("SetTextI18n")
    override fun onResume() {
        super.onResume()


        // load transaction list
        Thread {
            activity?.runOnUiThread {
                accountPRDetailAdapter?.setList(accountPRDetailViewModel.listPRDetail)
            }
        }.start()

        // show the info at title
        tv_account_pr_balance.text = "$" + "%.2f".format(accountPRDetailViewModel.accountBalance)
        tv_account_pr_lend_amount.text = "$" + "%.2f".format(accountPRDetailViewModel.lendAmount)
        tv_account_pr_receive_amount.text = "$" + "%.2f".format(accountPRDetailViewModel.receiveAmount)
        tv_account_pr_borrow_amount.text = "$" + "%.2f".format(accountPRDetailViewModel.borrowAmount)
        tv_account_pr_pay_amount.text = "$" + "%.2f".format(accountPRDetailViewModel.payAmount)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    private fun navigateToRecordFragment(transID: Long = 0, editMode: Boolean = false){
        // hide nav bottom bar
        //(activity as MainActivity).setNavBottomBarVisibility(View.GONE)

        //parentFragmentManager.setFragmentResult("requestKey", bundleOf("bundleKey" to transactionList[position].Transaction_ID))
        if (editMode) setFragmentResult("record_edit_mode", bundleOf("rID" to transID))
        // switch to record fragment
        findNavController().navigate(R.id.navigation_record)
    }
}

