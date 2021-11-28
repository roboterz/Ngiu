package com.example.ngiu.ui.account.addaccounts

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.ngiu.R
import com.example.ngiu.data.entities.Account
import com.example.ngiu.data.entities.Currency
import com.example.ngiu.databinding.FragmentAccountAddVirtualAccountBinding
import com.example.ngiu.functions.addDecimalLimiter
import com.google.android.material.textfield.TextInputEditText
import kotlinx.android.synthetic.main.fragment_account_add_virtual_account.*
import kotlinx.android.synthetic.main.popup_title.view.*



class AddVirtualAccountFragment : Fragment() {

    private var _binding: FragmentAccountAddVirtualAccountBinding? = null
    private val binding get() = _binding!!

    private lateinit var addVirtualAccountViewModel: AddVirtualAccountViewModel
    var currency = "USD"


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        addVirtualAccountViewModel = ViewModelProvider(this).get(AddVirtualAccountViewModel::class.java)
        _binding = FragmentAccountAddVirtualAccountBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListeners()
        getView()?.findViewById<TextInputEditText?>(R.id.tetVirtualBalance)?.addDecimalLimiter()

        toolbarAddVirtualAccount.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun initListeners() {
        binding.btnSaveVirtual.setOnClickListener {
            submitForm()
        }

        binding.btnVirtualAddOtherCurrency.setOnClickListener {

            val array : List<Currency> = addVirtualAccountViewModel.getCurrency(requireActivity())

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
            titleView.tv_popup_title_text.text = getText(R.string.option_title_add_currency)

            builder.setCustomTitle(titleView)

            // Set items form alert dialog
            builder.setItems(cs) { _, which ->
                currency = arrayList[which]
                binding.virtualBalanceTextLayout.suffixText = currency

            }

            // Create a new AlertDialog using builder object
            // Finally, display the alert dialog
            builder.create().show()
        }
    }

    private fun insertData() {
        val accountName = binding.tetVirtualAccountName.text.toString()
        val userID = binding.tetVirtualUserID.text.toString()
        val countInNetAsset: Boolean = binding.scVirtualCountNetAssets.isChecked
        val memo = binding.tetVirtualMemo.text.toString()
        val accountTypeID = 7L
        var balance = binding.tetVirtualBalance.text.toString()

        if (balance.isEmpty()) {
            balance = "0.0"
        }
        val cashAccount = Account(
            Account_Name =  accountName, Account_CardNumber = userID ,Account_Balance =  balance.toDouble(),
            Account_CountInNetAssets =  countInNetAsset, Account_Memo = memo, AccountType_ID = accountTypeID, Currency_ID = currency)


        addVirtualAccountViewModel.insertVirtualAccount(requireActivity(), cashAccount)


    }

    private fun submitForm() {
        binding.virtualAccountNameTextLayout.helperText = validAccountName()
        binding.virtualBalanceTextLayout.helperText = validBalance()

        val validAccountName = binding.virtualAccountNameTextLayout.helperText == null
        val validBalance = binding.virtualBalanceTextLayout.helperText == null

        if (validAccountName && validBalance ) {
            insertData()
            findNavController().navigate(R.id.navigation_account)

        }
        else
            invalidForm()
    }

    private fun invalidForm() {
        var message = ""
        if(binding.virtualAccountNameTextLayout.helperText != null) {
            message += "\nAccountName: " + binding.virtualAccountNameTextLayout.helperText
        }
        if(binding.virtualBalanceTextLayout.helperText != null) {
            message += "\n\nBalance: " + binding.virtualBalanceTextLayout.helperText
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
        val accountNameText = binding.tetVirtualAccountName.text.toString()
        if(accountNameText.length < 3) {
            return "Minimum of 3 Characters required."
        }
        return null
    }

    private fun validBalance(): String? {
        val accountBalanceText = binding.tetVirtualBalance.text.toString()
        if(accountBalanceText.length < 0) {
            return "Invalid Balance Entry."
        }
        return null
    }


}