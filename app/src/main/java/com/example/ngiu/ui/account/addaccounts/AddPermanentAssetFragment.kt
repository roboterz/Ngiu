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
import com.example.ngiu.databinding.FragmentAddDebitBinding
import com.example.ngiu.databinding.FragmentAddPermanentAssetBinding
import com.example.ngiu.functions.addDecimalLimiter
import com.google.android.material.textfield.TextInputEditText
import kotlinx.android.synthetic.main.popup_title.view.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class AddPermanentAssetFragment : Fragment() {

    private var _binding: FragmentAddPermanentAssetBinding? = null
    private val binding get() = _binding!!
    var currency = "USD"

    private val viewModel: AddPermanentAssetViewModel by lazy {
        ViewModelProvider(this).get(AddPermanentAssetViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return FragmentAddPermanentAssetBinding.inflate(inflater, container, false).run {
            _binding = this
            root
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListeners()
        getView()?.findViewById<TextInputEditText?>(R.id.tetPermaAssetsValue)?.addDecimalLimiter()
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


    private fun initListeners() {
        binding.btnSavePermaAssets.setOnClickListener {
            val accountName = binding.tetPermaAssetsAccountName.text.toString()
            val value = binding.tetPermaAssetsValue.text.toString()
            val countInNetAsset: Boolean = binding.scPermaAssetsCountNetAssets.isChecked
            val memo = binding.tetPermaAssetsMemo.text.toString()

            val permanentAssetAccount = Account(
                Account_Name = accountName, Account_Balance = value.toDouble()
                ,Account_CountInNetAssets = countInNetAsset, Account_Memo = memo,
                AccountType_ID = 8, Currency_ID = "USD"
            )
            GlobalScope.launch {
                viewModel.insertPermanentAssetAccount(requireActivity(), permanentAssetAccount)
            }
            binding.btnPermaAssetsAddOtherCurrency.setOnClickListener {

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


}