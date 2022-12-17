package com.example.ngiu.ui.account.addaccounts

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.ngiu.MainActivity
import com.example.ngiu.R
import com.example.ngiu.data.entities.Account
import com.example.ngiu.data.entities.Currency
import com.example.ngiu.databinding.FragmentAccountAddCreditBinding
import com.example.ngiu.functions.ACCOUNT_TYPE_CREDIT
import com.example.ngiu.functions.addDecimalLimiter
import com.google.android.material.textfield.TextInputEditText
import kotlinx.android.synthetic.main.popup_title.view.*
import com.example.ngiu.functions.getDayOfMonthSuffix
import kotlinx.android.synthetic.main.fragment_account_add_credit.*

class AddCreditFragment : Fragment() {
    private var _binding: FragmentAccountAddCreditBinding? = null
    private val binding get() = _binding!!

    private lateinit var addCashViewModel: AddCashViewModel
    var currency = "USD"
    var statementDay = "1"
    var paymentDay = "26"
    private val accountTypeID = ACCOUNT_TYPE_CREDIT
    var id: Long = 0L
    lateinit var page: String




    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        addCashViewModel = ViewModelProvider(this).get(AddCashViewModel::class.java)
        _binding = FragmentAccountAddCreditBinding.inflate(inflater, container, false)

        getBundleData()
        displayPage()

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

    private fun getBundleData() {
        page = arguments?.getString("page")!!

    }

    private fun displayPage() {
        when (page) {
            "add_credit" -> {
                binding.toolbarAddCreditAccount.title = "Add Credit Card"

            }
            "edit_credit" -> {
                binding.toolbarAddCreditAccount.title = "Edit Credit Card"
                binding.toolbarAddCreditAccount.menu.findItem(R.id.action_delete).isVisible = true
                id = arguments?.getLong("id")!!
                fetchAccountDetails(id)
            }

        }
    }


    @SuppressLint("SetTextI18n")
    private fun fetchAccountDetails(id: Long) {
        val account =  addCashViewModel.getAccountByID(requireContext(),id)
        binding.tetCreditAccountName.setText(account.Account_Name)
        binding.tetCreditCardNumber.setText(account.Account_CardNumber)
        binding.tetCreditArrears.setText("%.2f".format(account.Account_Balance))
        binding.tetCreditMemo.setText(account.Account_Memo)
        binding.tetCreditLimit.setText("%.2f".format(account.Account_CreditLimit))
        /*"Statement Day: $statementDay$suffix"*/
        val stateSuffix = getDayOfMonthSuffix(account.Account_StatementDay)
        binding.tvCreditStateDay.setText("Statement Day: ${account.Account_StatementDay}$stateSuffix")
        val paySuffix = getDayOfMonthSuffix(account.Account_PaymentDay)
        binding.tvCreditPaymentDay.setText("Payment Day: ${account.Account_PaymentDay}$paySuffix")
        binding.creditCurrentArrearsLayout.suffixText = account.Currency_ID
        binding.creditLimitLayout.suffixText = account.Currency_ID
        binding.scCreditCountNetAssets.isChecked = account.Account_CountInNetAssets
    }

    private fun updateAccount(id: Long) {
        val account = Account(
            Account_ID = id,
            Account_Name = binding.tetCreditAccountName.text.toString(),
            Account_Memo = binding.tetCreditMemo.text.toString(),
            Account_CardNumber = binding.tetCreditCardNumber.text.toString(),
            Account_CreditLimit = binding.tetCreditLimit.text.toString().toDouble(),
            Account_Balance = binding.tetCreditArrears.text.toString().toDouble() * -1,
            Account_CountInNetAssets = binding.scCreditCountNetAssets.isChecked,
            AccountType_ID = accountTypeID,
            Currency_ID = currency,
            Account_StatementDay = statementDay.toInt(),
            Account_PaymentDay = paymentDay.toInt()
        )

        addCashViewModel.getAllAccount(requireContext()).forEach { item ->
            if (item.Account_Name.equals(
                    binding.tetCreditAccountName.text.toString(),
                    ignoreCase = true
                ) && (account.Account_ID != item.Account_ID)
            ) {
                Toast.makeText(requireContext(), "Account Name already exist", Toast.LENGTH_LONG)
                    .show()
                return
            }
        }

        addCashViewModel.updateAccount(requireContext(), account)
        findNavController().navigate(R.id.navigation_account)
        Toast.makeText(requireContext(), "Update Successful", Toast.LENGTH_LONG).show()

    }

