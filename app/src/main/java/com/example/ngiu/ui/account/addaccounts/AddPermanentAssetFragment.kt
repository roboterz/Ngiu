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
import com.example.ngiu.databinding.FragmentAccountAddPermanentAssetBinding
import com.example.ngiu.functions.addDecimalLimiter
import com.google.android.material.textfield.TextInputEditText
import kotlinx.android.synthetic.main.fragment_account_add_permanent_asset.*
import kotlinx.android.synthetic.main.popup_title.view.*


class AddPermanentAssetFragment : Fragment() {

    private var _binding: FragmentAccountAddPermanentAssetBinding? = null
    private val binding get() = _binding!!
    private lateinit var addCashViewModel: AddCashViewModel
    var currency = "USD"

    private var balance: Double = 0.0
    lateinit var page: String
    var accountTypeID : Long = 0L
    var id: Long = 0L

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        addCashViewModel = ViewModelProvider(this).get(AddCashViewModel::class.java)
        _binding = FragmentAccountAddPermanentAssetBinding.inflate(inflater, container, false)
        getBundleData()
        displayPage()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListeners()
        getView()?.findViewById<TextInputEditText?>(R.id.tetPermaAssetsValue)?.addDecimalLimiter()

        toolbarAddPermanentAssets.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        (activity as MainActivity).setNavBottomBarVisibility(View.GONE)
    }

    private fun displayPage() {
        when (page) {
            "add_valueCard" -> {
                binding.toolbarAddPermanentAssets.title = "Add Store Value Card"
                accountTypeID = 6L
            }
            "edit_valueCard" -> {
                binding.toolbarAddPermanentAssets.title = "Add Store Value Card"
                binding.toolbarAddPermanentAssets.menu.findItem(R.id.action_delete).isVisible = true
                accountTypeID = 6L
                id = arguments?.getLong("id")!!
                binding.tetPermaAssetsValue.isEnabled = false
                binding.btnPermaAssetsAddOtherCurrency.isEnabled = false
                fetchAccountDetails(id)
            }
            "add_perm" -> {
                binding.toolbarAddPermanentAssets.title = "Add Permanent Account"
                accountTypeID = 8L
            }
            "edit_perm" -> {
                binding.toolbarAddPermanentAssets.title = "Edit Permanent Account"
                binding.toolbarAddPermanentAssets.menu.findItem(R.id.action_delete).isVisible = true
                accountTypeID = 8L
                id = arguments?.getLong("id")!!
                binding.tetPermaAssetsValue.isEnabled = false
                binding.btnPermaAssetsAddOtherCurrency.isEnabled = false
                fetchAccountDetails(id)
            }



        }
    }

    private fun getBundleData() {
        page = arguments?.getString("page")!!
        balance = arguments?.getDouble("balance")!!
    }


    private fun fetchAccountDetails(id: Long) {
        val account = addCashViewModel.getAccountByID(requireContext(), id)
        binding.tetPermaAssetsAccountName.setText(account.Account_Name)
        binding.tetPermaAssetsValue.setText("%.2f".format(balance))
        binding.tetPermaAssetsMemo.setText(account.Account_Memo)
        binding.scPermaAssetsCountNetAssets.isChecked = account.Account_CountInNetAssets



    }


    private fun initListeners() {
        binding.btnSavePermaAssets.setOnClickListener {
            when (page) {
                "add_valueCard" -> {
                    submitForm()
                }
                "edit_valueCard" -> {
                    id = arguments?.getLong("id")!!
                    updateAccount(id)
                }
                "add_perm" -> {
                    submitForm()
                }
                "edit_perm" -> {

                    id = arguments?.getLong("id")!!
                    updateAccount(id)
                }

            }
        }

        binding.toolbarAddPermanentAssets.setOnMenuItemClickListener{
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

        binding.btnPermaAssetsAddOtherCurrency.setOnClickListener {

            val array : List<Currency> = addCashViewModel.getCurrency(requireActivity())

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
                binding.permaAssetsBalanceTextLayout.suffixText = currency

            }

            // Create a new AlertDialog using builder object
            // Finally, display the alert dialog
            builder.create().show()
        }
    }

    private fun updateAccount(id: Long) {

        val account = Account(
            Account_ID = id,
            Account_Name = binding.tetPermaAssetsAccountName.text.toString(),
            Account_Balance = binding.tetPermaAssetsValue.text.toString().toDouble(),
            Account_CountInNetAssets = binding.scPermaAssetsCountNetAssets.isChecked,
            Account_Memo = binding.tetPermaAssetsMemo.text.toString(),
            AccountType_ID = accountTypeID,
            Currency_ID = currency
        )
        addCashViewModel.getAllAccount(requireContext()).forEach { item ->
            if (item.Account_Name.equals(binding.tetPermaAssetsAccountName.text.toString(), ignoreCase = true)  && (account.Account_ID != item.Account_ID)) {
                Toast.makeText(requireContext(), "Account Name already exist", Toast.LENGTH_LONG).show()
                return
            }
        }
        addCashViewModel.updateAccount(requireContext(), account)
        findNavController().navigate(R.id.navigation_account)
        Toast.makeText(requireContext(), "Update Successful", Toast.LENGTH_LONG).show()

    }

    private fun insertData() {
        val accountName = binding.tetPermaAssetsAccountName.text.toString()
        val countInNetAsset: Boolean = binding.scPermaAssetsCountNetAssets.isChecked
        val memo = binding.tetPermaAssetsMemo.text.toString()
        val accountTypeID = accountTypeID
        var value = binding.tetPermaAssetsValue.text.toString()

        if (value.isEmpty()) {
            value = "0.0"
        }
        val cashAccount = Account(
            Account_Name =  accountName, Account_Balance =  value.toDouble(),
            Account_CountInNetAssets =  countInNetAsset, Account_Memo = memo, AccountType_ID = accountTypeID, Currency_ID = currency)


        addCashViewModel.insertCash(requireActivity(), cashAccount)
        Toast.makeText(requireContext(), "Successfully added your account",Toast.LENGTH_LONG).show()

    }

    private fun submitForm() {
        binding.permaAssetsAccountNameTextLayout.helperText = validAccountName()


        val validAccountName = binding.permaAssetsAccountNameTextLayout.helperText == null


        if (validAccountName  ) {
            insertData()
            findNavController().navigate(R.id.navigation_account)

        }
        else
            invalidForm()
    }

    private fun invalidForm() {
        var message = ""
        if(binding.permaAssetsAccountNameTextLayout.helperText != null) {
            message += "\nAccountName: " + binding.permaAssetsAccountNameTextLayout.helperText
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
        val accountNameText = binding.tetPermaAssetsAccountName.text.toString()
        if(accountNameText.length < 3) {
            return "Minimum of 3 Characters required."
        }
        return null
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }




}