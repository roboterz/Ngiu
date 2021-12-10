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
import com.example.ngiu.databinding.FragmentAccountGeneralDetailBinding
import com.example.ngiu.ui.activity.TransListAdapter
import kotlinx.android.synthetic.main.fragment_account.*
import kotlinx.android.synthetic.main.fragment_account_add_cash.*
import kotlinx.android.synthetic.main.fragment_account_general_detail.*
import kotlinx.android.synthetic.main.fragment_account_p_r_detail.*
import kotlinx.android.synthetic.main.fragment_account_records.*
import kotlinx.android.synthetic.main.fragment_activity.*

class AccountGeneralDetailFragment : Fragment() {

    private lateinit var  accountGeneralDetailViewModel:AccountGeneralDetailViewModel
    private var _binding: FragmentAccountGeneralDetailBinding? = null


    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private var accountGeneralDetailAdapter: AccountGeneralDetailAdapter? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        accountGeneralDetailViewModel =
            ViewModelProvider(this).get(AccountGeneralDetailViewModel::class.java)

        _binding = FragmentAccountGeneralDetailBinding.inflate(inflater, container, false)

        // load data to ram
        //Thread {
            accountGeneralDetailViewModel.loadDataToRam(requireContext(), 3L)
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
                            navigateToRecordFragment(transID, true)
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
                accountGeneralDetailAdapter?.setTotalAccountBalance(accountGeneralDetailViewModel.accountID, accountGeneralDetailViewModel.accountBalance)
                accountGeneralDetailAdapter?.setList(accountGeneralDetailViewModel.listDetail)
            }
        }.start()

        // show the info at title
        tv_account_general_balance.text = "$" + "%.2f".format(accountGeneralDetailViewModel.accountBalance)
        //tv_account_pr_lend_amount.text = "$" + "%.2f".format(accountGeneralDetailViewModel.lendAmount)
        //tv_account_pr_receive_amount.text = "$" + "%.2f".format(accountGeneralDetailViewModel.receiveAmount)
        //tv_account_pr_borrow_amount.text = "$" + "%.2f".format(accountGeneralDetailViewModel.borrowAmount)
        //tv_account_pr_pay_amount.text = "$" + "%.2f".format(accountGeneralDetailViewModel.payAmount)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    private fun navigateToRecordFragment(transID: Long = 0, editMode: Boolean = false){
        // hide nav bottom bar
        //(activity as MainActivity).setNavBottomBarVisibility(View.GONE)

        //parentFragmentManager.setFragmentResult("requestKey", bundleOf("bundleKey" to transactionList[position].Transaction_ID))
        //if (editMode) setFragmentResult("record_edit_mode", bundleOf("rID" to transID))
        // switch to record fragment
        findNavController().navigate(R.id.navigation_record)
    }
}

