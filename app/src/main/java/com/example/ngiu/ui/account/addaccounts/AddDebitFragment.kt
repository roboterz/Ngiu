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
            val accountName = binding.tetDebitAccountName.text.toString()
            val cardNumber = binding.tetDebitCardNumber.text.toString()
            val balance = binding.tetDebitBalance.text.toString()
            val countInNetAsset: Boolean = binding.scDebitCountNetAssets.isChecked
            val memo = binding.tetDebitMemo.text.toString()
            val accountTypeID = 2L

            val debitAccount = Account(
                Account_Name = accountName, Account_CardNumber = cardNumber,
                Account_Balance = balance.toDouble(), Account_CountInNetAssets = countInNetAsset,
                Account_Memo = memo, AccountType_ID = accountTypeID, Currency_ID = currency
            )
            GlobalScope.launch {
                viewModel.insertDebit(requireActivity(), debitAccount)
            }


        }

        binding.btnDebitAddOtherCurrency.setOnClickListener {

            val array: List<Currency> = viewModel.getCurrency(requireActivity())

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
                // Get the dialog selected item
                val selected = array[which]
                currency = arrayList.get(which)
                Toast.makeText(context, "You Clicked: " + selected, Toast.LENGTH_SHORT).show()
            }

            // Create a new AlertDialog using builder object
            // Finally, display the alert dialog
            builder.create().show()
        }

    }
}