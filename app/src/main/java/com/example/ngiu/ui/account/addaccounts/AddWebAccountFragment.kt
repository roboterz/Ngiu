package com.example.ngiu.ui.account.addaccounts

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.ngiu.R
import com.example.ngiu.data.entities.Account
import com.example.ngiu.data.entities.Currency
import com.example.ngiu.databinding.FragmentAddVirtualAccountBinding
import com.example.ngiu.databinding.FragmentAddWebAccountBinding
import com.example.ngiu.functions.addDecimalLimiter
import com.google.android.material.textfield.TextInputEditText
import kotlinx.android.synthetic.main.popup_title.view.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class AddWebAccountFragment : Fragment() {
    private var _binding: FragmentAddWebAccountBinding? = null
    private val binding get() = _binding!!
    var currency = "USD"

    private val viewModel: AddWebAccountViewModel by lazy {
        ViewModelProvider(this).get(AddWebAccountViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return FragmentAddWebAccountBinding.inflate(inflater, container, false).run {
            _binding = this
            root
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListeners()
        getView()?.findViewById<TextInputEditText?>(R.id.tetWebBalance)?.addDecimalLimiter()
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

            val array : List<Currency> = viewModel.getCurrency(requireActivity())

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
                currency = arrayList.get(which)
                binding.webBalanceTextLayout.setSuffixText(currency)

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

        GlobalScope.launch{
            viewModel.insertWebAccount(requireActivity(), cashAccount)
        }

    }

    private fun submitForm() {
        binding.webAccountNameTextLayout.helperText = validAccountName()
        binding.webBalanceTextLayout.helperText = validBalance()

        val validAccountName = binding.webAccountNameTextLayout.helperText == null
        val validBalance = binding.webBalanceTextLayout.helperText == null

        if (validAccountName && validBalance ) {
            insertData()
            requireActivity().onBackPressed()
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

    private fun validBalance(): String? {
        val accountBalanceText = binding.tetWebBalance.text.toString()
        if(accountBalanceText.length < 0) {
            return "Invalid Balance Entry."
        }
        return null
    }
}