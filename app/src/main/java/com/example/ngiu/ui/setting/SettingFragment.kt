package com.example.ngiu.ui.setting

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
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
import com.example.ngiu.functions.*
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
            switchToCategoryManager(view, this, EDIT_MODE, TRANSACTION_TYPE_EXPENSE)
            //switchToCategoryManager(view,, EDIT_MODE, TRANSACTION_TYPE_EXPENSE)
        }

        // Income
        binding.tvSettingIncome.setOnClickListener {
            switchToCategoryManager(view, this, EDIT_MODE, TRANSACTION_TYPE_INCOME)
            //switchToCategoryManager(view,, EDIT_MODE, TRANSACTION_TYPE_INCOME)
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

        // Export Data to CSV
        binding.tvSettingExport.setOnClickListener{

            val i = CSVFile().exportToCSV(requireContext())

            if (i ==0 ){
                // completed
                Toast.makeText(context, getText(R.string.msg_completed), Toast.LENGTH_LONG).show()
            }else{
                // error
                Toast.makeText(context, getText(R.string.msg_error), Toast.LENGTH_LONG).show()
            }
        }

        // Import data from CSV
        binding.tvSettingImport.setOnClickListener{
            //CSVFile().importFromCSV(requireContext())
        }

        binding.tvSettingNgiu.setOnClickListener {
            // todo


        }


        /** click the navigation Icon in the left side of toolbar **/
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


    //********************* Private Function ******************************
/*    private fun openCategoryManager(transactionID: Long) {

        // Put Data Before switch
        setFragmentResult(
            KEY_CATEGORY_MANAGER, bundleOf(
            KEY_CATEGORY_MANAGER_MODE to EDIT_MODE,
            KEY_CATEGORY_MANAGER_TRANSACTION_TYPE to transactionID))

        // switch to category manage fragment
        findNavController().navigate(R.id.navigation_category_manage)
    }*/

    //********************* Private Function ******************************
}
