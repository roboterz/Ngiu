package com.aerolite.ngiu.ui.account.addaccounts

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.aerolite.ngiu.functions.ACCOUNT_TYPE_ASSETS
import com.aerolite.ngiu.functions.ACCOUNT_TYPE_STORED
import com.aerolite.ngiu.functions.KEY_ACCOUNT_ID
import com.aerolite.ngiu.functions.KEY_ACCOUNT_PAGE
import com.aerolite.ngiu.functions.KEY_VALUE_ACCOUNT_ADD_ASSETS
import com.aerolite.ngiu.functions.KEY_VALUE_ACCOUNT_ADD_STORED
import com.aerolite.ngiu.functions.KEY_VALUE_ACCOUNT_EDIT_ASSETS
import com.aerolite.ngiu.functions.KEY_VALUE_ACCOUNT_EDIT_STORED
import com.aerolite.ngiu.MainActivity
import com.aerolite.ngiu.R
import com.aerolite.ngiu.data.entities.Account
import com.aerolite.ngiu.data.entities.Currency
import com.aerolite.ngiu.databinding.FragmentAccountAddFixedAssetsBinding
import com.aerolite.ngiu.functions.addDecimalLimiter
import com.google.android.material.textfield.TextInputEditText
//import kotlinx.android.synthetic.main.fragment_account_add_fixed_assets.*
//import kotlinx.android.synthetic.main.popup_title.view.*


class AddFixedAssetsFragment : Fragment() {

    private var _binding: FragmentAccountAddFixedAssetsBinding? = null
    private val binding get() = _binding!!
    private lateinit var addCashViewModel: AddCashViewModel
    var currency = "USD"
    var page: Long = 0L
    var accountTypeID : Long = 0L
    var id: Long = 0L

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        addCashViewModel = ViewModelProvider(this).get(AddCashViewModel::class.java)
        _binding = FragmentAccountAddFixedAssetsBinding.inflate(inflater, container, false)
        getBundleData()
        displayPage()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListeners()
        getView()?.findViewById<TextInputEditText?>(R.id.tetFixedAssetsValue)?.addDecimalLimiter()

