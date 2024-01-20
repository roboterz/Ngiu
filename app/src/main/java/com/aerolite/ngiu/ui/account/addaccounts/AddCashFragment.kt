package com.aerolite.ngiu.ui.account.addaccounts

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.aerolite.ngiu.MainActivity
import com.aerolite.ngiu.R
import com.aerolite.ngiu.data.entities.Account
import com.aerolite.ngiu.data.entities.Currency
import com.aerolite.ngiu.databinding.FragmentAccountAddCashBinding
import com.aerolite.ngiu.functions.addDecimalLimiter
import com.google.android.material.textfield.TextInputEditText
//import kotlinx.android.synthetic.main.fragment_account_add_cash.*
//import kotlinx.android.synthetic.main.popup_title.view.*
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import com.aerolite.ngiu.functions.ACCOUNT_TYPE_CASH
import com.aerolite.ngiu.functions.ACCOUNT_TYPE_CREDIT
import com.aerolite.ngiu.functions.ACCOUNT_TYPE_RECEIVABLE
import com.aerolite.ngiu.functions.KEY_ACCOUNT_ID
import com.aerolite.ngiu.functions.KEY_ACCOUNT_PAGE
import com.aerolite.ngiu.functions.KEY_VALUE_ACCOUNT_ADD_CASH
import com.aerolite.ngiu.functions.KEY_VALUE_ACCOUNT_ADD_PAYABLE
import com.aerolite.ngiu.functions.KEY_VALUE_ACCOUNT_EDIT_CASH
import com.aerolite.ngiu.functions.KEY_VALUE_ACCOUNT_EDIT_PAYABLE
import com.aerolite.ngiu.functions.*
//import kotlinx.android.synthetic.main.fragment_account_add_web_account.*


class AddCashFragment : Fragment() {
    private var _binding: FragmentAccountAddCashBinding? = null
    private val binding get() = _binding!!
    private lateinit var addCashViewModel: AddCashViewModel

    var currency = "USD"
    var page: Long = 0L
    var accountTypeID : Long = 0L
    private var accountID : Long= 0L

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        addCashViewModel = ViewModelProvider(this)[AddCashViewModel::class.java]
        _binding = FragmentAccountAddCashBinding.inflate(inflater, container, false)

        getBundleData()
        displayPage()


