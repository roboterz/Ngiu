package com.aerolite.ngiu.ui.account.list

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aerolite.ngiu.MainActivity
import com.aerolite.ngiu.R
import com.aerolite.ngiu.databinding.FragmentAccountListBinding
import com.aerolite.ngiu.functions.KEY_ACCOUNT_LIST
import com.aerolite.ngiu.functions.KEY_ACCOUNT_LIST_CATEGORY_ID
import com.aerolite.ngiu.functions.KEY_ACCOUNT_LIST_EXCEPT_ID
import com.aerolite.ngiu.functions.KEY_ACCOUNT_LIST_MODE
import com.aerolite.ngiu.functions.KEY_ACCOUNT_LIST_TRANSACTION_TYPE_ID
import com.aerolite.ngiu.functions.KEY_RECORD_ACCOUNT_LIST
import com.aerolite.ngiu.functions.KEY_RECORD_PAY_ACCOUNT_NAME
import com.aerolite.ngiu.functions.KEY_RECORD_RECEIVE_ACCOUNT_NAME

//import kotlinx.android.synthetic.main.fragment_account.toolbar_account
//import kotlinx.android.synthetic.main.fragment_account_add_fixed_assets.fixedAssetsAccountNameTextLayout
//import kotlinx.android.synthetic.main.fragment_account_list.toolbar_account_list
//import kotlinx.android.synthetic.main.fragment_category_manage.toolbar_category


class AccountListFragment : Fragment(){
    private lateinit var accountListViewModel: AccountListViewModel
    private var _binding: FragmentAccountListBinding? = null


    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private var rvAccountList: RecyclerView? = null
    private var adapter: AccountListSectionAdapter? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setFragmentResultListener(KEY_ACCOUNT_LIST) { _, bundle ->
            // transaction type
            val receivedTransTypeID = bundle.getLong(KEY_ACCOUNT_LIST_TRANSACTION_TYPE_ID)
            // mode
            val receivedModeID = bundle.getInt(KEY_ACCOUNT_LIST_MODE)
            // except ID
            accountListViewModel.except_id = bundle.getLong(KEY_ACCOUNT_LIST_EXCEPT_ID)
            // category ID
            accountListViewModel.cate_id = bundle.getLong(KEY_ACCOUNT_LIST_CATEGORY_ID)


            // transaction type
            if (receivedTransTypeID > 0) {
                accountListViewModel.trans_type_id = receivedTransTypeID
            }

            // mode
            if (receivedModeID == 1) {
                accountListViewModel.bln_acct_out = true
            }else if (receivedModeID == 2) {
                accountListViewModel.bln_acct_out = false
            }

            // set account list
            accountListViewModel.setAccountSectionUiModel(
                requireContext(),
                accountListViewModel.trans_type_id,
                accountListViewModel.cate_id,
                accountListViewModel.except_id,
                accountListViewModel.bln_acct_out
            )

        }

    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        accountListViewModel =
            ViewModelProvider(this)[AccountListViewModel::class.java]

        _binding = FragmentAccountListBinding.inflate(inflater, container, false)
        rvAccountList = binding.root.findViewById(R.id.rv_account_list)

        // Hide bottom bar
        (activity as MainActivity).setNavBottomBarVisibility(View.GONE)


        return binding.root
    }


    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)






        // click the navigation Icon in the left side of toolbar
        binding.toolbarAccountList.setNavigationOnClickListener {

            // pass the string back to record fragment
            backToRecordFragment()

        }


    }

    override fun onResume() {
        super.onResume()

        rvAccountList?.layoutManager = LinearLayoutManager(context)

        adapter = this.context?.let{
            AccountListSectionAdapter(object: AccountListSectionAdapter.OnClickListener {
                override fun onItemClick(AccountName: String) {
                    // back to record fragment
                    backToRecordFragment(AccountName, accountListViewModel.bln_acct_out)
                }
            })
        }
        rvAccountList?.adapter = adapter



        accountListViewModel.accountListSections.observe(viewLifecycleOwner){
            // if there is nothing to display go to the add account
            //if(it.isEmpty()){
            //findNavController().navigate(R.id.navigation_add_account)
            //}
            adapter?.addItems(it)
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }



    private fun backToRecordFragment(acctName: String ="", payAccount: Boolean = true){
        // pass the string back to record fragment
        setFragmentResult(
            KEY_RECORD_ACCOUNT_LIST, bundleOf(
                KEY_RECORD_PAY_ACCOUNT_NAME to if (payAccount) acctName else "",
                KEY_RECORD_RECEIVE_ACCOUNT_NAME to if (payAccount) "" else acctName
            )
        )
        requireActivity().onBackPressedDispatcher.onBackPressed()
    }
}