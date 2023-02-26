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
import com.example.ngiu.databinding.FragmentAccountAddWebAccountBinding
import com.example.ngiu.functions.*
import com.example.ngiu.functions.addDecimalLimiter
import com.google.android.material.textfield.TextInputEditText
import kotlinx.android.synthetic.main.fragment_account_add_web_account.*
import kotlinx.android.synthetic.main.popup_title.view.*



class AddWebAccountFragment : Fragment() {
    private var _binding: FragmentAccountAddWebAccountBinding? = null
    private val binding get() = _binding!!
    private lateinit var addCashViewModel: AddCashViewModel
    var currency = "USD"
    private var balance: Double = 0.0
    var page: Long = 0L
    var accountTypeID : Long = 0L
    var id : Long= 0L

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        addCashViewModel = ViewModelProvider(this)[AddCashViewModel::class.java]
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
        getBundleData()
        displayPage()



        (activity as MainActivity).setNavBottomBarVisibility(View.GONE)
    }

    private fun displayPage() {
        when (page) {
            KEY_VALUE_ACCOUNT_ADD_INVESTMENT -> {
                binding.toolbarAddWebAccount.title = "Add Investment Account"
                accountTypeID = ACCOUNT_TYPE_INVESTMENT
            }
            KEY_VALUE_ACCOUNT_EDIT_INVESTMENT -> {
                binding.toolbarAddWebAccount.title = "Edit Investment Account"
                binding.toolbarAddWebAccount.menu.findItem(R.id.action_delete).isVisible = true
                accountTypeID = ACCOUNT_TYPE_INVESTMENT
                id = arguments?.getLong(KEY_ACCOUNT_ID)!!
                fetchAccountDetails(id)
            }
            KEY_VALUE_ACCOUNT_ADD_WEB -> {
                binding.toolbarAddWebAccount.title = "Add Web Account"
                accountTypeID = ACCOUNT_TYPE_WEB
            }
            KEY_VALUE_ACCOUNT_EDIT_WEB -> {
                binding.toolbarAddWebAccount.title = "Edit Web Account"
                binding.toolbarAddWebAccount.menu.findItem(R.id.action_delete).isVisible = true
                accountTypeID = ACCOUNT_TYPE_WEB
                id = arguments?.getLong(KEY_ACCOUNT_ID)!!
                fetchAccountDetails(id)
            }
            KEY_VALUE_ACCOUNT_ADD_VIRTUAL -> {
                binding.toolbarAddWebAccount.title = "Add Virtual Account"
                accountTypeID = ACCOUNT_TYPE_VIRTUAL
            }
            KEY_VALUE_ACCOUNT_EDIT_VIRTUAL -> {
                binding.toolbarAddWebAccount.title = "Edit Virtual Account"
                binding.toolbarAddWebAccount.menu.findItem(R.id.action_delete).isVisible = true
                accountTypeID = ACCOUNT_TYPE_VIRTUAL
                id = arguments?.getLong(KEY_ACCOUNT_ID)!!
                fetchAccountDetails(id)
            }

        }
    }


    private fun getBundleData() {
        page = arguments?.getLong(KEY_ACCOUNT_PAGE)!!
        balance = arguments?.getDouble(KEY_ACCOUNT_BALANCE)!!
    }


    private fun fetchAccountDetails(id: Long) {
        val account =  addCashViewModel.getAccountByID(requireContext(),id)
        binding.tetWebAccountName.setText(account.Account_Name)
        binding.tetWebBalance.setText(account.Account_Balance.toString())
        binding.tetWebMemo.setText(account.Account_Memo)
        binding.tetWebUserID.setText(account.Account_CardNumber)

    }



    @SuppressLint("InflateParams")
    private fun initListeners() {
        binding.btnSaveWeb.setOnClickListener {
            when (page) {
                KEY_VALUE_ACCOUNT_ADD_INVESTMENT -> {
                    accountTypeID = ACCOUNT_TYPE_INVESTMENT
                    submitForm()

                }
                KEY_VALUE_ACCOUNT_EDIT_INVESTMENT -> {
                    accountTypeID = ACCOUNT_TYPE_INVESTMENT
                    id = arguments?.getLong(KEY_ACCOUNT_ID)!!
                    updateAccount(id)
                }
                KEY_VALUE_ACCOUNT_ADD_WEB -> {
                    accountTypeID = ACCOUNT_TYPE_WEB
                    submitForm()
                }
                KEY_VALUE_ACCOUNT_EDIT_WEB -> {
                    accountTypeID = ACCOUNT_TYPE_WEB
                    id = arguments?.getLong(KEY_ACCOUNT_ID)!!
                    updateAccount(id)
                }


                KEY_VALUE_ACCOUNT_ADD_VIRTUAL -> {
                    accountTypeID = ACCOUNT_TYPE_VIRTUAL
                    submitForm()

                }
                KEY_VALUE_ACCOUNT_EDIT_VIRTUAL -> {
                    accountTypeID = ACCOUNT_TYPE_VIRTUAL
                    id = arguments?.getLong(KEY_ACCOUNT_ID)!!
                    updateAccount(id)
                }
            }

        }

        binding.toolbarAddWebAccount.setOnMenuItemClickListener{
            when (it.itemId) {
                R.id.action_delete -> {
                    // navigate to add record screen
                    addCashViewModel.deleteAccount(requireContext(),id)
                    findNavController().navigate(R.id.navigation_account)
                    true
                }


                else -> true
            }
        }

        binding.btnWebAddOtherCurrency.setOnClickListener {

            val array : List<Currency> = addCashViewModel.getCurrency(requireActivity())

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

    private fun updateAccount(id: Long) {

        val account = Account(
            Account_ID = id,
            Account_Name = binding.tetWebAccountName.text.toString(),
            Account_Balance = binding.tetWebBalance.text.toString().toDouble(),
            Account_CountInNetAssets = binding.scWebCountNetAssets.isChecked,
            Account_Memo = binding.tetWebMemo.text.toString(),
            AccountType_ID = accountTypeID,
            Currency_ID = currency,
            Account_CardNumber = binding.tetWebUserID.text.toString()

        )
        addCashViewModel.getAllAccount(requireContext()).forEach { item ->
            if (item.Account_Name.equals(binding.tetWebAccountName.text.toString(), ignoreCase = true)  && (account.Account_ID != item.Account_ID)) {
                Toast.makeText(requireContext(), "Account Name already exist", Toast.LENGTH_LONG).show()
                return
            }
        }

        addCashViewModel.updateAccount(requireContext(),account)
        findNavController().navigate(R.id.navigation_account)
        Toast.makeText(requireContext(), "Update Successful", Toast.LENGTH_LONG).show()

    }

    private fun insertData() {
        val accountName = binding.tetWebAccountName.text.toString()
        val userID = binding.tetWebUserID.text.toString()
        val countInNetAsset: Boolean = binding.scWebCountNetAssets.isChecked
        val memo = binding.tetWebMemo.text.toString()
        val accountTypeID = accountTypeID
        var balance = binding.tetWebBalance.text.toString()

        if (balance.isEmpty()) {
            balance = "0.0"
        }
        val cashAccount = Account(
            Account_Name =  accountName, Account_CardNumber = userID ,Account_Balance =  balance.toDouble(),
            Account_CountInNetAssets =  countInNetAsset, Account_Memo = memo, AccountType_ID = accountTypeID, Currency_ID = currency)


        addCashViewModel.insertCash(requireActivity(), cashAccount)
        Toast.makeText(requireContext(), "Successfully Deleted Your Account", Toast.LENGTH_LONG).show()


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


    override fun onResume() {
        super.onResume()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


}