package com.example.ngiu.ui.account


import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ngiu.MainActivity
import com.example.ngiu.R
import com.example.ngiu.databinding.FragmentAccountBinding
import kotlinx.android.synthetic.main.fragment_account.toolbar_account



class AccountFragment : Fragment() {

    private lateinit var accountViewModel: AccountViewModel
    private var _binding: FragmentAccountBinding? = null


    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private var rvAccount: RecyclerView? = null
    private var adapter = AccountSectionAdapter()



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        accountViewModel =
            ViewModelProvider(this).get(AccountViewModel::class.java)

        _binding = FragmentAccountBinding.inflate(inflater, container, false)
        rvAccount = binding.root.findViewById(R.id.rvAccount)

        return binding.root
    }


    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        accountViewModel.getAccountSectionUiModel(requireContext())
        rvAccount?.layoutManager = LinearLayoutManager(context)
        rvAccount?.adapter = adapter

        val netAssets = accountViewModel.getNetAssets(requireContext())
        val totalAsset = accountViewModel.getTotalAssets(requireContext())
        val totalLiability = accountViewModel.getTotalLiability()

        binding.tvAccountTotalAssetsValue.text = "$"+"%.2f".format(totalAsset)
        binding.tvAccountNetAssetsValue.text = "$"+"%.2f".format(netAssets)

        accountViewModel.accountSections.observe(viewLifecycleOwner){
            binding.tvAccountTotalLiabilityValue.text = "$"+"%.2f".format(totalLiability)
            // if there is nothing to display go to the add account
            if(it.isEmpty()){
                findNavController().navigate(R.id.navigation_add_account)
            }
            adapter.addItems(it)
        }


        // set up toolbar icon and click event
        // choose items to show
        toolbar_account.menu.findItem(R.id.action_add).isVisible = true

        // menu item clicked
        toolbar_account.setOnMenuItemClickListener{
            when (it.itemId) {
                R.id.action_add -> {
                    // hide nav bottom bar
                    (activity as MainActivity).setNavBottomBarVisibility(View.GONE)
                    // navigate to add record screen
                    view.findNavController().navigate(R.id.navigation_add_account)
                    true
                }

                else -> super.onOptionsItemSelected(it)
            }
        }

    }

    override fun onResume() {
        super.onResume()

        // Show bottom bar
        (activity as MainActivity).setNavBottomBarVisibility(View.VISIBLE)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}