        binding.toolbarAddFixedAssets.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        (activity as MainActivity).setNavBottomBarVisibility(View.GONE)
    }

    private fun displayPage() {
        when (page) {
            KEY_VALUE_ACCOUNT_ADD_STORED -> {
                binding.toolbarAddFixedAssets.title = "Add Store Value Card"
                accountTypeID = ACCOUNT_TYPE_STORED
            }
            KEY_VALUE_ACCOUNT_EDIT_STORED -> {
                binding.toolbarAddFixedAssets.title = "Add Store Value Card"
                binding.toolbarAddFixedAssets.menu.findItem(R.id.action_delete).isVisible = true
                accountTypeID = ACCOUNT_TYPE_STORED
                id = arguments?.getLong(KEY_ACCOUNT_ID)!!
                fetchAccountDetails(id)
            }
            KEY_VALUE_ACCOUNT_ADD_ASSETS -> {
                binding.toolbarAddFixedAssets.title = "Add Fixed Account"
                accountTypeID = ACCOUNT_TYPE_ASSETS
            }
            KEY_VALUE_ACCOUNT_EDIT_ASSETS -> {
                binding.toolbarAddFixedAssets.title = "Edit Fixed Account"
                binding.toolbarAddFixedAssets.menu.findItem(R.id.action_delete).isVisible = true
                accountTypeID = ACCOUNT_TYPE_ASSETS
                id = arguments?.getLong(KEY_ACCOUNT_ID)!!
                fetchAccountDetails(id)
            }



        }
    }

    private fun getBundleData() {
        page = arguments?.getLong(KEY_ACCOUNT_PAGE)!!

    }


    private fun fetchAccountDetails(id: Long) {
        val account = addCashViewModel.getAccountByID(requireContext(), id)
        binding.tetFixedAssetsAccountName.setText(account.Account_Name)
        binding.tetFixedAssetsValue.setText(account.Account_Balance.toString())
        binding.tetFixedAssetsMemo.setText(account.Account_Memo)
        binding.scFixedAssetsCountNetAssets.isChecked = account.Account_CountInNetAssets



    }


    private fun initListeners() {
        binding.btnSaveFixedAssets.setOnClickListener {
            when (page) {
                KEY_VALUE_ACCOUNT_ADD_STORED -> {
                    submitForm()
                }
                KEY_VALUE_ACCOUNT_EDIT_STORED -> {
                    id = arguments?.getLong(KEY_ACCOUNT_ID)!!
                    updateAccount(id)
                }
                KEY_VALUE_ACCOUNT_ADD_ASSETS -> {
                    submitForm()
                }
                KEY_VALUE_ACCOUNT_EDIT_ASSETS -> {

                    id = arguments?.getLong(KEY_ACCOUNT_ID)!!
                    updateAccount(id)
                }

            }
        }

        binding.toolbarAddFixedAssets.setOnMenuItemClickListener{
            when (it.itemId) {
                R.id.action_delete -> {
                    // navigate to add record screen
                    addCashViewModel.deleteAccount(requireContext(),id)
                    findNavController().navigate(R.id.navigation_account)
                    Toast.makeText(requireContext(), "Successfully Deleted Your Account", Toast.LENGTH_LONG).show()
                    true
                }


                else -> true
            }
        }

        binding.btnFixedAssetsAddOtherCurrency.setOnClickListener {

            val array : List<Currency> = addCashViewModel.getCurrency(requireActivity())

            val arrayList: ArrayList<String> = ArrayList()

            for (item in array){
                arrayList.add(item.Currency_ID)
            }

            val cs: Array<CharSequence> = arrayList.toArray(arrayOfNulls<CharSequence>(arrayList.size))

            //   val array = arrayof()
            // Initialize a new instance of alert dialog builder object
            val builder = AlertDialog.Builder(requireContext())

            // set Title Style
            val titleView = layoutInflater.inflate(R.layout.popup_title,null)
            // set Title Text
            titleView.findViewById<TextView>(R.id.tv_popup_title_text).text = getText(R.string.option_title_add_currency)

            builder.setCustomTitle(titleView)

            // Set items form alert dialog
            builder.setItems(cs) { _, which ->
                currency = arrayList.get(which)
                binding.fixedAssetsBalanceTextLayout.suffixText = currency

            }

            // Create a new AlertDialog using builder object
            // Finally, display the alert dialog
            builder.create().show()
        }
    }

    private fun updateAccount(id: Long) {

        val account = Account(
            Account_ID = id,
            Account_Name = binding.tetFixedAssetsAccountName.text.toString(),
            Account_Balance = binding.tetFixedAssetsValue.text.toString().toDouble(),
            Account_CountInNetAssets = binding.scFixedAssetsCountNetAssets.isChecked,
            Account_Memo = binding.tetFixedAssetsMemo.text.toString(),
            AccountType_ID = accountTypeID,
            Currency_ID = currency
        )
        addCashViewModel.getAllAccount(requireContext()).forEach { item ->
            if (item.Account_Name.equals(binding.tetFixedAssetsAccountName.text.toString(), ignoreCase = true)  && (account.Account_ID != item.Account_ID)) {
                Toast.makeText(requireContext(), "Account Name already exist", Toast.LENGTH_LONG).show()
                return
            }
        }
        addCashViewModel.updateAccount(requireContext(), account)
        findNavController().popBackStack()
        Toast.makeText(requireContext(), "Update Successful", Toast.LENGTH_LONG).show()

    }

    private fun insertData() {
        val accountName = binding.tetFixedAssetsAccountName.text.toString()
        val countInNetAsset: Boolean = binding.scFixedAssetsCountNetAssets.isChecked
        val memo = binding.tetFixedAssetsMemo.text.toString()
        val accountTypeID = accountTypeID
        var value = binding.tetFixedAssetsValue.text.toString()

        if (value.isEmpty()) {
            value = "0.0"
        }
        val cashAccount = Account(
            Account_Name =  accountName, Account_Balance =  value.toDouble(),
            Account_CountInNetAssets =  countInNetAsset, Account_Memo = memo, AccountType_ID = accountTypeID, Currency_ID = currency)


        addCashViewModel.insertCash(requireActivity(), cashAccount)
        Toast.makeText(requireContext(), "Successfully added your account",Toast.LENGTH_LONG).show()

    }

    private fun submitForm() {
        binding.fixedAssetsAccountNameTextLayout.helperText = validAccountName()


        val validAccountName = binding.fixedAssetsAccountNameTextLayout.helperText == null


        if (validAccountName  ) {
            insertData()
            findNavController().popBackStack()

        }
        else
            invalidForm()
    }

    private fun invalidForm() {
        var message = ""
        if(binding.fixedAssetsAccountNameTextLayout.helperText != null) {
            message += "\nAccountName: " + binding.fixedAssetsAccountNameTextLayout.helperText
        }

        AlertDialog.Builder(context)
            .setTitle("Invalid Form")
            .setMessage(message)
            .setPositiveButton("Okay"){ _,_ ->
                // do nothing
            }
            .show()
    }


    private fun validAccountName(): String? {
        val accountNameText = binding.tetFixedAssetsAccountName.text.toString()
        if(accountNameText.length < 3) {
            return "Minimum of 3 Characters required."
        }
        return null
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }




}