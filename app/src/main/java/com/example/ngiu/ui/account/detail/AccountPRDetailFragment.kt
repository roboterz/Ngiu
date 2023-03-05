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
import com.example.ngiu.functions.*
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

        accountPRDetailViewModel.accountID = arguments?.getLong(KEY_ACCOUNT_ID)!!
        accountPRDetailViewModel.accountName = arguments?.getString(KEY_ACCOUNT_NAME)!!
        accountPRDetailViewModel.accountTypeID = arguments?.getLong(KEY_ACCOUNT_TYPE)!!

        // load data to ram
        accountPRDetailViewModel.loadDataToRam(requireContext())




        return binding.root
    }


    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initAdapter(view, this)


        // set up toolbar icon and click event
        // choose items to show

        toolbar_account_p_r_detail.setNavigationOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        toolbar_account_p_r_detail.menu.findItem(R.id.action_edit).isVisible = true
        toolbar_account_p_r_detail.menu.findItem(R.id.action_add).isVisible = true

        // menu item clicked
        toolbar_account_p_r_detail.setOnMenuItemClickListener{
            when (it.itemId) {
                R.id.action_add -> {
                    // navigate to add record screen
                    //navigateToRecordFragment(0, accountPRDetailViewModel.accountID, TRANSACTION_TYPE_DEBIT)
                    switchToRecordFragment(view, this,
                        0,
                        accountPRDetailViewModel.accountID,
                        TRANSACTION_TYPE_DEBIT)
                    true
                }
                R.id.action_edit -> {
                    (activity as MainActivity).setNavBottomBarVisibility(View.GONE)
                    // navigate to edit account

                    switchToAccountAttributePage(view, ACCOUNT_TYPE_RECEIVABLE,
                        accountPRDetailViewModel.accountID, 0.0,
                        EDIT_MODE)

                    true
                }

                else -> true
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



    private fun initAdapter(view: View, fragment: Fragment) {
        Thread {
            this.activity?.runOnUiThread {

                recyclerview_account_pr_detail.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL,false)
                accountPRDetailAdapter = this.context?.let {
                    AccountPRDetailAdapter(object: AccountPRDetailAdapter.OnClickListener {
                        // catch the item click event from adapter
                        override fun onItemClick(transID: Long) {
                            // switch to record fragment (Edit mode)
                            //navigateToRecordFragment(transID)
                            switchToRecordFragment(view, fragment,
                                transID,
                                accountPRDetailViewModel.accountID,
                                TRANSACTION_TYPE_DEBIT
                            )
                        }
                    })
                }
                recyclerview_account_pr_detail.adapter = accountPRDetailAdapter
            }
        }.start()
    }


    private fun navigateToRecordFragment(trans_ID: Long = 0, account_ID: Long = 0, transType_ID: Long = 0){
        val bundle = Bundle().apply {
            putLong(KEY_RECORD_TRANSACTION_ID, trans_ID)
            putLong(KEY_RECORD_ACCOUNT_ID, account_ID)
            putLong(KEY_RECORD_TRANSACTION_TYPE_ID, transType_ID)
        }
        // todo open record fragment with specified account or specified transaction type
        // switch to record fragment
        findNavController().navigate(R.id.navigation_record, bundle)
    }
}

