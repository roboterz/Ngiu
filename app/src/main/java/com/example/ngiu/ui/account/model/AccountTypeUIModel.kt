package com.example.ngiu.ui.account.model


import com.example.ngiu.data.entities.Account


data class AccountTypeUIModel(val Name: String, val Memo: String? = null)



data class AccountSectionUiModel(
    val title: String,
    val balance: String,
    var isExpanded: Boolean,
    val list: List<Account>
)
