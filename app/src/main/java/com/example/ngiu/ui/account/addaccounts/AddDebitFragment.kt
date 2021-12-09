package com.example.ngiu.ui.account.addaccounts

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
import com.example.ngiu.databinding.FragmentAccountAddDebitBinding
import com.example.ngiu.functions.addDecimalLimiter
import com.google.android.material.textfield.TextInputEditText
import kotlinx.android.synthetic.main.fragment_account_add_debit.*
import kotlinx.android.synthetic.main.popup_title.view.*


class AddDebitFragment : Fragment() {
    private var _binding: FragmentAccountAddDebitBinding? = null
    private val binding get() = _binding!!

    private lateinit var addDebitViewModel: AddDebitViewModel
    var currency = "USD"
    lateinit var page: String
    private var balance: Double = 0.0
    var accountTypeID: Long = 3L


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        addDebitViewModel = ViewModelProvider(this).get(AddDebitViewModel::class.java)
        _binding = FragmentAccountAddDebitBinding.inflate(inflater, container, false)

        getBundleData()
        displayPage()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListeners()
        getView()?.findViewById<TextInputEditText?>(R.id.tetDebitBalance)?.addDecimalLimiter()

        toolbarAddDebitAccount.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        (activity as MainActivity).setNavBottomBarVisibility(View.GONE)
    }

    private fun displayPage() {
        when (page) {
            "add_debit" -> {
                binding.toolbarAddDebitAccount.title = "Add Debit"
            }
            "edit_debit" -> {
                binding.toolbarAddDebitAccount.title = "Edit Debit"

                accountTypeID = 3L
                var id: Long = 0L
                id = arguments?.getLong("id")!!

                fetchAccountDetails(id)
            }

        }
    }

    private fun getBundleData() {
        page = arguments?.getString("page")!!
        balance = arguments?.getDouble("balance")!!
    }

    private fun fetchAccountDetails(id: Long) {
        val account = addDebitViewModel.getAccountByID(requireContext(), id)
        binding.tetDebitAccountName.setText(account.Account_Name)
        binding.tetDebitBalance.setText("%.2f".format(balance))
        binding.tetDebitCardNumber.setText(account.Account_CardNumber)
        binding.scDebitCountNetAssets.isChecked = account.Account_CountInNetAssets
        binding.tetDebitMemo.setText(account.Account_Memo)
    }

    private fun initListeners() {
        binding.btnSaveDebit.setOnClickListener {
            when (page) {
                "add_debit" -> {
                    submitForm()
                }
                "edit_debit" -> {

                    var id: Long = 0L
                    id = arguments?.getLong("id")!!

                    updateAccount(id)
                }

            }

            binding.btnDebitAddOtherCurrency.setOnClickListener {

                val array: List<Currency> = addDebitViewModel.getCurrency(requireActivity())

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
                    currency = arrayList[which]
                    binding.debitBalanceTextLayout.suffixText = currency

                }

                // Create a new AlertDialog using builder object
                // Finally, display the alert dialog
                builder.create().show()
            }
        }
    }

    private fun updateAccount(id: Long) {

        val account = Account(
            Account_ID = id,
            Account_Name = binding.tetDebitAccountName.text.toString(),
            Account_Balance = binding.tetDebitBalance.text.toString().toDouble(),
            Account_CountInNetAssets = binding.scDebitCountNetAssets.isChecked,
            Account_Memo = binding.tetDebitMemo.text.toString(),
            AccountType_ID = accountTypeID,
            Currency_ID = currency
        )
        addDebitViewModel.getAllAccount(requireContext()).forEach { item ->
            if (item.Account_Name.equals(binding.tetDebitAccountName.text.toString(), ignoreCase = true)  && (account.Account_ID != item.Account_ID)) {
                Toast.makeText(requireContext(), "Account Name already exist", Toast.LENGTH_LONG).show()
                return
            }
        }

        addDebitViewModel.updateAccount(requireContext(), account)
        findNavController().navigate(R.id.navigation_account)
        Toast.makeText(requireContext(), "Update Successful", Toast.LENGTH_LONG).show()

    }

    private fun insertData() {
        val accountName = binding.tetDebitAccountName.text.toString()
        addDebitViewModel.getAllAccount(requireContext()).forEach { item ->
            if (item.Account_Name.equals(accountName, ignoreCase = true)) {
                Toast.makeText(requireContext(), "Account Name already exist", Toast.LENGTH_LONG).show()
                return
            }
        }

        val countInNetAsset: Boolean = binding.scDebitCountNetAssets.isChecked
        val memo = binding.tetDebitMemo.text.toString()
        var balance = binding.tetDebitBalance.text.toString()
        val cardNumber = binding.tetDebitCardNumber.text.toString()

        if (balance.isEmpty()) {
            balance = "0.0"
        }
        val debitAccount = Account(
            Account_Name = accountName,
            Account_Balance = balance.toDouble(),
            Account_CardNumber = cardNumber,
            Account_CountInNetAssets = countInNetAsset,
            Account_Memo = memo,
            AccountType_ID = accountTypeID,
            Currency_ID = currency
        )

        addDebitViewModel.insertDebit(requireActivity(), debitAccount)
    }

    private fun submitForm() {
        binding.debitAccountNameTextLayout.helperText = validAccountName()
        val validAccountName = binding.debitAccountNameTextLayout.helperText == null

        if (validAccountName) {
            insertData()
            findNavController().navigate(R.id.navigation_account)
        } else
            invalidForm()
    }

    private fun invalidForm() {
        var message = ""
        if (binding.debitAccountNameTextLayout.helperText != null) {
            message += "\nAccountName: " + binding.debitAccountNameTextLayout.helperText
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
        val accountNameText = binding.tetDebitAccountName.text.toString()
        if (accountNameText.length < 3) {
            return "Minimum of 3 Characters required."
        }
        return null
    }







}



