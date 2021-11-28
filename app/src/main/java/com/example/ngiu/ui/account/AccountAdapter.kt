package com.example.ngiu.ui.account


import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ngiu.R
import com.example.ngiu.data.entities.Account
import com.example.ngiu.functions.changeColor
import com.example.ngiu.functions.toDayLeft
import com.example.ngiu.functions.toStatementDate
import kotlinx.android.synthetic.main.cardview_account_cash_item.view.*
import kotlinx.android.synthetic.main.cardview_account_credit_card_item.view.*


class AccountAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val accounts = ArrayList<Account>()

    private val VIEW_TYPE_CASH: Int = 0
    private val VIEW_TYPE_CREDIT: Int = 1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == VIEW_TYPE_CASH) {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.cardview_account_cash_item, parent, false);
            return CashViewHolder(view)
        }
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.cardview_account_credit_card_item, parent, false);
        return CreditViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = accounts[position]
        if (holder is CashViewHolder) {
            val viewHolder: CashViewHolder = holder
            viewHolder.accountTypeTitle.text = item.Account_Name
            changeColor(viewHolder.accountCashBalance, item.Account_Balance)


        } else
            if (holder is CreditViewHolder) {
                val viewHolder: CreditViewHolder = holder;
                viewHolder.creditAccountName.text = item.Account_Name
                viewHolder.cardNumber.text = item.Account_CardNumber.substring(item.Account_CardNumber.length-4,item.Account_CardNumber.length)
                viewHolder.currentCreditBalance.text = "%.2f".format(item.Account_Balance)
                //viewHolder.currentCreditBalance.setTextColor(Color.RED)
                //changeColor(viewHolder.currentCreditBalance, item.Account_Balance)


                if(item.Account_Balance == 0.0){
                    viewHolder.numberOfDays.text = toDayLeft(item.Account_StatementDay)
                    viewHolder.date.text = toStatementDate(item.Account_StatementDay)
                    viewHolder.creditStatementDay.visibility = View.VISIBLE
                    viewHolder.creditPaymentDay.visibility = View.GONE
                }
                else{
                    viewHolder.numberOfDays.text = toDayLeft(item.Account_PaymentDay)
                    viewHolder.date.text = toStatementDate(item.Account_PaymentDay)
                    viewHolder.creditStatementDay.visibility = View.GONE
                    viewHolder.creditPaymentDay.visibility = View.VISIBLE
                }
            }
    }


    override fun getItemViewType(position: Int): Int {
        val item: Account = accounts[position]
        if (item.AccountType_ID == 1L) {
            return VIEW_TYPE_CASH
        }
        return VIEW_TYPE_CREDIT
    }

    override fun getItemCount(): Int {
        return accounts.size
    }


    @SuppressLint("NotifyDataSetChanged")
    fun addItems(data: List<Account>){
        accounts.clear()
        accounts.addAll(data)
        notifyDataSetChanged()
    }

    //cash viewholder
    inner class CashViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val accountTypeTitle: TextView = itemView.tvCashAccountName
        val accountCashBalance: TextView = itemView.tvAccountCashBalance

    }

    //credit viewholder
    inner class CreditViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val creditAccountName: TextView = itemView.tvCreditAccountName
        val cardNumber: TextView = itemView.tvCardNumber
        val currentCreditBalance: TextView = itemView.tvCreditAccountBalance
        val numberOfDays: TextView = itemView.tvNumberOfDays
        val date: TextView = itemView.tvStatementOrPaymentDate
        val creditPaymentDay: TextView = itemView.tvCreditPaymentDay
        val creditStatementDay: TextView = itemView.tvCreditStatementDay
    }

}