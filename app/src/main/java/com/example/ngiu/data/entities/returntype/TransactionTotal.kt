package com.example.ngiu.data.entities.returntype

data class TransactionTotal(
    val Transaction_ID: Long,
    val TransactionType_ID: Long,
    val SubCategory_Name: String,
    val Account_Name: String,
    val AccountRecipient_Name: String,
    val Transaction_Amount: Double


){
    constructor() : this(
        0,
        0,
        "",
        "",
        "",
        0.0,
        )
}