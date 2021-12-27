package com.example.ngiu.ui.account.detail

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ngiu.MainActivity
import com.example.ngiu.R
import com.example.ngiu.databinding.FragmentAccountPRDetailBinding
import kotlinx.android.synthetic.main.fragment_account_p_r_detail.*
import kotlin.math.abs

class AccountPRDetailFragment : Fragment() {

    private lateinit var  accountPRDetailViewModel:AccountPRDetailViewModel
    private var _binding: FragmentAccountPRDetailBinding? = null


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

        _binding = FragmentAccountPRDetailBinding.inflate(inflater, container, false)

        // hide nav bottom bar
        (activity as MainActivity).setNavBottomBarVisibility(View.GONE)

        accountPRDetailViewModel.accountID = arguments?.getLong("accountId")!!
        accountPRDetailViewModel.accountName = arguments?.getString("accountName")!!
        accountPRDetailViewModel.accountTypeID = arguments?.getLong("accountType")!!

        // load data to ram
        accountPRDetailViewModel.loadDataToRam(requireContext())

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
                            navigateToRecordFragment(transID)
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

        toolbar_account_p_r_detail.setNavigationOnClickListener {
            requireActivity().onBackPressed()
        }

        toolbar_account_p_r_detail.menu.findItem(R.id.action_edit).isVisible = true
        toolbar_account_p_r_detail.menu.findItem(R.id.action_add).isVisible = true

        // menu item clicked
        toolbar_account_p_r_detail.setOnMenuItemClickListener{
            when (it.itemId) {
                R.id.action_add -> {
                    // navigate to add record screen
                    navigateToRecordFragment()
                    true
                }
                R.id.action_edit -> {
                    (activity as MainActivity).setNavBottomBarVisibility(View.GONE)
                    // navigate to edit account
                    val bundle = Bundle().apply {
                        putString("page", "edit_payable")
                        putLong("id", accountPRDetailViewModel.accountID)
                    }
                    view.findNavController().navigate(R.id.addCashFragment, bundle)

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
        if (accountPRDetailViewModel.accountBalance < 0){
            tv_account_pr_owe.text = getString(R.string.option_account_owe_he)
            tv_account_pr_balance.text = "$" + "%.2f".format(abs(accountPRDetailViewModel.accountBalance))
            tv_account_pr_balance.setTextColor(ContextCompat.getColor(requireContext(), R.color.app_expense_amount))
        }else{
            tv_account_pr_owe.text = getString(R.string.option_account_owe_you)
            tv_account_pr_balance.text = "$" + "%.2f".format(accountPRDetailViewModel.accountBalance)
            tv_account_pr_balance.setTextColor(ContextCompat.getColor(requireContext(), R.color.app_income_amount))
        }

        tv_account_pr_lend_amount.text = "$" + "%.2f".format(accountPRDetailViewModel.lendAmount)
        tv_account_pr_receive_amount.text = "$" + "%.2f".format(accountPRDetailViewModel.receiveAmount)
        tv_account_pr_borrow_amount.text = "$" + "%.2f".format(accountPRDetailViewModel.borrowAmount)
        tv_account_pr_pay_amount.text = "$" + "%.2f".format(accountPRDetailViewModel.payAmount)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    private fun navigateToRecordFragment(transID: Long = 0){

        val bundle = Bundle().apply {
            putLong("Transaction_ID", transID)
        }
        // switch to record fragment
        findNavController().navigate(R.id.navigation_record, bundle)
    }
}

