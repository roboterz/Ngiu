package com.example.ngiu.ui.account

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ngiu.MainActivity
import com.example.ngiu.R
import com.example.ngiu.databinding.FragmentAccountCreditRecordsBinding
import com.example.ngiu.functions.getDayOfMonthSuffix
import com.example.ngiu.functions.toStatementDate
import kotlinx.android.synthetic.main.fragment_account_credit_records.*


class AccountCreditDetailFragment : Fragment() {


    private lateinit var  accountCreditDetailViewModel: AccountCreditDetailViewModel
    private var _binding: FragmentAccountCreditRecordsBinding? = null


    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private var rvAccount: RecyclerView? = null
    private var adapter = AccountDetailAdapter()

    private var itemId: Long = 0
    private lateinit var accountName: String
    private var balance: Double = 0.0
    private var creditLimit: Double = 0.0
    private var paymentDate: Int = 0
    private var statementDate: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        accountCreditDetailViewModel =
            ViewModelProvider(this).get(AccountCreditDetailViewModel::class.java)


        _binding = FragmentAccountCreditRecordsBinding.inflate(inflater, container, false)
        rvAccount = binding.root.findViewById(R.id.recyclerview_account_detail)

        fetchDataFromBundle()

        return binding.root
    }

    private fun fetchDataFromBundle() {
        itemId = arguments?.getLong("accountId")!!
        accountName = arguments?.getString("accountName")!!
        balance = arguments?.getDouble("balance")!!
        creditLimit = arguments?.getDouble("creditLimit")!!
        paymentDate = arguments?.getInt("paymentDate")!!
        statementDate = arguments?.getInt("statementDate")!!

    }


    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rvAccount?.layoutManager = LinearLayoutManager(context)
        rvAccount?.adapter = adapter


        binding.tvAccountDetailCurrentArrearsValue.text = balance.toString()
        binding.tvAccountInfoPaymentDayValue.text = toStatementDate(paymentDate)
        binding.tvAccountInfoStatementDayValue.text =
            "$statementDate${getDayOfMonthSuffix(statementDate)}"
        binding.tvAccountDetailAvailableCreditLimitValue.text = "${creditLimit + balance}"



        // set up toolbar icon and click event
        // choose items to show
        toolbar_account_credit_card_details.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        toolbar_account_credit_card_details.menu.findItem(R.id.action_edit).isVisible = true
        toolbar_account_credit_card_details.menu.findItem(R.id.action_add).isVisible = true

        // menu item clicked
        toolbar_account_credit_card_details.setOnMenuItemClickListener{
            when (it.itemId) {
                R.id.action_add -> {
                    // hide nav bottom bar
                    (activity as MainActivity).setNavBottomBarVisibility(View.GONE)
                    // navigate to add record screen
                    view.findNavController().navigate(R.id.navigation_record)
                    true
                }
                R.id.action_edit -> {
                    (activity as MainActivity).setNavBottomBarVisibility(View.GONE)

                        val bundle = Bundle().apply {
                            putString("page", "edit_credit")
                            putLong("id", itemId)
                        }
                        view.findNavController().navigate(R.id.addCreditFragment, bundle)

                    true
                }

                else -> super.onOptionsItemSelected(it)
            }
        }

    }

    override fun onResume() {
        super.onResume()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}