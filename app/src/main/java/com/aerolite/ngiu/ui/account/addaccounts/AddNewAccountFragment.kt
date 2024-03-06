package com.aerolite.ngiu.ui.account.addaccounts

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.text.InputFilter
import android.text.InputType
import android.text.method.DigitsKeyListener
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.aerolite.ngiu.functions.ACCOUNT_TYPE_CREDIT
import com.aerolite.ngiu.functions.KEY_ACCOUNT_ID
import com.aerolite.ngiu.functions.addDecimalLimiter
import com.aerolite.ngiu.functions.getDayOfMonthSuffix
import com.aerolite.ngiu.MainActivity
import com.aerolite.ngiu.R
import com.aerolite.ngiu.data.entities.Account
import com.aerolite.ngiu.data.entities.Currency
import com.aerolite.ngiu.data.entities.Template
import com.aerolite.ngiu.data.entities.Trans
import com.aerolite.ngiu.databinding.FragmentAccountAddGeneralBinding
import com.aerolite.ngiu.functions.*
import com.google.android.material.textfield.TextInputEditText

class AddNewAccountFragment : Fragment() {
    private var _binding: FragmentAccountAddGeneralBinding? = null
    private val binding get() = _binding!!

    private lateinit var addNewAccountViewModel: AddNewAccountModel
    var currency = "USD"
    private var statementDay = "1"
    private var paymentDay = "26"




    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        addNewAccountViewModel = ViewModelProvider(this)[AddNewAccountModel::class.java]
        _binding = FragmentAccountAddGeneralBinding.inflate(inflater, container, false)


        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // get Bundle Data
        getBundleData()

        // decimal limit 2
        view.findViewById<TextInputEditText>(R.id.ti_add_general_credit_limit).addDecimalLimiter()
        view.findViewById<TextInputEditText>(R.id.ti_add_general_balance).addDecimalLimiter()