        return binding.root
    }

    private fun displayPage() {
        when (page) {
            KEY_VALUE_ACCOUNT_ADD_CASH -> {
                binding.toolbarAddCashAccount.title = "Add Cash"
                accountTypeID = ACCOUNT_TYPE_CASH
            }
            KEY_VALUE_ACCOUNT_EDIT_CASH -> {
                binding.toolbarAddCashAccount.title = "Edit Cash"
                binding.toolbarAddCashAccount.menu.findItem(R.id.action_delete).isVisible = true
                accountTypeID = ACCOUNT_TYPE_CREDIT

                accountID = arguments?.getLong(KEY_ACCOUNT_ID)!!
             /*   binding.cashBalanceTextLayout.isEnabled = false
                binding.btnCashAddOtherCurrency.isEnabled = false*/
                fetchAccountDetails(accountID)
            }
            KEY_VALUE_ACCOUNT_ADD_PAYABLE -> {
                binding.toolbarAddCashAccount.title = "Add Receivable/Payable"
                accountTypeID = ACCOUNT_TYPE_RECEIVABLE
            }
            KEY_VALUE_ACCOUNT_EDIT_PAYABLE -> {
                binding.toolbarAddCashAccount.title = "Edit Receivable/Payable"
                binding.toolbarAddCashAccount.menu.findItem(R.id.action_delete).isVisible = true
                accountTypeID = ACCOUNT_TYPE_RECEIVABLE
                accountID = arguments?.getLong(KEY_ACCOUNT_ID)!!

                fetchAccountDetails(accountID)
            }
        }
    }

    private fun fetchAccountDetails(acctID: Long) {
        val account =  addCashViewModel.getAccountByID(requireContext(),acctID)
        binding.tetCashAccountName.setText(account.Account_Name)
        binding.tetCashBalance.setText(account.Account_Balance.toString())
        binding.tetCashMemo.setText(account.Account_Memo)
        /*binding.scCashCountNetAssets.setText(account.Account_CountInNetAssets.toString())*/
        binding.cashBalanceTextLayout.suffixText = account.Currency_ID
        binding.scCashCountNetAssets.isChecked = account.Account_CountInNetAssets
    }

    private fun getBundleData() {
        page = arguments?.getLong(KEY_ACCOUNT_PAGE)!!

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListeners()
        getView()?.findViewById<TextInputEditText?>(R.id.tetCashBalance)?.addDecimalLimiter()

        binding.toolbarAddCashAccount.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        (activity as MainActivity).setNavBottomBarVisibility(View.GONE)

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    @SuppressLint("InflateParams")
    private fun initListeners() {
        binding.btnSaveCash.setOnClickListener {
            when (page) {
                KEY_VALUE_ACCOUNT_ADD_CASH -> {
                    submitForm()
                }
                KEY_VALUE_ACCOUNT_EDIT_CASH -> {

                    val tempID = arguments?.getLong(KEY_ACCOUNT_ID)!!

                    updateAccount(tempID)
                }
                KEY_VALUE_ACCOUNT_ADD_PAYABLE -> {
                    binding.toolbarAddCashAccount.title = "Add Receivable/Payable"
                    accountTypeID = ACCOUNT_TYPE_RECEIVABLE
                    submitForm()

                }
                KEY_VALUE_ACCOUNT_EDIT_PAYABLE -> {
                    binding.toolbarAddCashAccount.title = "Edit Receivable/Payable"
                    val tempID = arguments?.getLong(KEY_ACCOUNT_ID)!!
                    updateAccount(tempID)
                }
            }



        }

        binding.btnCashAddOtherCurrency.setOnClickListener {

            val array: List<Currency> = addCashViewModel.getCurrency(requireActivity())

            val arrayList: ArrayList<String> = ArrayList()

            for (item in array) {
                arrayList.add(item.Currency_ID)
            }

            val cs: Array<CharSequence> =
                arrayList.toArray(arrayOfNulls<CharSequence>(arrayList.size))

            //   val array = array()
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
                binding.cashBalanceTextLayout.suffixText = currency

            }

            // Create a new AlertDialog using builder object
            // Finally, display the alert dialog
            builder.create().show()
        }

//        binding.scCashCountNetAssets.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
//            Log.v(
//                "Switch State=",
//                "" + isChecked
//            )
//        })



        binding.toolbarAddCashAccount.setOnMenuItemClickListener{
            when (it.itemId) {
                R.id.action_delete -> {

                    deleteAccount(requireActivity(), accountID)

                    true
                }


                else -> true
            }
        }

    }

    private fun updateAccount(acctID: Long) {

        val account = Account(
            Account_ID = acctID,
            Account_Name = binding.tetCashAccountName.text.toString(),
            Account_Balance = binding.tetCashBalance.text.toString().toDouble(),
            Account_CountInNetAssets = binding.scCashCountNetAssets.isChecked,
            Account_Memo = binding.tetCashMemo.text.toString(),
            AccountType_ID = accountTypeID,
            Currency_ID = currency
        )
        addCashViewModel.getAllAccount(requireContext()).forEach { item ->
            if (item.Account_Name.equals(binding.tetCashAccountName.text.toString(), ignoreCase = true)  && (account.Account_ID != item.Account_ID)) {
                Toast.makeText(requireContext(), "Account Name already exist", Toast.LENGTH_LONG).show()
                return
            }
        }

        addCashViewModel.updateAccount(requireContext(),account)
        findNavController().popBackStack()
        Toast.makeText(requireContext(), "Update Successful",Toast.LENGTH_LONG).show()

    }

    private fun insertData() {

        val accountName = binding.tetCashAccountName.text.toString()

        addCashViewModel.getAllAccount(requireContext()).forEach { item ->
            if (item.Account_Name.equals(accountName, ignoreCase = true)) {
                Toast.makeText(requireContext(), "Account Name already exist", Toast.LENGTH_LONG).show()
                return
            }
        }



        val countInNetAsset: Boolean = binding.scCashCountNetAssets.isChecked
        val memo = binding.tetCashMemo.text.toString()

        var balance = binding.tetCashBalance.text.toString()

        if (balance.isEmpty()) {
            balance = "0.0"
        }
        val cashAccount = Account(
            Account_Name = accountName,
            Account_Balance = balance.toDouble(),
            Account_CountInNetAssets = countInNetAsset,
            Account_Memo = memo,
            AccountType_ID = accountTypeID,
            Currency_ID = currency
        )


        addCashViewModel.insertCash(requireActivity(), cashAccount)

        Toast.makeText(requireContext(), "Successfully added your account",Toast.LENGTH_LONG).show()

    }

    private fun submitForm() {
        binding.cashAccountNameTextLayout.helperText = validAccountName()

        val validAccountName = binding.cashAccountNameTextLayout.helperText == null


        if (validAccountName) {
            insertData()
            findNavController().popBackStack()
        } else
            invalidForm()
    }

    private fun invalidForm() {
        var message = ""
        if (binding.cashAccountNameTextLayout.helperText != null) {
            message += "\nAccountName: " + binding.cashAccountNameTextLayout.helperText
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
        val accountNameText = binding.tetCashAccountName.text.toString()
        if (accountNameText.length < 3) {
            return "Minimum of 3 Characters required."
        }
        return null
    }

    // delete record
    @SuppressLint("InflateParams")
    private fun deleteAccount(activity: FragmentActivity?, accountID: Long) {

        val dialogBuilder = AlertDialog.Builder(activity)

        dialogBuilder.setMessage(getText(R.string.msg_content_account_delete))
            .setCancelable(true)
            .setPositiveButton(getText(R.string.msg_button_confirm)) { _, _ ->

                //delete account
                addCashViewModel.deleteAccount(requireContext(),accountID)
                // navigate to account list screen
                findNavController().navigate(R.id.navigation_account)
                Toast.makeText(requireContext(), "Successfully Deleted Your Account", Toast.LENGTH_LONG).show()

                // exit
                //requireActivity().onBackPressed()

            }
            .setNegativeButton(getText(R.string.msg_button_cancel)) { dialog, _ ->
                // cancel
                dialog.cancel()
            }

        // set Title Style
        val titleView = layoutInflater.inflate(R.layout.popup_title,null)
        // set Title Text
        titleView.findViewById<TextView>(R.id.tv_popup_title_text).text = getText(R.string.msg_Title_prompt)

        val alert = dialogBuilder.create()
        //alert.setIcon(R.drawable.ic_baseline_delete_forever_24)
        alert.setCustomTitle(titleView)
        alert.show()
    }

}