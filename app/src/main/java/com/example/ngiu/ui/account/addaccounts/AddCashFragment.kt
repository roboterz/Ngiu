package com.example.ngiu.ui.account.addaccounts

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.ngiu.MainActivity
import com.example.ngiu.R
import com.example.ngiu.data.entities.Account
import com.example.ngiu.data.entities.Currency
import com.example.ngiu.databinding.FragmentAccountAddCashBinding
import com.example.ngiu.functions.addDecimalLimiter
import com.google.android.material.textfield.TextInputEditText
import kotlinx.android.synthetic.main.fragment_account_add_cash.*
import kotlinx.android.synthetic.main.popup_title.view.*
import android.util.Log
import android.widget.Toast
import kotlinx.android.synthetic.main.fragment_account_add_web_account.*


class AddCashFragment : Fragment() {
    private var _binding: FragmentAccountAddCashBinding? = null
    private val binding get() = _binding!!
    private lateinit var addCashViewModel: AddCashViewModel

    var currency = "USD"
    lateinit var page: String
    var accountTypeID : Long = 0L
    var id : Long= 0L

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        addCashViewModel = ViewModelProvider(this).get(AddCashViewModel::class.java)
        _binding = FragmentAccountAddCashBinding.inflate(inflater, container, false)

        getBundleData()
        displayPage()


        return binding.root
    }

    private fun displayPage() {
        when (page) {
            "add_cash" -> {
                binding.toolbarAddCashAccount.title = "Add Cash"
                accountTypeID = 1L
            }
            "edit_cash" -> {
                binding.toolbarAddCashAccount.title = "Edit Cash"
                binding.toolbarAddCashAccount.menu.findItem(R.id.action_delete).isVisible = true
                accountTypeID = 1L

                id = arguments?.getLong("id")!!
             /*   binding.cashBalanceTextLayout.isEnabled = false
                binding.btnCashAddOtherCurrency.isEnabled = false*/
                fetchAccountDetails(id)
            }
            "add_payable" -> {
                binding.toolbarAddCashAccount.title = "Add Receivable/Payable"
                accountTypeID = 9L
            }
            "edit_payable" -> {
                binding.toolbarAddCashAccount.title = "Edit Receivable/Payable"
                binding.toolbarAddCashAccount.menu.findItem(R.id.action_delete).isVisible = true
                accountTypeID = 9L
                id = arguments?.getLong("id")!!

                fetchAccountDetails(id)
            }
        }
    }

    private fun fetchAccountDetails(id: Long) {
        val account =  addCashViewModel.getAccountByID(requireContext(),id)
        binding.tetCashAccountName.setText(account.Account_Name)
        binding.tetCashBalance.setText(account.Account_Balance.toString())
        binding.tetCashMemo.setText(account.Account_Memo)
        /*binding.scCashCountNetAssets.setText(account.Account_CountInNetAssets.toString())*/
        binding.cashBalanceTextLayout.suffixText = account.Currency_ID
        binding.scCashCountNetAssets.isChecked = account.Account_CountInNetAssets
    }

    private fun getBundleData() {
        page = arguments?.getString("page")!!

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListeners()
        getView()?.findViewById<TextInputEditText?>(R.id.tetCashBalance)?.addDecimalLimiter()

        toolbarAddCashAccount.setNavigationOnClickListener {
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
            when (page) {
                "add_cash" -> {
                    submitForm()
                }
                "edit_cash" -> {

                    var id : Long= 0L
                    id = arguments?.getLong("id")!!

                    updateAccount(id)
                }
                "add_payable" -> {
                    binding.toolbarAddCashAccount.title = "Add Receivable/Payable"
                    accountTypeID = 9L
                    submitForm()

                }
                "edit_payable" -> {
                    binding.toolbarAddCashAccount.title = "Edit Receivable/Payable"
                    var id : Long= 0L
                    id = arguments?.getLong("id")!!
                    updateAccount(id)
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
                    // navigate to add record screen
                   addCashViewModel.deleteAccount(requireContext(),id)
                    findNavController().navigate(R.id.navigation_account)
                    Toast.makeText(requireContext(), "Successfully Deleted Your Account", Toast.LENGTH_LONG).show()
                    true
                }


                else -> super.onOptionsItemSelected(it)
            }
        }

    }

    private fun updateAccount(id: Long) {

        val account = Account(
            Account_ID = id,
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
        findNavController().navigate(R.id.navigation_account)
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
            findNavController().navigate(R.id.navigation_account)
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


}