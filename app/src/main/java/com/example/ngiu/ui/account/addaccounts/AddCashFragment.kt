package com.example.ngiu.ui.account.addaccounts

import android.os.Bundle
import android.provider.Settings
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.ngiu.R
import com.example.ngiu.data.entities.Account
import com.example.ngiu.data.entities.AccountType
import com.example.ngiu.databinding.FragmentAddCashBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class AddCashFragment : Fragment() {

    private lateinit var binding: FragmentAddCashBinding


    private val viewModel: AddCashViewModel by lazy {
        ViewModelProvider(this).get(AddCashViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddCashBinding.inflate(inflater, container, false)
        val view = binding.root
        initListeners()


        return view
    }


    private fun initListeners() {
        binding.btnSaveCash.setOnClickListener {
            val accountName = binding.tetCashAccountName.text.toString()
            val balance = binding.tetCashBalance.text.toString()
            val countInNetAsset: Boolean = binding.scCashCountNetAssets.isChecked
            val memo = binding.tetCashMemo.text.toString()

            val account: Account = Account(Account_Name =  accountName, Account_Balance =  balance.toDouble(),
                Account_CountInNetAssets =  countInNetAsset, Account_Memo = memo, AccountType_ID = 2, Currency_ID = "USD")

            GlobalScope.launch {
                viewModel.insertData(requireActivity(), account)
            }


        }
    }


}