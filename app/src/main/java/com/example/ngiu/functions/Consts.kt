package com.example.ngiu.functions


// ACCOUNT TYPE
const val ACCOUNT_TYPE_LIMIT = 9

const val ACCOUNT_TYPE_CASH = 1L
const val ACCOUNT_TYPE_CREDIT = 2L
const val ACCOUNT_TYPE_DEBIT = 3L
const val ACCOUNT_TYPE_INVESTMENT = 4L
const val ACCOUNT_TYPE_WEB = 5L
const val ACCOUNT_TYPE_STORED = 6L
const val ACCOUNT_TYPE_VIRTUAL = 7L
const val ACCOUNT_TYPE_ASSETS = 8L
const val ACCOUNT_TYPE_RECEIVABLE = 9L

// TRANSACTION TYPE
const val TRANSACTION_TYPE_EXPENSE = 1L
const val TRANSACTION_TYPE_INCOME = 2L
const val TRANSACTION_TYPE_TRANSFER = 3L
const val TRANSACTION_TYPE_DEBIT = 4L

//
const val PROJECT_DAILY = 1L
const val PERSON_ME = 1L
const val MERCHANT_NO_LOCATION = 1L
const val CURRENCY_USD = 1L

// CATEGORY
const val CATEGORY_LIMIT = 50L

const val CATEGORY_MAIN_EXPENSE = 1L
const val CATEGORY_MAIN_INCOME = 2L
const val CATEGORY_MAIN_TRANSFER = 3L
const val CATEGORY_MAIN_DEBIT = 4L
const val CATEGORY_SUB_ACCOUNT_TRANSFER = 5L
const val CATEGORY_SUB_CREDIT_PAYMENT = 6L
const val CATEGORY_SUB_DEPOSIT = 7L
const val CATEGORY_SUB_WITHDRAW = 8L
const val CATEGORY_SUB_BORROW = 9L
const val CATEGORY_SUB_LEND = 10L
const val CATEGORY_SUB_PAYMENT = 11L
const val CATEGORY_SUB_RECEIVE_PAYMENT = 12L

// REIMBURSABLE
const val NON_REIMBURSABLE = 0
const val REIMBURSABLE = 1
const val REIMBURSED = 2

// EVENT
const val EVENT_CREDIT_PAYMENT = 1
const val EVENT_PERIODIC_BILL = 2
const val EVENT_NOTE = 3

// CATEGORY EDIT
const val ADD_MAIN_CATEGORY = 0
const val EDIT_MAIN_CATEGORY = 1
const val ADD_SUB_CATEGORY = 2
const val EDIT_SUB_CATEGORY = 3

const val MAIN_CATEGORY = 0
const val SUB_CATEGORY = 1

// MPP MANAGE
const val MPP_MERCHANT = 0
const val MPP_PERSON = 1
const val MPP_PROJECT = 2

// VIEW TYPE
const val VIEW_TYPE_CASH: Int = 1
const val VIEW_TYPE_CREDIT: Int = 2

// MODE
const val NEW_MODE: Int = 1
const val EDIT_MODE: Int = 2
const val SELECT_MODE: Int = 3

// OPEN RECORD
const val RECORD_NEW = 1
const val RECORD_EDIT = 2
const val RECORD_NEW_FROM_ACCOUNT = 3

// KEY
const val KEY_RECORD = "RECORD"
const val KEY_RECORD_OPEN_TYPE = "RECORD_OPEN_TYPE"
const val KEY_RECORD_TRANSACTION_ID = "RECORD_TRANSACTION_ID"
const val KEY_RECORD_TRANSACTION_TYPE_ID = "RECORD_TRANSACTION_TYPE_ID"
const val KEY_RECORD_ACCOUNT_ID = "RECORD_ACCOUNT_ID"
const val KEY_RECORD_CATEGORY_ID = "RECORD_CATEGORY_ID"
const val KEY_RECORD_CATEGORY = "RECORD_CATEGORY"

const val KEY_ACCOUNT_PAGE = "ACCOUNT_PAGE"
const val KEY_ACCOUNT_ID = "ACCOUNT_ID"
const val KEY_ACCOUNT_BALANCE = "ACCOUNT_BALANCE"
const val KEY_ACCOUNT_NAME = "ACCOUNT_NAME"
const val KEY_ACCOUNT_LIMIT = "ACCOUNT_LIMIT"
const val KEY_ACCOUNT_PAYMENT_DAY = "ACCOUNT_PAYMENT_DAY"
const val KEY_ACCOUNT_STATEMENT_DATE = "ACCOUNT_STATEMENT_DATE"
const val KEY_ACCOUNT_TYPE = "ACCOUNT_TYPE"

const val KEY_CATEGORY_MANAGER = "CATEGORY_MANAGER"
const val KEY_CATEGORY_MANAGER_MODE = "CATEGORY_MANAGER_MODE"
const val KEY_CATEGORY_MANAGER_TRANSACTION_TYPE = "CATEGORY_MANAGER_TYPE"

// KEY VALUE
const val KEY_VALUE_ACCOUNT_ADD_CASH = 1L
const val KEY_VALUE_ACCOUNT_ADD_CREDIT = 2L
const val KEY_VALUE_ACCOUNT_ADD_DEBIT = 3L
const val KEY_VALUE_ACCOUNT_ADD_INVESTMENT = 4L
const val KEY_VALUE_ACCOUNT_ADD_WEB = 5L
const val KEY_VALUE_ACCOUNT_ADD_STORED = 6L
const val KEY_VALUE_ACCOUNT_ADD_VIRTUAL = 7L
const val KEY_VALUE_ACCOUNT_ADD_ASSETS = 8L
const val KEY_VALUE_ACCOUNT_ADD_PAYABLE = 9L

const val KEY_VALUE_ACCOUNT_EDIT_CASH = 21L
const val KEY_VALUE_ACCOUNT_EDIT_CREDIT = 22L
const val KEY_VALUE_ACCOUNT_EDIT_DEBIT = 23L
const val KEY_VALUE_ACCOUNT_EDIT_INVESTMENT = 24L
const val KEY_VALUE_ACCOUNT_EDIT_WEB = 25L
const val KEY_VALUE_ACCOUNT_EDIT_STORED = 26L
const val KEY_VALUE_ACCOUNT_EDIT_VIRTUAL = 27L
const val KEY_VALUE_ACCOUNT_EDIT_ASSETS = 28L
const val KEY_VALUE_ACCOUNT_EDIT_PAYABLE = 29L