        binding.toolbarAddGeneral.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
        (activity as MainActivity).setNavBottomBarVisibility(View.GONE)
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }



    /********************************* Private Function **************************************/
    /****************************************************************************************/

    private fun getBundleData() {
        val acctType = arguments?.getLong(KEY_ACCOUNT_TYPE)!!
        val mode = arguments?.getInt(KEY_ACCOUNT_MODE)!!
        val acctID = arguments?.getLong(KEY_ACCOUNT_ID)!!

        // display Account item
        displayPage(acctType, mode, acctID)

        //
        initListeners(acctType, acctID)
    }


    private fun displayPage(accountType: Long, mode: Int, accountID: Long) {

        when (accountType) {
            ACCOUNT_TYPE_CASH -> {
                binding.lyAddGeneralCardNumberUserId.visibility = View.GONE
                binding.toolbarAddGeneral.title =
                    if (mode == EDIT_MODE) getString(R.string.nav_title_edit_cash)
                    else getString(R.string.nav_title_add_cash)
            }

            ACCOUNT_TYPE_CREDIT -> {
                binding.toolbarAddGeneral.title =
                    if (mode == EDIT_MODE) getString(R.string.nav_title_edit_credit_card)
                    else getString(R.string.nav_title_add_credit_card)
                binding.lyAddGeneralCardNumberUserId.hint = getString(R.string.option_title_card_number)
                binding.tiAddGeneralCardNumberUserId.inputType = InputType.TYPE_CLASS_NUMBER
                binding.lyAddGeneralCardNumberUserId.counterMaxLength = CARD_NUMBER_MAX_LENGTH
                binding.tiAddGeneralCardNumberUserId.addDecimalLimiter(0, CARD_NUMBER_MAX_LENGTH)

                binding.lyAddGeneralStatementDay.visibility = View.VISIBLE
                binding.lyAddGeneralPaymentDay.visibility = View.VISIBLE
                binding.lyAddGeneralCreditLimit.visibility = View.VISIBLE
            }

            ACCOUNT_TYPE_DEBIT -> {
                binding.toolbarAddGeneral.title =
                    if (mode == EDIT_MODE) getString(R.string.nav_title_edit_debit_card)
                    else getString(R.string.nav_title_add_debit_card)
                binding.lyAddGeneralCardNumberUserId.hint = getString(R.string.option_title_card_number)
                binding.tiAddGeneralCardNumberUserId.inputType = InputType.TYPE_CLASS_NUMBER
                binding.lyAddGeneralCardNumberUserId.counterMaxLength = CARD_NUMBER_MAX_LENGTH
                binding.tiAddGeneralCardNumberUserId.addDecimalLimiter(0, CARD_NUMBER_MAX_LENGTH)
            }

            ACCOUNT_TYPE_INVESTMENT -> {
                binding.toolbarAddGeneral.title =
                    if (mode == EDIT_MODE) getString(R.string.nav_title_edit_investment_account)
                    else getString(R.string.nav_title_add_investment_account)
            }

            ACCOUNT_TYPE_WEB -> {
                binding.toolbarAddGeneral.title =
                    if (mode == EDIT_MODE) getString(R.string.nav_title_edit_web_account)
                    else getString(R.string.nav_title_add_web_account)
            }

            ACCOUNT_TYPE_STORED -> {
                binding.toolbarAddGeneral.title =
                    if (mode == EDIT_MODE) getString(R.string.nav_title_edit_store_value_card)
                    else getString(R.string.nav_title_add_store_value_card)
                binding.lyAddGeneralCardNumberUserId.visibility = View.GONE
            }

            ACCOUNT_TYPE_VIRTUAL -> {
                binding.toolbarAddGeneral.title =
                    if (mode == EDIT_MODE) getString(R.string.nav_title_edit_virtual_account)
                    else getString(R.string.nav_title_add_virtual_account)
            }

            ACCOUNT_TYPE_ASSETS -> {
                binding.toolbarAddGeneral.title =
                    if (mode == EDIT_MODE) getString(R.string.nav_title_edit_fixed_assets)
                    else getString(R.string.nav_title_add_fixed_assets)
                binding.lyAddGeneralCardNumberUserId.visibility = View.GONE
            }

            ACCOUNT_TYPE_RECEIVABLE -> {
                binding.toolbarAddGeneral.title =
                    if (mode == EDIT_MODE) getString(R.string.nav_title_edit_receivable_payable)
                    else getString(R.string.nav_title_add_receivable_payable)
                binding.lyAddGeneralCardNumberUserId.visibility = View.GONE
            }
        }


        // Delete Button
        if (mode == EDIT_MODE){
            binding.toolbarAddGeneral.menu.findItem(R.id.action_delete).isVisible = true
            fetchAccountDetails(accountID)
        }
    }


    @SuppressLint("SetTextI18n")
    private fun fetchAccountDetails(id: Long) {
        val account =  addNewAccountViewModel.getAccountByID(id)
        binding.tiAddGeneralAccountName.setText(account.Account_Name)
        binding.tiAddGeneralCardNumberUserId.setText(account.Account_CardNumber)
        binding.tiAddGeneralBalance.setText("%.2f".format(account.Account_Balance))
        binding.tiAddGeneralMemo.setText(account.Account_Memo)
        binding.tiAddGeneralCreditLimit.setText("%.2f".format(account.Account_CreditLimit))
        /*"Statement Day: $statementDay$suffix"*/
        statementDay = account.Account_StatementDay.toString()
        val stateSuffix = getDayOfMonthSuffix(account.Account_StatementDay)
        binding.tvAddGeneralStatementDay.text = getString(R.string.option_account_statement_date)
                                .plus("${account.Account_StatementDay}$stateSuffix")
        paymentDay = account.Account_PaymentDay.toString()
        val paySuffix = getDayOfMonthSuffix(account.Account_PaymentDay)
        binding.tvAddGeneralPaymentDay.text = "Payment Day: ${account.Account_PaymentDay}$paySuffix"
        binding.lyAddGeneralBalance.suffixText = account.Currency_ID
        binding.lyAddGeneralCreditLimit.suffixText = account.Currency_ID
        binding.scAddGeneralCountInNetAssets.isChecked = account.Account_CountInNetAssets
    }



    @SuppressLint("InflateParams", "SetTextI18n")
    private fun initListeners(acctType: Long, acctID: Long) {
        // save button
        binding.btnAddGeneralSave.setOnClickListener {
            submitForm(acctType, acctID)
        }

        //
        binding.toolbarAddGeneral.setOnMenuItemClickListener{
            when (it.itemId) {
                R.id.action_delete -> {

                    // delete account
                    deleteAccount(requireContext(), acctID)

                    true
                }


                else -> true
            }
        }


        binding.tvAddGeneralStatementDay.setOnClickListener {
            val array = (1..31).map{ it.toString() }.toTypedArray()

            // Initialize a new instance of alert dialog builder object
            val builder = AlertDialog.Builder(requireContext())

            // set Title Style
            val titleView = layoutInflater.inflate(R.layout.popup_title, null)
            // set Title Text
            titleView.findViewById<TextView>(R.id.tv_popup_title_text).text = getText(R.string.option_statement_day)

            builder.setCustomTitle(titleView)

            // Set items form alert dialog
            builder.setItems(array) { _, which ->
                statementDay = array[which]
                val suffix = getDayOfMonthSuffix(statementDay.toInt())
                binding.tvAddGeneralStatementDay.text =
                    getString(R.string.option_account_statement_date) + statementDay + suffix
            }

            // Create a new AlertDialog using builder object
            // Finally, display the alert dialog
            builder.create().show()
        }

        binding.tvAddGeneralPaymentDay.setOnClickListener {
            val array = (1..31).map{ it.toString() }.toTypedArray()

            // Initialize a new instance of alert dialog builder object
            val builder = AlertDialog.Builder(requireContext())

            // set Title Style
            val titleView = layoutInflater.inflate(R.layout.popup_title, null)
            // set Title Text
            titleView.findViewById<TextView>(R.id.tv_popup_title_text).text = getText(R.string.option_statement_day)

            builder.setCustomTitle(titleView)

            // Set items form alert dialog
            builder.setItems(array) { _, which ->
                paymentDay = array[which]
                val suffix = getDayOfMonthSuffix(paymentDay.toInt())
                binding.tvAddGeneralPaymentDay.text = "Payment Day: \u00A0 \u00A0$paymentDay$suffix"
            }

            // Create a new AlertDialog using builder object
            // Finally, display the alert dialog
            builder.create().show()
        }


        binding.btnAddGeneralOtherCurrency.setOnClickListener {

            val array: List<Currency> = addNewAccountViewModel.getCurrency()

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
                binding.lyAddGeneralCreditLimit.suffixText = currency
                binding.lyAddGeneralBalance.suffixText = currency
            }

            // Create a new AlertDialog using builder object
            // Finally, display the alert dialog
            builder.create().show()
        }

        binding.btnAddGeneralOtherCurrency.setOnClickListener {

            val array: List<Currency> = addNewAccountViewModel.getCurrency()

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
                binding.lyAddGeneralBalance.suffixText = currency
                binding.lyAddGeneralCreditLimit.suffixText = currency

            }

            // Create a new AlertDialog using builder object
            // Finally, display the alert dialog
            builder.create().show()
        }
    }


    private fun insertData(acctType: Long, acctID: Long = 0L) {

        if (acctID == 0L){
            addNewAccountViewModel.getAllAccount().forEach { item ->
                if (item.Account_Name.equals(binding.tiAddGeneralAccountName.text.toString(), ignoreCase = true)) {
                    Toast.makeText(requireContext(), "Account Name already exist", Toast.LENGTH_LONG)
                        .show()
                    return
                }
            }
        }

        var creditLimit = binding.tiAddGeneralCreditLimit.text.toString()
        var balance = binding.tiAddGeneralBalance.text.toString()

        if (balance.isEmpty()) {
            balance = "0.0"
        }

        if (creditLimit.isEmpty()) {
            creditLimit = "1000"
        }


        val account = Account(
            Account_ID = acctID,
            Account_Name = binding.tiAddGeneralAccountName.text.toString(),
            Account_CardNumber = binding.tiAddGeneralCardNumberUserId.text.toString(),
            Account_CreditLimit = creditLimit.toDouble(),
            Account_Balance = balance.toDouble() *
                    if (acctType == ACCOUNT_TYPE_CREDIT) -1 else 1 ,
            Account_CountInNetAssets = binding.scAddGeneralCountInNetAssets.isChecked,
            Account_Memo = binding.tiAddGeneralMemo.text.toString(),
            AccountType_ID = acctType,
            Currency_ID = currency,
            Account_BaseReward = 1.0,
            Account_StatementDay = statementDay.toInt(),
            Account_PaymentDay = paymentDay.toInt()
        )

        // save to database
        addNewAccountViewModel.insertAccount(account)
        Toast.makeText(requireContext(), getString(R.string.msg_saved), Toast.LENGTH_LONG).show()
    }


    private fun submitForm(acctType: Long, acctID: Long = 0L) {
        binding.lyAddGeneralAccountName.helperText = validAccountName()

        val validAccountName = binding.lyAddGeneralAccountName.helperText == null

        if (validAccountName) {
            insertData(acctType, acctID)
            findNavController().popBackStack()
            findNavController().popBackStack()

        } else
            invalidForm(requireContext(), binding.lyAddGeneralAccountName)
    }



    private fun validAccountName(): String? {
        val accountNameText = binding.tiAddGeneralAccountName.text.toString()
        if (accountNameText.length < 3) {
            return "Minimum of 3 Characters required."
        }
        return null
    }


    /** Delete Account **/
    private fun deleteAccount(context: Context, acctID: Long) {

        // set Title Style
        val titleView = layoutInflater.inflate(R.layout.popup_title,null)
        // set Title Text
        titleView.findViewById<TextView>(R.id.tv_popup_title_text).text = getString(R.string.msg_Title_prompt)

        // set msg text
        val msg = getString(R.string.msg_content_account_delete)

        // Set Dialog
        val dialog = AlertDialog.Builder(activity)
            .setMessage(msg)
            .setCancelable(true)
            // Right Button
            .setPositiveButton(getString(R.string.msg_button_confirm)) { _, _ ->

                if (acctID > 0L) {
                    // delete account
                    addNewAccountViewModel.deleteAccount(acctID)

                    // todo make sure the account only can be deleted with zero record
                    findNavController().navigate(R.id.navigation_account)
                    Toast.makeText(requireContext(), getString(R.string.msg_content_account_delete_success), Toast.LENGTH_LONG).show()
                }

            }
            // Left Button
            .setNegativeButton(getString(R.string.msg_button_cancel)) { dialog, _ ->
                // cancel
                dialog.cancel()
            }
            .create()

        // Set View
        dialog.setCustomTitle(titleView)
        // Show Dialog
        dialog.show()

        // button text color
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(context, R.color.app_button_text_highlight))
        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(ContextCompat.getColor(context, R.color.app_button_text))
    }
}
