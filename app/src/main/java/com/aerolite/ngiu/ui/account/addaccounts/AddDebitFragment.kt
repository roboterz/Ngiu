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
import com.aerolite.ngiu.functions.ACCOUNT_TYPE_DEBIT
import com.aerolite.ngiu.functions.KEY_ACCOUNT_ID
import com.aerolite.ngiu.functions.KEY_ACCOUNT_PAGE
import com.aerolite.ngiu.functions.KEY_VALUE_ACCOUNT_ADD_DEBIT
import com.aerolite.ngiu.functions.KEY_VALUE_ACCOUNT_EDIT_DEBIT
import com.aerolite.ngiu.functions.addDecimalLimiter
import com.aerolite.ngiu.MainActivity
import com.aerolite.ngiu.R
import com.aerolite.ngiu.data.entities.Account
import com.aerolite.ngiu.data.entities.Currency
import com.aerolite.ngiu.databinding.FragmentAccountAddDebitBinding
import com.google.android.material.textfield.TextInputEditText
//import kotlinx.android.synthetic.main.fragment_account_add_debit.*
//import kotlinx.android.synthetic.main.fragment_account_add_web_account.*
//import kotlinx.android.synthetic.main.popup_title.view.*


class AddDebitFragment : Fragment() {
    private var _binding: FragmentAccountAddDebitBinding? = null
    private val binding get() = _binding!!

    private lateinit var addCashViewModel: AddCashViewModel
    var currency = "USD"
    var page: Long = 0L
    var accountTypeID: Long = ACCOUNT_TYPE_DEBIT
    var id: Long = 0L

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        addCashViewModel = ViewModelProvider(this)[AddCashViewModel::class.java]
        _binding = FragmentAccountAddDebitBinding.inflate(inflater, container, false)

        getBundleData()
        displayPage()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListeners()
        getView()?.findViewById<TextInputEditText?>(R.id.tetDebitBalance)?.addDecimalLimiter()

        binding.toolbarAddDebitAccount.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        (activity as MainActivity).setNavBottomBarVisibility(View.GONE)
    }

    private fun displayPage() {
        when (page) {
            KEY_VALUE_ACCOUNT_ADD_DEBIT -> {
                binding.toolbarAddDebitAccount.title = "Add Debit"
            }
            KEY_VALUE_ACCOUNT_EDIT_DEBIT -> {
                binding.toolbarAddDebitAccount.title = "Edit Debit"
                binding.toolbarAddDebitAccount.menu.findItem(R.id.action_delete).isVisible = true
                accountTypeID = ACCOUNT_TYPE_DEBIT
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
        binding.tetDebitAccountName.setText(account.Account_Name)
        binding.tetDebitBalance.setText(account.Account_Balance.toString())
        binding.tetDebitCardNumber.setText(account.Account_CardNumber)
        binding.scDebitCountNetAssets.isChecked = account.Account_CountInNetAssets
        binding.tetDebitMemo.setText(account.Account_Memo)
    }

    private fun initListeners() {
        binding.btnSaveDebit.setOnClickListener {
            when (page) {
                KEY_VALUE_ACCOUNT_ADD_DEBIT -> {
                    submitForm()
                }
                KEY_VALUE_ACCOUNT_EDIT_DEBIT -> {
                    id = arguments?.getLong(KEY_ACCOUNT_ID)!!
                    updateAccount(id)
                }

            }



            binding.btnDebitAddOtherCurrency.setOnClickListener {

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
                titleView.findViewById<TextView>(R.id.tv_popup_title_text).text = getText(R.string.option_title_add_currency)

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

        binding.toolbarAddDebitAccount.setOnMenuItemClickListener{
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
    }

    private fun updateAccount(id: Long) {

        val account = Account(
            Account_ID = id,
            Account_Name = binding.tetDebitAccountName.text.toString().trim(),
            Account_CardNumber = binding.tetDebitCardNumber.text.toString(),
            Account_Balance = binding.tetDebitBalance.text.toString().toDouble(),
            Account_CountInNetAssets = binding.scDebitCountNetAssets.isChecked,
            Account_Memo = binding.tetDebitMemo.text.toString().trim(),
            AccountType_ID = accountTypeID,
            Currency_ID = currency
        )
        addCashViewModel.getAllAccount(requireContext()).forEach { item ->
            if (item.Account_Name.equals(binding.tetDebitAccountName.text.toString(), ignoreCase = true)  && (account.Account_ID != item.Account_ID)) {
                Toast.makeText(requireContext(), "Account Name already exist", Toast.LENGTH_LONG).show()
                return
            }
        }

        addCashViewModel.updateAccount(requireContext(), account)
        findNavController().popBackStack()
        Toast.makeText(requireContext(), "Update Successful", Toast.LENGTH_LONG).show()

    }

    private fun insertData() {
        val accountName = binding.tetDebitAccountName.text.toString()
        addCashViewModel.getAllAccount(requireContext()).forEach { item ->
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

        addCashViewModel.insertCash(requireActivity(), debitAccount)
        Toast.makeText(requireContext(), "Successfully added your account",Toast.LENGTH_LONG).show()
    }

    private fun submitForm() {
        binding.debitAccountNameTextLayout.helperText = validAccountName()
        val validAccountName = binding.debitAccountNameTextLayout.helperText == null

        if (validAccountName) {
            insertData()
            findNavController().popBackStack()
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


