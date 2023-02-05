package com.example.ngiu.ui.account.detail

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ngiu.MainActivity
import com.example.ngiu.R
import com.example.ngiu.databinding.FragmentAccountGeneralDetailBinding
import com.example.ngiu.functions.*
import kotlinx.android.synthetic.main.fragment_account_general_detail.*

class AccountGeneralDetailFragment : Fragment() {

    private lateinit var  accountGeneralDetailViewModel:AccountGeneralDetailViewModel
    private var _binding: FragmentAccountGeneralDetailBinding? = null


    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private var accountGeneralDetailAdapter: AccountGeneralDetailAdapter? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // hide bottom bar
        (activity as MainActivity).setNavBottomBarVisibility(View.GONE)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        accountGeneralDetailViewModel =
            ViewModelProvider(this).get(AccountGeneralDetailViewModel::class.java)

        _binding = FragmentAccountGeneralDetailBinding.inflate(inflater, container, false)

        // received from other fragment
        accountGeneralDetailViewModel.accountID = arguments?.getLong("accountId")!!
        accountGeneralDetailViewModel.accountName = arguments?.getString("accountName")!!
        accountGeneralDetailViewModel.accountTypeID = arguments?.getLong("accountType")!!


        // load data to ram
        //Thread {
            accountGeneralDetailViewModel.loadDataToRam(requireContext())
        //}.start()


        initAdapter()


        return binding.root
    }

    private fun initAdapter() {
        Thread {
            this.activity?.runOnUiThread {

                recyclerview_account_general_detail.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL,false)
                accountGeneralDetailAdapter = this.context?.let {
                    AccountGeneralDetailAdapter(object: AccountGeneralDetailAdapter.OnClickListener {
                        // catch the item click event from adapter
                        override fun onItemClick(transID: Long) {
                            // switch to record fragment (Edit mode)
                            navigateToRecordFragment(transID)
                        }
                    })
                }
                recyclerview_account_general_detail.adapter = accountGeneralDetailAdapter
            }
        }.start()
    }


    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // set up toolbar icon and click event
        // choose items to show

        toolbar_account_general_detail.setNavigationOnClickListener {
            //findNavController().popBackStack()
            requireActivity().onBackPressed()
        }

        toolbar_account_general_detail.menu.findItem(R.id.action_edit).isVisible = true
        toolbar_account_general_detail.menu.findItem(R.id.action_add).isVisible = true

        // menu item clicked
        toolbar_account_general_detail.setOnMenuItemClickListener{
            when (it.itemId) {
                R.id.action_add -> {
                    // navigate to add record screen
                    navigateToRecordFragment(0, accountGeneralDetailViewModel.accountID, TRANSACTION_TYPE_EXPENSE)
                    true
                }
                R.id.action_edit -> {
                    //(activity as MainActivity).setNavBottomBarVisibility(View.GONE)
                    // navigate to edit account
                    // swtich to edit account

                    when (accountGeneralDetailViewModel.accountTypeID) {
                        ACCOUNT_TYPE_CASH -> {
                            val bundle = Bundle().apply {
                                putString("page", "edit_cash")
                                putLong("id", accountGeneralDetailViewModel.accountID)
                                putDouble("balance", accountGeneralDetailViewModel.accountBalance)
                            }


                            view.findNavController().navigate(R.id.addCashFragment, bundle)
                        }
                        ACCOUNT_TYPE_DEBIT -> {
                            val bundle = Bundle().apply {
                                putString("page", "edit_debit")
                                putLong("id", accountGeneralDetailViewModel.accountID)
                                putDouble("balance", accountGeneralDetailViewModel.accountBalance)
                            }
                            view.findNavController().navigate(R.id.addDebitFragment, bundle)
                        }
                        ACCOUNT_TYPE_INVESTMENT-> {
                            val bundle = Bundle().apply {
                                putString("page", "edit_investment")
                                putLong("id", accountGeneralDetailViewModel.accountID)
                                putDouble("balance", accountGeneralDetailViewModel.accountBalance)
                            }
                            view.findNavController().navigate(R.id.addWebAccountFragment, bundle)
                        }
                        ACCOUNT_TYPE_WEB-> {
                            val bundle = Bundle().apply {
                                putString("page", "edit_web")
                                putLong("id", accountGeneralDetailViewModel.accountID)
                                putDouble("balance", accountGeneralDetailViewModel.accountBalance)
                            }
                            view.findNavController().navigate(R.id.addWebAccountFragment, bundle)
                        }
                        ACCOUNT_TYPE_STORED -> {
                            val bundle = Bundle().apply {
                                putString("page", "edit_valueCard")
                                putLong("id", accountGeneralDetailViewModel.accountID)
                                putDouble("balance", accountGeneralDetailViewModel.accountBalance)
                            }
                            view.findNavController().navigate(R.id.addPermanentAssetFragment, bundle)
                        }

                        ACCOUNT_TYPE_VIRTUAL-> {
                            val bundle = Bundle().apply {
                                putString("page", "edit_virtual")
                                putLong("id", accountGeneralDetailViewModel.accountID)
                                putDouble("balance", accountGeneralDetailViewModel.accountBalance)
                            }
                            view.findNavController().navigate(R.id.addWebAccountFragment, bundle)
                        }

                        ACCOUNT_TYPE_ASSETS -> {
                            val bundle = Bundle().apply {
                                putString("page", "edit_perm")
                                putLong("id", accountGeneralDetailViewModel.accountID)
                                putDouble("balance", accountGeneralDetailViewModel.accountBalance)
                            }
                            view.findNavController().navigate(R.id.addPermanentAssetFragment, bundle)
                        }


                        ACCOUNT_TYPE_RECEIVABLE -> {
                            val bundle = Bundle().apply {
                                putString("page", "edit_payable")
                                putLong("id", accountGeneralDetailViewModel.accountID)
                            }
                            view.findNavController().navigate(R.id.addCashFragment, bundle)
                        }
                    }
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
                accountGeneralDetailAdapter?.setTotalAccountBalance(accountGeneralDetailViewModel.accountID, accountGeneralDetailViewModel.accountBalance)
                accountGeneralDetailAdapter?.setList(accountGeneralDetailViewModel.listDetail)
            }
        }.start()

        // show the info at title
        tv_account_general_balance.text = "$" + "%.2f".format(accountGeneralDetailViewModel.accountBalance)
        tv_account_general_inflow_amount.text = "$" + "%.2f".format(accountGeneralDetailViewModel.inflowAmount)
        tv_account_general_outflow_amount.text = "$" + "%.2f".format(accountGeneralDetailViewModel.outflowAmount)
        tv_account_general_name.text = accountGeneralDetailViewModel.accountName

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    private fun navigateToRecordFragment(trans_ID: Long = 0, account_ID: Long = 0, transType_ID: Long = 0){
        val bundle = Bundle().apply {
            putLong("Transaction_ID", trans_ID)
            putLong("Account_ID", account_ID)
            putLong("TransactionType_ID", transType_ID)
        }
        // todo open record fragment with specified account or specified transaction type
        // switch to record fragment
        findNavController().navigate(R.id.navigation_record, bundle)
    }
}

