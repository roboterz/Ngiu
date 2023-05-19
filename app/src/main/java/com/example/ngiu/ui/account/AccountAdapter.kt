package com.example.ngiu.ui.account


import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.ngiu.R
import com.example.ngiu.data.entities.Account
import com.example.ngiu.functions.*
import com.example.ngiu.functions.changeColor
import com.example.ngiu.functions.toDayLeft
import com.example.ngiu.functions.toStatementDate
import kotlinx.android.synthetic.main.cardview_account_cash_item.view.*
import kotlinx.android.synthetic.main.cardview_account_credit_card_item.view.*
import kotlin.math.roundToInt


class AccountAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val accounts = ArrayList<Account>()




    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return when (viewType) {
            VIEW_TYPE_CASH -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.cardview_account_cash_item, parent, false)
                CashViewHolder(view)
            }
            VIEW_TYPE_CREDIT -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.cardview_account_credit_card_item, parent, false)
                CreditViewHolder(view)
            }
            else -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.cardview_account_cash_item, parent, false)
                CashViewHolder(view)
            }
        }


    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = accounts[position]
        if (holder is CashViewHolder) {
            val viewHolder: CashViewHolder = holder
            viewHolder.accountTypeTitle.text = item.Account_Name
            changeColor(viewHolder.accountCashBalance, item.Account_Balance)

        } else if (holder is CreditViewHolder) {
            val viewHolder: CreditViewHolder = holder
            viewHolder.creditAccountName.text = item.Account_Name
            viewHolder.currentCreditBalance.text = "%.2f".format((item.Account_Balance * 100).roundToInt().toDouble() / 100)
            if (item.Account_CardNumber.length > 4) {
                viewHolder.cardNumber.text = item.Account_CardNumber.substring(
                    item.Account_CardNumber.length - 4,
                    item.Account_CardNumber.length
                )
                viewHolder.cardNumber.visibility = View.VISIBLE
            } else {
                viewHolder.cardNumber.text = item.Account_CardNumber
                viewHolder.cardNumber.visibility = View.VISIBLE
            }
            changeColor(viewHolder.currentCreditBalance, (item.Account_Balance * 100).roundToInt().toDouble() / 100)
            // credit card
            if (item.AccountType_ID == ACCOUNT_TYPE_CREDIT) {
                if (item.Account_Balance >= 0.0) {
                    viewHolder.numberOfDays.text = toDayLeft(item.Account_StatementDay)
                    viewHolder.date.text = toStatementDate(item.Account_StatementDay)
                    viewHolder.creditStatementDay.visibility = View.VISIBLE
                } else {
                    viewHolder.numberOfDays.text = toDayLeft(item.Account_PaymentDay)
                    viewHolder.date.text = toStatementDate(item.Account_PaymentDay)
                    viewHolder.creditPaymentDay.visibility = View.GONE
                }
                // debit card
            } else if (item.AccountType_ID == ACCOUNT_TYPE_DEBIT) {
                viewHolder.numberOfDays.visibility = View.GONE
                viewHolder.date.visibility = View.GONE
                viewHolder.numberOfDays.visibility = View.GONE
                viewHolder.daysLeft.visibility = View.GONE
            }
        }


        holder.itemView.setOnClickListener {
            val bundle = Bundle().apply {
                putLong(KEY_ACCOUNT_ID, item.Account_ID)
                putString(KEY_ACCOUNT_NAME, item.Account_Name)
                putDouble(KEY_ACCOUNT_BALANCE, item.Account_Balance)
                putDouble(KEY_ACCOUNT_LIMIT, item.Account_CreditLimit)
                putInt(KEY_ACCOUNT_PAYMENT_DAY, item.Account_PaymentDay)
                putInt(KEY_ACCOUNT_STATEMENT_DATE, item.Account_StatementDay)
                putLong(KEY_ACCOUNT_TYPE,item.AccountType_ID)

            }

            when (item.AccountType_ID) {
                ACCOUNT_TYPE_CREDIT -> {
                    holder.itemView.findNavController().navigate(R.id.accountCreditRecords, bundle)
                }
                ACCOUNT_TYPE_RECEIVABLE -> {
                    holder.itemView.findNavController().navigate(R.id.accountRecordsPR, bundle)
                }
                else -> {
                    holder.itemView.findNavController().navigate(R.id.accountRecords, bundle)
                }
            }
        }
    }


    override fun getItemViewType(position: Int): Int {
        val item: Account = accounts[position]
       return when(item.AccountType_ID) {
           ACCOUNT_TYPE_CASH -> VIEW_TYPE_CASH
           ACCOUNT_TYPE_CREDIT, ACCOUNT_TYPE_DEBIT -> VIEW_TYPE_CREDIT
            else -> VIEW_TYPE_CASH
        }
    }

    override fun getItemCount(): Int {
        return accounts.size
    }

    // clear data to add new as data is changed
    @SuppressLint("NotifyDataSetChanged")
    fun addItems(data: List<Account>) {
        accounts.clear()
        accounts.addAll(data)
        notifyDataSetChanged()
    }

    // cash/everything view-holder
    inner class CashViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val accountTypeTitle: TextView = itemView.tvCashAccountName
        val accountCashBalance: TextView = itemView.tvAccountCashBalance

    }

    // credit view-holder
    inner class CreditViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val creditAccountName: TextView = itemView.tvCreditAccountName
        val cardNumber: TextView = itemView.tvCardNumber
        val currentCreditBalance: TextView = itemView.tvCreditAccountBalance
        val numberOfDays: TextView = itemView.tvNumberOfDays
        val date: TextView = itemView.tvStatementOrPaymentDate
        val creditPaymentDay: TextView = itemView.tvCreditPaymentDay
        val creditStatementDay: TextView = itemView.tvCreditStatementDay
        val daysLeft: TextView = itemView.tvDaysLeft

    }

}