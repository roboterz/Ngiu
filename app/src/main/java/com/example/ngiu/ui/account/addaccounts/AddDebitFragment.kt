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
import com.example.ngiu.databinding.FragmentAddCashBinding
import com.example.ngiu.databinding.FragmentAddDebitBinding
import com.example.ngiu.functions.addDecimalLimiter
import com.google.android.material.textfield.TextInputEditText
import kotlinx.android.synthetic.main.popup_title.view.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class AddDebitFragment : Fragment() {
    private var _binding: FragmentAddDebitBinding? = null
    private val binding get() = _binding!!
    var currency = "USD"

    private val viewModel: AddDebitViewModel by lazy {
        ViewModelProvider(this).get(AddDebitViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return FragmentAddDebitBinding.inflate(inflater, container, false).run {
            _binding = this
            root
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListeners()
        getView()?.findViewById<TextInputEditText?>(R.id.tetDebitBalance)?.addDecimalLimiter()
    }

    override fun onResume() {
        super.onResume()
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


    private fun initListeners() {
        binding.btnSaveDebit.setOnClickListener {
            submitForm()
        }

        binding.btnDebitAddOtherCurrency.setOnClickListener {

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
                binding.debitBalanceTextLayout.setSuffixText(currency)

            }

            // Create a new AlertDialog using builder object
            // Finally, display the alert dialog
            builder.create().show()
        }
    }

    private fun insertData() {
        val accountName = binding.tetDebitAccountName.text.toString()
        val countInNetAsset: Boolean = binding.scDebitCountNetAssets.isChecked
        val memo = binding.tetDebitMemo.text.toString()
        val accountTypeID = 3L
        var balance = binding.tetDebitBalance.text.toString()
        val cardNumber = binding.tetDebitCardNumber.text.toString()

        if (balance.isEmpty()) {
            balance = "0.0"
        }
        val cashAccount = Account(
            Account_Name =  accountName, Account_Balance =  balance.toDouble(), Account_CardNumber = cardNumber,
            Account_CountInNetAssets =  countInNetAsset, Account_Memo = memo, AccountType_ID = accountTypeID, Currency_ID = currency)

        GlobalScope.launch{
            viewModel.insertDebit(requireActivity(), cashAccount)
        }

    }

    private fun submitForm() {
        binding.debitAccountNameTextLayout.helperText = validAccountName()
        binding.debitCardNumberTextLayout.helperText = validCardNumber()

        val validAccountName = binding.debitAccountNameTextLayout.helperText == null
        val validCardNumber = binding.debitCardNumberTextLayout.helperText == null

        if (validAccountName && validCardNumber ) {
            insertData()
            requireActivity().onBackPressed()
        }
        else
            invalidForm()
    }

    private fun invalidForm() {
        var message = ""
        if(binding.debitAccountNameTextLayout.helperText != null) {
            message += "\nAccountName: " + binding.debitAccountNameTextLayout.helperText
        }
        if(binding.debitCardNumberTextLayout.helperText != null) {
            message += "\n\nCardNumber: " + binding.debitCardNumberTextLayout.helperText
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
        val accountNameText = binding.tetDebitAccountName.text.toString()
        if(accountNameText.length < 3) {
            return "Minimum of 3 Characters required."
        }
        return null
    }

    private fun validCardNumber(): String? {
        val accountDebitText = binding.tetDebitCardNumber.text.toString()
        if(accountDebitText.length < 16) {
            return "Invalid Card Number Entry."
        }
        return null
    }
}