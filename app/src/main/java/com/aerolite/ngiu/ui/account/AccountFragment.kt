package com.aerolite.ngiu.ui.account


import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aerolite.ngiu.MainActivity
import com.aerolite.ngiu.R
import com.aerolite.ngiu.databinding.FragmentAccountBinding
import com.aerolite.ngiu.functions.get2DigitFormat
//import kotlinx.android.synthetic.main.fragment_account.toolbar_account


class AccountFragment : Fragment() {

    private lateinit var accountViewModel: AccountViewModel
    private var _binding: FragmentAccountBinding? = null


    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private var rvAccount: RecyclerView? = null
    private var adapter: AccountSectionAdapter? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        accountViewModel =
            ViewModelProvider(this)[AccountViewModel::class.java]

        _binding = FragmentAccountBinding.inflate(inflater, container, false)
        rvAccount = binding.root.findViewById(R.id.rvAccount)

        // Show bottom bar
        (activity as MainActivity).setNavBottomBarVisibility(View.VISIBLE)


        return binding.root
    }


    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        accountViewModel.getAccountSectionUiModel(requireContext())
        rvAccount?.layoutManager = LinearLayoutManager(context)
        adapter = this.context?.let{
            AccountSectionAdapter(object: AccountSectionAdapter.OnClickListener {
                override fun onItemClick(AccountTypeID: Long, isExpanded: Boolean) {
                    // save the expanded status
                    accountViewModel.saveExpandedStatus(it, AccountTypeID, isExpanded)
                }
            })
        }
        rvAccount?.adapter = adapter

        val netAssets = accountViewModel.getNetAssets()
        val totalAsset = accountViewModel.getTotalAssets()
        val totalLiability = accountViewModel.getTotalLiability()

        binding.tvAccountTotalAssetsValue.text = get2DigitFormat(totalAsset)
        binding.tvAccountNetAssetsValue.text = get2DigitFormat(netAssets)

        accountViewModel.accountSections.observe(viewLifecycleOwner){
            binding.tvAccountTotalLiabilityValue.text = get2DigitFormat(totalLiability)
            // if there is nothing to display go to the add account
            //if(it.isEmpty()){
                //findNavController().navigate(R.id.navigation_add_account)
            //}
            adapter?.addItems(it)
        }


        // set up toolbar icon and click event
        // choose items to show
        binding.toolbarAccount.menu.findItem(R.id.action_add).isVisible = true

        // menu item clicked
        binding.toolbarAccount.setOnMenuItemClickListener{
            when (it.itemId) {
                R.id.action_add -> {
                    // hide nav bottom bar
                    (activity as MainActivity).setNavBottomBarVisibility(View.GONE)
                    // navigate to add record screen
                    view.findNavController().navigate(R.id.navigation_add_account)
                    true
                }

                else -> true
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
