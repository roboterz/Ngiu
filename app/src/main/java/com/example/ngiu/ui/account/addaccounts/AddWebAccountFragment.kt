package com.example.ngiu.ui.account.addaccounts

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.ngiu.MainActivity
import com.example.ngiu.R
import com.example.ngiu.data.entities.Account
import com.example.ngiu.data.entities.Currency
import com.example.ngiu.databinding.FragmentAccountAddWebAccountBinding
import com.example.ngiu.functions.addDecimalLimiter
import com.google.android.material.textfield.TextInputEditText
import kotlinx.android.synthetic.main.fragment_account_add_web_account.*
import kotlinx.android.synthetic.main.popup_title.view.*



class AddWebAccountFragment : Fragment() {
    private var _binding: FragmentAccountAddWebAccountBinding? = null
    private val binding get() = _binding!!
    private lateinit var addWebAccountViewModel: AddWebAccountViewModel
    var currency = "USD"



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        addWebAccountViewModel = ViewModelProvider(this).get(AddWebAccountViewModel::class.java)
        _binding = FragmentAccountAddWebAccountBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListeners()
        getView()?.findViewById<TextInputEditText?>(R.id.tetWebBalance)?.addDecimalLimiter()

        toolbarAddWebAccount.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        (activity as MainActivity).setNavBottomBarVisibility(View.GONE)
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun initListeners() {
        binding.btnSaveWeb.setOnClickListener {
            submitForm()
        }

        binding.btnWebAddOtherCurrency.setOnClickListener {

            val array : List<Currency> = addWebAccountViewModel.getCurrency(requireActivity())

            val arrayList: ArrayList<String> = ArrayList()

            for (item in array){
                arrayList.add(item.Currency_ID)
            }

            val cs: Array<CharSequence> = arrayList.toArray(arrayOfNulls<CharSequence>(arrayList.size))

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
                binding.webBalanceTextLayout.suffixText = currency

            }

            // Create a new AlertDialog using builder object
            // Finally, display the alert dialog
            builder.create().show()
        }
    }

    private fun insertData() {
        val accountName = binding.tetWebAccountName.text.toString()
        val userID = binding.tetWebUserID.text.toString()
        val countInNetAsset: Boolean = binding.scWebCountNetAssets.isChecked
        val memo = binding.tetWebMemo.text.toString()
        val accountTypeID = 5L
        var balance = binding.tetWebBalance.text.toString()

        if (balance.isEmpty()) {
            balance = "0.0"
        }
        val cashAccount = Account(
            Account_Name =  accountName, Account_CardNumber = userID ,Account_Balance =  balance.toDouble(),
            Account_CountInNetAssets =  countInNetAsset, Account_Memo = memo, AccountType_ID = accountTypeID, Currency_ID = currency)


        addWebAccountViewModel.insertWebAccount(requireActivity(), cashAccount)


    }

    private fun submitForm() {
        binding.webAccountNameTextLayout.helperText = validAccountName()


        val validAccountName = binding.webAccountNameTextLayout.helperText == null
        val validBalance = binding.webBalanceTextLayout.helperText == null

        if (validAccountName && validBalance ) {
            insertData()
            findNavController().navigate(R.id.navigation_account)

        }
        else
            invalidForm()
    }

    private fun invalidForm() {
        var message = ""
        if(binding.webAccountNameTextLayout.helperText != null) {
            message += "\nAccountName: " + binding.webAccountNameTextLayout.helperText
        }
        if(binding.webBalanceTextLayout.helperText != null) {
            message += "\n\nBalance: " + binding.webBalanceTextLayout.helperText
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
        val accountNameText = binding.tetWebAccountName.text.toString()
        if(accountNameText.length < 3) {
            return "Minimum of 3 Characters required."
        }
        return null
    }


}