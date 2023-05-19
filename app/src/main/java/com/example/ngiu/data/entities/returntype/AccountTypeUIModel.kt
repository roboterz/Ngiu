package com.example.ngiu.ui.account.model


import com.example.ngiu.data.entities.Account
import com.example.ngiu.data.entities.returntype.RecordDetail
import java.time.LocalDateTime
import java.time.LocalDateTime.now


data class AccountTypeUIModel(val Name: String, val Memo: String? = null)



data class AccountSectionUiModel(
    val accountTypeID: Long,
    val title: String,
    val balance: String,
    var isExpanded: Boolean,
    val list: List<Account>
)



data class AccountTransRecordModel(
    val name: String,
    val trans_amount: String,
    val balance: String,
    val date: String,
    val trans_type_id: Long,
    val recipient_ID: Long,
    val id: Long,
    val account_ID: Long

)

data class AccountCreditDetailGroupModel(
    var TermStartDate: LocalDateTime = now(),
    var TermEndDate: LocalDateTime = now(),
    // false: statement Not Generated; true: amount due
    var StatementStatus: Boolean = false,
    var DueAmount: Double = 0.00,
    var IsExpanded: Boolean = false,
    val CDList: MutableList<RecordDetail> = ArrayList()
)