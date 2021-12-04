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
import com.example.ngiu.databinding.FragmentAccountAddCreditBinding
import com.example.ngiu.functions.addDecimalLimiter
import com.google.android.material.textfield.TextInputEditText
import kotlinx.android.synthetic.main.popup_title.view.*
import com.example.ngiu.functions.getDayOfMonthSuffix
import kotlinx.android.synthetic.main.fragment_account_add_credit.*

class AddCreditFragment : Fragment() {
    private var _binding: FragmentAccountAddCreditBinding? = null
    private val binding get() = _binding!!

    private lateinit var addCreditViewModel: AddCreditViewModel
    var currency = "USD"
    var statementDay = "1"
    var paymentDay = "26"



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        addCreditViewModel = ViewModelProvider(this).get(AddCreditViewModel::class.java)
        _binding = FragmentAccountAddCreditBinding.inflate(inflater, container, false)


        return binding.root


        }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListeners()
        getView()?.findViewById<TextInputEditText?>(R.id.tetCreditLimit)?.addDecimalLimiter()

        toolbarAddCreditAccount.setNavigationOnClickListener {
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
        binding.btnSaveCash.setOnClickListener {
            submitForm()
        }

        binding.tvCreditStateDay.setOnClickListener {
            val ls = listOf<String>("1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20","21","22","23","24","25","26","27","28")
            val array = ls.toTypedArray()

            // Initialize a new instance of alert dialog builder object
            val builder = AlertDialog.Builder(requireContext())

            // set Title Style
            val titleView = layoutInflater.inflate(R.layout.popup_title,null)
            // set Title Text
            titleView.tv_popup_title_text.text = getText(R.string.option_statement_day)

            builder.setCustomTitle(titleView)

            // Set items form alert dialog
            builder.setItems(array) { _, which ->
                statementDay = array.get(which)
                val suffix = getDayOfMonthSuffix(statementDay.toInt())
                binding.tvCreditStateDayValue.setText(statementDay + "$suffix")
            }

            // Create a new AlertDialog using builder object
            // Finally, display the alert dialog
            builder.create().show()
        }

        binding.tvCreditPaymentDay.setOnClickListener {
            val ls = listOf<String>("1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20","21","22","23","24","25","26","27","28")
            val array = ls.toTypedArray()

            // Initialize a new instance of alert dialog builder object
            val builder = AlertDialog.Builder(requireContext())

            // set Title Style
            val titleView = layoutInflater.inflate(R.layout.popup_title,null)
            // set Title Text
            titleView.tv_popup_title_text.text = getText(R.string.option_statement_day)

            builder.setCustomTitle(titleView)

            // Set items form alert dialog
            builder.setItems(array) { _, which ->
                paymentDay = array.get(which)
                val suffix = getDayOfMonthSuffix(paymentDay.toInt())
                binding.tvCreditPaymentDayValue.setText(paymentDay + "$suffix")
            }

            // Create a new AlertDialog using builder object
            // Finally, display the alert dialog
            builder.create().show()
        }


        binding.btnCreditAddOtherCurrency.setOnClickListener {

            val array : List<Currency> = addCreditViewModel.getCurrency(requireActivity())

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
                currency = arrayList.get(which)
                binding.creditLimitLayout.setSuffixText(currency)
                binding.creditCurrentArrearsLayout.setSuffixText(currency)
            }

            // Create a new AlertDialog using builder object
            // Finally, display the alert dialog
            builder.create().show()
        }

        binding.btnCreditAddOtherArrears.setOnClickListener {

            val array : List<Currency> = addCreditViewModel.getCurrency(requireActivity())

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
                binding.creditCurrentArrearsLayout.suffixText = currency
                binding.creditLimitLayout.suffixText = currency

            }

            // Create a new AlertDialog using builder object
            // Finally, display the alert dialog
            builder.create().show()
        }
    }

    private fun insertData() {
        val accountName = binding.tetCreditAccountName.text.toString()
        val cardNumber = binding.tetCreditCardNumber.text.toString()
        val accountTypeID = 2L
        var creditLimit = binding.tetCreditLimit.text.toString()
        var currentArrears = binding.tetCreditArrears.text.toString()
        val countInNetAsset: Boolean = binding.scCashCountNetAssets.isChecked
        val memo = binding.tetCreditMemo.text.toString()

        if (currentArrears.isEmpty()) {
            currentArrears = "0.0"
        }

        if (creditLimit.isEmpty()) {
            creditLimit = "5000"
        }
        val creditAccount = Account(
            Account_Name =  accountName, Account_CardNumber = cardNumber, Account_CreditLimit = creditLimit.toDouble(),
            Account_Balance =  currentArrears.toDouble() * -1, Account_CountInNetAssets =  countInNetAsset, Account_Memo = memo,
            AccountType_ID = accountTypeID, Currency_ID = currency, Account_StatementDay = statementDay.toInt(),
            Account_PaymentDay = paymentDay.toInt())


        addCreditViewModel.insertCredit(requireActivity(), creditAccount)


    }

    private fun submitForm() {
        binding.creditAccountNameTextLayout.helperText = validAccountName()
        binding.creditCardNumberTextLayout.helperText = validCard()


        val validAccountName = binding.creditAccountNameTextLayout.helperText == null
        val validCard = binding.creditCardNumberTextLayout.helperText == null


        if (validAccountName && validCard ) {
            insertData()
            findNavController().navigate(R.id.navigation_account)

        }
        else
            invalidForm()
    }

    private fun invalidForm() {
        var message = ""
        if(binding.creditAccountNameTextLayout.helperText != null) {
            message += "\nAccountName: " + binding.creditAccountNameTextLayout.helperText
        }
        if(binding.creditCardNumberTextLayout.helperText != null) {
            message += "\n\nCredit Card: " + binding.creditCardNumberTextLayout.helperText
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
        val accountNameText = binding.tetCreditAccountName.text.toString()
        if(accountNameText.length < 3) {
            return "Minimum of 3 Characters required."
        }
        return null
    }

    private fun validCard(): String? {
        val cardNumber = binding.tetCreditCardNumber.text.toString()
        if(cardNumber.length < 4) {
            return "Enter at least four digits credit Card number."
        }
        return null
    }



}
