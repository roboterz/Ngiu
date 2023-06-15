package com.example.ngiu.ui.account.list


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


class AccountListAdapter(
    private val onClick: OnClickListener
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val accounts = ArrayList<Account>()

    // interface for passing the onClick event to fragment.
    interface OnClickListener {
        fun onItemClick(AccountName: String)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.cardview_account_cash_item, parent, false)
        return CashViewHolder(view)


    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = accounts[position]
        if (holder is CashViewHolder) {
            val viewHolder: CashViewHolder = holder
            viewHolder.accountTypeTitle.text = item.Account_Name
            //changeColor(viewHolder.accountCashBalance, item.Account_Balance)

        } else if (holder is CreditViewHolder) {
            val viewHolder: CreditViewHolder = holder

            // Account Name
            viewHolder.creditAccountName.text = item.Account_Name

            // Account Number
            if (item.Account_CardNumber.isNotEmpty()) {
                viewHolder.cardNumber.text = item.Account_CardNumber.takeLast(4)
                viewHolder.cardNumber.visibility = View.VISIBLE
            }

            // balance
            //viewHolder.currentCreditBalance.text = get2DigitFormat(item.Account_Balance )
            //changeColor(viewHolder.currentCreditBalance, item.Account_Balance )

        }


        holder.itemView.setOnClickListener {
            onClick.onItemClick(item.Account_Name)
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