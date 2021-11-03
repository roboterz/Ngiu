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
import com.example.ngiu.databinding.FragmentAddVirtualAccountBinding
import com.example.ngiu.databinding.FragmentAddWebAccountBinding
import com.example.ngiu.functions.addDecimalLimiter
import com.google.android.material.textfield.TextInputEditText
import kotlinx.android.synthetic.main.popup_title.view.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class AddWebAccountFragment : Fragment() {
    private var _binding: FragmentAddWebAccountBinding? = null
    private val binding get() = _binding!!
    var currency = "USD"

    private val viewModel: AddWebAccountViewModel by lazy {
        ViewModelProvider(this).get(AddWebAccountViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return FragmentAddWebAccountBinding.inflate(inflater, container, false).run {
            _binding = this
            root
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListeners()
        getView()?.findViewById<TextInputEditText?>(R.id.tetWebBalance)?.addDecimalLimiter()
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun initListeners() {
        binding.btnSaveWeb.setOnClickListener {
            val accountName = binding.tetWebAccountName.text.toString()
            val userID = binding.tetWebUserID.text.toString()
            val balance = binding.tetWebBalance.text.toString()
            val countInNetAsset: Boolean = binding.scWebCountNetAssets.isChecked
            val memo = binding.tetWebMemo.text.toString()
            val accountTypeID = 5L
            val webAccount = Account(
                Account_Name = accountName,Account_CardNumber = userID, Account_Balance = balance.toDouble()
                ,Account_CountInNetAssets = countInNetAsset, Account_Memo = memo,
                AccountType_ID = accountTypeID, Currency_ID = currency
            )
            GlobalScope.launch {
                viewModel.insertWebAccount(requireActivity(), webAccount)
            }
        }

        binding.btnWebAddOtherCurrency.setOnClickListener {
         
            val array : List<Currency> = viewModel.getCurrency(requireActivity())

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