package com.example.ngiu.ui.setting

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI.navigateUp
import com.example.ngiu.MainActivity
import com.example.ngiu.R
import com.example.ngiu.databinding.FragmentSettingBinding
import com.example.ngiu.functions.MPP_MERCHANT
import com.example.ngiu.functions.MPP_PERSON
import com.example.ngiu.functions.MPP_PROJECT
import kotlinx.android.synthetic.main.fragment_record.*
import kotlinx.android.synthetic.main.fragment_setting.*

class SettingFragment : Fragment() {

    private lateinit var settingViewModel: SettingViewModel
    private var _binding: FragmentSettingBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        settingViewModel =
            ViewModelProvider(this).get(SettingViewModel::class.java)

        _binding = FragmentSettingBinding.inflate(inflater, container, false)

        // Show bottom bar
        (activity as MainActivity).setNavBottomBarVisibility(View.VISIBLE)
        

        //todo on click to the next screen
        val textView: TextView = binding.tvSettingExpense
        textView.setOnClickListener {

        }
     //   findNavController().navigate(R.id.action_navigation_add_account_to_blankFragment)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Expense
        binding.tvSettingExpense.setOnClickListener {
            setFragmentResult("category_manage_mode", bundleOf("edit_mode" to true))
            setFragmentResult("category_manage_type", bundleOf("transaction_type" to 1L))
            // switch to category manage fragment
            findNavController().navigate(R.id.navigation_category_manage)
        }

        // Income
        binding.tvSettingIncome.setOnClickListener {

            setFragmentResult("category_manage_mode", bundleOf("edit_mode" to true))
            setFragmentResult("category_manage_type", bundleOf("transaction_type" to 2L))
            // switch to category manage fragment
            findNavController().navigate(R.id.navigation_category_manage)
        }

        // Merchant
        binding.tvSettingMerchant.setOnClickListener {

            setFragmentResult("mpp_type", bundleOf("type_ID" to MPP_MERCHANT))
            // switch to category manage fragment
            findNavController().navigate(R.id.navigation_mpp_manage)
        }

        // Person
        binding.tvSettingPerson.setOnClickListener {

            setFragmentResult("mpp_type", bundleOf("type_ID" to MPP_PERSON))
            // switch to category manage fragment
            findNavController().navigate(R.id.navigation_mpp_manage)
        }

        // Project
        binding.tvSettingProject.setOnClickListener {

            setFragmentResult("mpp_type", bundleOf("type_ID" to MPP_PROJECT))
            // switch to category manage fragment
            findNavController().navigate(R.id.navigation_mpp_manage)
        }

        binding.tvSettingNgiu.setOnClickListener {
            // todo

        }


        // click the navigation Icon in the left side of toolbar
        toolbar_setting.setNavigationOnClickListener {
            // call back button event to switch to previous fragment

            //requireActivity().onBackPressed()
            NavHostFragment.findNavController(this).navigateUp()
        }

    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
