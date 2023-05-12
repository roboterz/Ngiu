package com.example.ngiu.ui.account.detail

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ngiu.MainActivity
import com.example.ngiu.R
import com.example.ngiu.databinding.FragmentAccountCreditDetailBinding
import com.example.ngiu.functions.*
import kotlinx.android.synthetic.main.fragment_account_credit_detail.*
import java.time.LocalDate
import java.time.format.DateTimeFormatter


class AccountCreditDetailFragment : Fragment() {


    private lateinit var  accountCreditDetailViewModel: AccountCreditDetailViewModel
    private var _binding: FragmentAccountCreditDetailBinding? = null


    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private var accountCDGAdapter: AccountCreditDetailGroupAdapter? = null

    private var accountID: Long = 0

    private val itemDateFormatter = DateTimeFormatter.ofPattern("MM/dd")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // hide bottom bar
        (activity as MainActivity).setNavBottomBarVisibility(View.GONE)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        accountCreditDetailViewModel = ViewModelProvider(this)[AccountCreditDetailViewModel::class.java]

        _binding = FragmentAccountCreditDetailBinding.inflate(inflater, container, false)


        // get data from other fragment
        accountID = arguments?.getLong(KEY_ACCOUNT_ID)!!

        accountCreditDetailViewModel.loadDataToRam(requireContext(), accountID)



        return binding.root
    }



    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // initial adapter
        initAdapter(view, this)



        tv_account_credit_detail_available_credit_limit_value.text = "$" + "%.2f".format(accountCreditDetailViewModel.availableCreditLimit)
        tv_account_credit_detail_current_arrears_value.text =  "$" + "%.2f".format(accountCreditDetailViewModel.currentArrears)

        if (accountCreditDetailViewModel.accountRecord.Account_PaymentDay > LocalDate.now().dayOfMonth){
            tv_account_credit_info_payment_day_value.text =
                LocalDate.now().plusDays(accountCreditDetailViewModel.accountRecord.Account_PaymentDay.toLong() - LocalDate.now().dayOfMonth).format(itemDateFormatter)
        }else{
            tv_account_credit_info_payment_day_value.text =
                LocalDate.of(LocalDate.now().year, LocalDate.now().month, accountCreditDetailViewModel.accountRecord.Account_PaymentDay)
                    .plusMonths(1).format(itemDateFormatter)
        }

        tv_account_credit_info_statement_day_value.text = accountCreditDetailViewModel.accountRecord.Account_StatementDay.toString() + getDayOfMonthSuffix(accountCreditDetailViewModel.accountRecord.Account_StatementDay)


        // set up toolbar icon and click event
        // choose items to show
        toolbar_account_credit_card_details.setNavigationOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        toolbar_account_credit_card_details.menu.findItem(R.id.action_edit).isVisible = true
        toolbar_account_credit_card_details.menu.findItem(R.id.action_add).isVisible = true
        toolbar_account_credit_card_details.title = accountCreditDetailViewModel.accountRecord.Account_Name //+ "(" + accountCreditDetailViewModel.accountRecord.Account_CardNumber + ")"

        // menu item clicked
        toolbar_account_credit_card_details.setOnMenuItemClickListener{
            when (it.itemId) {
                R.id.action_add -> {
                    // navigate to add record screen
                    //navigateToRecordFragment(0, accountID, TRANSACTION_TYPE_EXPENSE)
                    switchToRecordFragment(view, this, 0,
                        accountID,
                        TRANSACTION_TYPE_EXPENSE
                    )
                    true
                }
                R.id.action_edit -> {
                    //(activity as MainActivity).setNavBottomBarVisibility(View.GONE)

                    // Switch to Credit Account Attribute Page
                    switchToAccountAttributePage(view, ACCOUNT_TYPE_CREDIT,
                                                accountID, 0.0, EDIT_MODE)

                    true
                }

                else -> true
            }
        }

    }



    override fun onResume() {
        super.onResume()

        Thread {
            activity?.runOnUiThread {
                //vpAdapter?.stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
                accountCDGAdapter?.setAccountID(accountCreditDetailViewModel.accountRecord.Account_ID)
                accountCDGAdapter?.setStatementList(accountCreditDetailViewModel.listGroup)
                //recyclerView_activity.adapter = vpAdapter
            }
        }.start()


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    private fun initAdapter(view: View, fragment: Fragment) {

        Thread {
            activity?.runOnUiThread {
                // pass the value to fragment from adapter when item clicked
                accountCDGAdapter = AccountCreditDetailGroupAdapter(object: AccountCreditDetailGroupAdapter.OnClickListener {
                    // catch the item click event from adapter
                    override fun onItemClick(transID: Long) {
                        // switch to record fragment (Edit mode)
                        switchToRecordFragment(view, fragment, transID)
                    }
                })

                recyclerview_account_credit_detail.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL,false)

                recyclerview_account_credit_detail.adapter = accountCDGAdapter
            }
        }.start()
    }

/*    private fun navigateToRecordFragment(trans_ID: Long = 0, account_ID: Long = 0, transType_ID: Long = TRANSACTION_TYPE_EXPENSE){
        val bundle = Bundle().apply {
            putLong(KEY_RECORD_TRANSACTION_ID, trans_ID)
            putLong(KEY_RECORD_ACCOUNT_ID, account_ID)
            putLong(KEY_RECORD_TRANSACTION_TYPE_ID, transType_ID)
        }

        // switch to record fragment
        findNavController().navigate(R.id.navigation_record, bundle)
    }*/

}