    private fun initListeners() {
        binding.btnSaveCash.setOnClickListener {
            when (page) {
                "add_credit" -> {
                    submitForm()
                }
                "edit_credit" -> {
                    id = arguments?.getLong("id")!!
                    updateAccount(id)
                }
            }
        }

        binding.toolbarAddCreditAccount.setOnMenuItemClickListener{
            when (it.itemId) {
                R.id.action_delete -> {
                    // navigate to add record screen
                    addCashViewModel.deleteAccount(requireContext(),id)
                    findNavController().navigate(R.id.navigation_account)
                    Toast.makeText(requireContext(), "Successfully Deleted Your Account", Toast.LENGTH_LONG).show()
                    true
                }


                else -> super.onOptionsItemSelected(it)
            }
        }

        binding.tvCreditStateDay.setOnClickListener {
            val ls = listOf<String>(
                "1",
                "2",
                "3",
                "4",
                "5",
                "6",
                "7",
                "8",
                "9",
                "10",
                "11",
                "12",
                "13",
                "14",
                "15",
                "16",
                "17",
                "18",
                "19",
                "20",
                "21",
                "22",
                "23",
                "24",
                "25",
                "26",
                "27",
                "28",
                "29",
                "30",
                "31"
            )
            val array = ls.toTypedArray()

            // Initialize a new instance of alert dialog builder object
            val builder = AlertDialog.Builder(requireContext())

            // set Title Style
            val titleView = layoutInflater.inflate(R.layout.popup_title, null)
            // set Title Text
            titleView.tv_popup_title_text.text = getText(R.string.option_statement_day)

            builder.setCustomTitle(titleView)

            // Set items form alert dialog
            builder.setItems(array) { _, which ->
                statementDay = array[which]
                val suffix = getDayOfMonthSuffix(statementDay.toInt())
                binding.tvCreditStateDay.text = "Statement Day: $statementDay$suffix"
            }

            // Create a new AlertDialog using builder object
            // Finally, display the alert dialog
            builder.create().show()
        }

        binding.tvCreditPaymentDay.setOnClickListener {
            val ls = listOf<String>(
                "1",
                "2",
                "3",
                "4",
                "5",
                "6",
                "7",
                "8",
                "9",
                "10",
                "11",
                "12",
                "13",
                "14",
                "15",
                "16",
                "17",
                "18",
                "19",
                "20",
                "21",
                "22",
                "23",
                "24",
                "25",
                "26",
                "27",
                "28",
                "29",
                "30",
                "31"
            )
            val array = ls.toTypedArray()

            // Initialize a new instance of alert dialog builder object
            val builder = AlertDialog.Builder(requireContext())

            // set Title Style
            val titleView = layoutInflater.inflate(R.layout.popup_title, null)
            // set Title Text
            titleView.tv_popup_title_text.text = getText(R.string.option_statement_day)

            builder.setCustomTitle(titleView)

            // Set items form alert dialog
            builder.setItems(array) { _, which ->
                paymentDay = array[which]
                val suffix = getDayOfMonthSuffix(paymentDay.toInt())
                binding.tvCreditPaymentDay.text = "Payment Day: \u00A0 \u00A0$paymentDay$suffix"
            }

            // Create a new AlertDialog using builder object
            // Finally, display the alert dialog
            builder.create().show()
        }


        binding.btnCreditAddOtherCurrency.setOnClickListener {

            val array: List<Currency> = addCashViewModel.getCurrency(requireActivity())

            val arrayList: ArrayList<String> = ArrayList()

            for (item in array) {
                arrayList.add(item.Currency_ID)
            }

            val cs: Array<CharSequence> =
                arrayList.toArray(arrayOfNulls<CharSequence>(arrayList.size))

            //   val array = arrayof()
            // Initialize a new instance of alert dialog builder object
            val builder = AlertDialog.Builder(requireContext())

            // set Title Style
            val titleView = layoutInflater.inflate(R.layout.popup_title, null)
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

            val array: List<Currency> = addCashViewModel.getCurrency(requireActivity())

            val arrayList: ArrayList<String> = ArrayList()

            for (item in array) {
                arrayList.add(item.Currency_ID)
            }

            val cs: Array<CharSequence> =
                arrayList.toArray(arrayOfNulls<CharSequence>(arrayList.size))

            // Initialize a new instance of alert dialog builder object
            val builder = AlertDialog.Builder(requireContext())

            // set Title Style
            val titleView = layoutInflater.inflate(R.layout.popup_title, null)
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
        addCashViewModel.getAllAccount(requireContext()).forEach { item ->
            if (item.Account_Name.equals(accountName, ignoreCase = true)) {
                Toast.makeText(requireContext(), "Account Name already exist", Toast.LENGTH_LONG)
                    .show()
                return
            }
        }

        val cardNumber = binding.tetCreditCardNumber.text.toString()
        var creditLimit = binding.tetCreditLimit.text.toString()
        var currentArrears = binding.tetCreditArrears.text.toString()
        val countInNetAsset: Boolean = binding.scCreditCountNetAssets.isChecked
        val memo = binding.tetCreditMemo.text.toString()

        if (currentArrears.isEmpty()) {
            currentArrears = "0.0"
        }

        if (creditLimit.isEmpty()) {
            creditLimit = "5000"
        }
        val creditAccount = Account(
            Account_Name = accountName,
            Account_CardNumber = cardNumber,
            Account_CreditLimit = creditLimit.toDouble(),
            Account_Balance = currentArrears.toDouble() * -1,
            Account_CountInNetAssets = countInNetAsset,
            Account_Memo = memo,
            AccountType_ID = accountTypeID,
            Currency_ID = currency,
            Account_StatementDay = statementDay.toInt(),
            Account_PaymentDay = paymentDay.toInt()
        )


        addCashViewModel.insertCash(requireActivity(), creditAccount)


    }

    private fun submitForm() {
        binding.creditAccountNameTextLayout.helperText = validAccountName()

        val validAccountName = binding.creditAccountNameTextLayout.helperText == null

        if (validAccountName) {
            insertData()
            findNavController().navigate(R.id.navigation_account)

        } else
            invalidForm()
    }

    private fun invalidForm() {
        var message = ""
        if (binding.creditAccountNameTextLayout.helperText != null) {
            message += "\nAccountName: " + binding.creditAccountNameTextLayout.helperText
        }
        if (binding.creditCardNumberTextLayout.helperText != null) {
            message += "\n\nCredit Card: " + binding.creditCardNumberTextLayout.helperText
        }


        AlertDialog.Builder(context)
            .setTitle("Invalid Form")
            .setMessage(message)
            .setPositiveButton("Okay") { _, _ ->
                // do nothing
            }
            .show()
    }


    private fun validAccountName(): String? {
        val accountNameText = binding.tetCreditAccountName.text.toString()
        if (accountNameText.length < 3) {
            return "Minimum of 3 Characters required."
        }
        return null
    }


}
