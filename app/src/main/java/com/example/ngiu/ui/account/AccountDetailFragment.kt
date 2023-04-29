package com.example.ngiu.ui.account

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ngiu.MainActivity
import com.example.ngiu.R
import com.example.ngiu.databinding.FragmentAccountRecordsBinding
import com.example.ngiu.functions.*
import com.example.ngiu.functions.changeColor
import kotlinx.android.synthetic.main.fragment_account_records.*


class AccountDetailFragment : Fragment() {

    private lateinit var  accountDetailViewModel:AccountDetailViewModel
    private var _binding: FragmentAccountRecordsBinding? = null


    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private var rvAccount: RecyclerView? = null
    private var adapter = AccountDetailAdapter()

    private var itemId: Long = 0
    private var accountType: Long = 0
    private lateinit var accountName: String
    private var balance: Double = 0.0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        accountDetailViewModel =
            ViewModelProvider(this).get(AccountDetailViewModel::class.java)


        _binding = FragmentAccountRecordsBinding.inflate(inflater, container, false)
        rvAccount = binding.root.findViewById(R.id.rvAccountNormalDetails)


        fetchDataFromBundle()


        return binding.root
    }

    private fun fetchDataFromBundle() {
        itemId = arguments?.getLong(KEY_ACCOUNT_ID)!!
        accountName = arguments?.getString(KEY_ACCOUNT_NAME)!!
        //balance = arguments?.getDouble(KEY_ACCOUNT_BALANCE)!!
            balance = accountDetailViewModel.calculateBalance(requireContext(), itemId)
        accountType = arguments?.getLong(KEY_ACCOUNT_TYPE)!!
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        accountDetailViewModel.getTransRecords(requireContext(),itemId, balance)
        rvAccount?.layoutManager = LinearLayoutManager(context)
        rvAccount?.adapter = adapter

        binding.tvInflowValue.text = accountDetailViewModel.getInflow(requireContext(),itemId)
        binding.tvOutflowValue.text = accountDetailViewModel.getOutflow(requireContext(),itemId)
        binding.tvAccountRecordName.text = accountName
        changeColor(tvAccountRecordBalance,balance )


            accountDetailViewModel.accountRecordsList.observe(viewLifecycleOwner){
                adapter.addItems(it)
        }

        // set up toolbar icon and click event
        // choose items to show

        toolbar_account_normal_details.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
        binding.toolbarAccountNormalDetails.title = getString(R.string.nav_title_account_detail)
        toolbar_account_normal_details.menu.findItem(R.id.action_edit).isVisible = true
        toolbar_account_normal_details.menu.findItem(R.id.action_add).isVisible = true

        // menu item clicked
        // menu item clicked
        toolbar_account_normal_details.setOnMenuItemClickListener{
            when (it.itemId) {
                R.id.action_add -> {
                    // hide nav bottom bar
                    (activity as MainActivity).setNavBottomBarVisibility(View.GONE)
                    // navigate to add record screen
                    //view.findNavController().navigate(R.id.navigation_record)
                    switchToRecordFragment(view, this)
                    true
                }
                R.id.action_edit -> {
                    (activity as MainActivity).setNavBottomBarVisibility(View.GONE)
                    // navigate to edit account attribute
                    switchToAccountAttributePage(view, accountType, itemId, balance,
                                                EDIT_MODE)

                    true
                }

                else -> true
            }
        }


    }

    override fun onResume() {
        super.onResume()
        (activity as MainActivity).setNavBottomBarVisibility(View.VISIBLE)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}

