package com.aerolite.ngiu.ui.account.detail

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aerolite.ngiu.functions.EDIT_MODE
import com.aerolite.ngiu.functions.KEY_ACCOUNT_ID
import com.aerolite.ngiu.functions.KEY_ACCOUNT_NAME
import com.aerolite.ngiu.functions.KEY_ACCOUNT_TYPE
import com.aerolite.ngiu.functions.TRANSACTION_TYPE_EXPENSE
import com.aerolite.ngiu.functions.get2DigitFormat
import com.aerolite.ngiu.functions.switchToAccountAttributePage2
import com.aerolite.ngiu.functions.switchToRecordFragment
import com.aerolite.ngiu.MainActivity
import com.aerolite.ngiu.R
import com.aerolite.ngiu.databinding.FragmentAccountGeneralDetailBinding
import com.aerolite.ngiu.functions.*
//import kotlinx.android.synthetic.main.fragment_account_general_detail.*

class AccountGeneralDetailFragment : Fragment() {

    private lateinit var  accountGeneralDetailViewModel: AccountGeneralDetailViewModel
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
        accountGeneralDetailViewModel.accountID = arguments?.getLong(KEY_ACCOUNT_ID)!!
        accountGeneralDetailViewModel.accountName = arguments?.getString(KEY_ACCOUNT_NAME)!!
        accountGeneralDetailViewModel.accountTypeID = arguments?.getLong(KEY_ACCOUNT_TYPE)!!


        // load data to ram
        //Thread {
            accountGeneralDetailViewModel.loadDataToRam(requireContext())
        //}.start()





        return binding.root
    }


    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        initAdapter(view, this)


        // set up toolbar icon and click event
        // choose items to show

        binding.toolbarAccountGeneralDetail.setNavigationOnClickListener {
            //findNavController().popBackStack()
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        binding.toolbarAccountGeneralDetail.menu.findItem(R.id.action_edit).isVisible = true
        binding.toolbarAccountGeneralDetail.menu.findItem(R.id.action_add).isVisible = true

        // menu item clicked
        binding.toolbarAccountGeneralDetail.setOnMenuItemClickListener{
            when (it.itemId) {
                R.id.action_add -> {
                    // navigate to add record screen
                    //navigateToRecordFragment(0, accountGeneralDetailViewModel.accountID, TRANSACTION_TYPE_EXPENSE)
                    switchToRecordFragment(view, this, 0,
                        accountGeneralDetailViewModel.accountID,
                        TRANSACTION_TYPE_EXPENSE
                    )
                    true
                }
                R.id.action_edit -> {
                    //(activity as MainActivity).setNavBottomBarVisibility(View.GONE)

                    // switch to edit account attribute page
                    switchToAccountAttributePage2(view,
                        accountGeneralDetailViewModel.accountTypeID,
                        accountGeneralDetailViewModel.accountID,
                        EDIT_MODE
                    )

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
                accountGeneralDetailAdapter?.setList(accountGeneralDetailViewModel.listDetail, accountGeneralDetailViewModel.listBalance, accountGeneralDetailViewModel.accountID)
            }
        }.start()

        // show the info at title
        binding.tvAccountGeneralBalance.text = get2DigitFormat(accountGeneralDetailViewModel.accountBalance)
        binding.tvAccountGeneralInflowAmount.text = get2DigitFormat(accountGeneralDetailViewModel.inflowAmount)
        binding.tvAccountGeneralOutflowAmount.text = get2DigitFormat(accountGeneralDetailViewModel.outflowAmount)
        binding.tvAccountGeneralName.text = accountGeneralDetailViewModel.accountName

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    @SuppressLint("CutPasteId")
    private fun initAdapter(view:View, fragment: Fragment) {
        Thread {
            this.activity?.runOnUiThread {

                binding.recyclerviewAccountGeneralDetail.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL,false)
                accountGeneralDetailAdapter = this.context?.let {
                    AccountGeneralDetailAdapter(object: AccountGeneralDetailAdapter.OnClickListener {
                        // catch the item click event from adapter
                        override fun onItemClick(transID: Long) {
                            // switch to record fragment (Edit mode)
                            //navigateToRecordFragment(transID)
                            switchToRecordFragment(view, fragment, transID,
                                accountGeneralDetailViewModel.accountID,
                                TRANSACTION_TYPE_EXPENSE
                            )
                        }
                    })
                }
                binding.recyclerviewAccountGeneralDetail.adapter = accountGeneralDetailAdapter
            }
        }.start()
    }


/*    private fun navigateToRecordFragment(trans_ID: Long = 0, account_ID: Long = 0, transType_ID: Long = 0){
        val bundle = Bundle().apply {
            putLong(KEY_RECORD_TRANSACTION_ID, trans_ID)
            putLong(KEY_RECORD_ACCOUNT_ID, account_ID)
            putLong(KEY_RECORD_TRANSACTION_TYPE_ID, transType_ID)
        }
        // switch to record fragment
        findNavController().navigate(R.id.navigation_record, bundle)
    }*/
}

