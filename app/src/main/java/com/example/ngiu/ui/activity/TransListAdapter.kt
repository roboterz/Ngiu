package com.example.ngiu.ui.activity

import android.annotation.SuppressLint
import android.graphics.Color
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.marginTop
import androidx.recyclerview.widget.RecyclerView
import com.example.ngiu.R
import com.example.ngiu.data.entities.list.TransactionDetail
import kotlinx.android.synthetic.main.transaction_cardview.view.*
import kotlin.collections.ArrayList


class TransListAdapter(private val trans: ArrayList<TransactionDetail>)
    : RecyclerView.Adapter<TransListAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // inflate the custom view from xml layout file
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.transaction_cardview,parent,false)


        // return the view holder
        return ViewHolder(view)

    }


    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // display the custom class
        trans[position].apply {
            holder.day.text = DateFormat.format("dd", Transaction_Date)
            holder.week.text =DateFormat.format("EEEE",Transaction_Date)
            holder.income.text = "Income"
            holder.dailyIncome.text ="$0.00"
            holder.expense.text = "Expense"
            holder.dailyExpense.text ="$0.00"
            holder.time.text = DateFormat.format("HH:mm", Transaction_Date)
            //holder.period.text = ""
            holder.memo.text ="$Transaction_Memo"
            holder.amount.text ="$$Transaction_Amount"

            if (TransactionType_Name == "Expense" || TransactionType_Name == "Income" ) {
                holder.subCategory.text = "$SubCategory_Name"
                holder.account.text = "$Account_Name"
                holder.person.text = "$Person_Name"
                holder.merchant.text = "$Merchant_Name"
                holder.project.text = "$Project_Name"
                if (holder.person.text !=""){
                    holder.beforePerson.text =" • "
                }
                if (holder.merchant.text !=""){
                    holder.beforeMerchant.text =" • "
                }
                if (holder.project.text !=""){
                    holder.beforeProject.text =" • "
                }
            }else{
                holder.account.visibility = View.GONE
                holder.person.visibility = View.GONE
                holder.merchant.visibility = View.GONE
                holder.project.visibility = View.GONE
                holder.beforeMerchant.visibility = View.GONE
                holder.beforePerson.visibility = View.GONE
                holder.beforeProject.visibility = View.GONE
            }




            if (TransactionType_Name == "Transfer"){
                holder.subCategory.text = "$Account_Name"
                holder.accountReceiver.text =" ➡️ $AccountRecipient_Name"
                holder.accountReceiver.visibility = View.VISIBLE
            } else {
                holder.accountReceiver.text = ""
            }

            if (Period_ID.toInt() != 0) {
                holder.period.text="Period"
                //holder.period.visibility = View.VISIBLE
            }

            holder.amount.setTextColor(
                when (TransactionType_Name) {
                    "Expense" -> Color.RED
                    "Income" -> Color.parseColor("#29C010")
                    else -> Color.BLACK
                }
            )


        }
    }


    override fun getItemCount(): Int {
        // the data set held by the adapter.
        return trans.size
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val day: TextView = itemView.tvTrans_day
        val week: TextView = itemView.tvTrans_week
        val expense: TextView = itemView.tvTrans_daily_expense
        val income: TextView = itemView.tvTrans_daily_income
        val dailyExpense: TextView = itemView.tvTrans_daily_expense_amount
        val dailyIncome: TextView = itemView.tvTrans_daily_income_amount
        val subCategory: TextView = itemView.tvTrans_category
        val time: TextView = itemView.tvTrans_time
        val account: TextView = itemView.tvTrans_account_pay
        val accountReceiver: TextView = itemView.tvTrans_account_receive
        val merchant: TextView = itemView.tvTrans_merchant
        val period: TextView = itemView.tvTrans_period
        val memo: TextView = itemView.tvTrans_memo
        val person: TextView = itemView.tvTrans_person
        val project: TextView = itemView.tvTrans_project
        val amount: TextView = itemView.tvTrans_amount
        val beforePerson: TextView = itemView.tvTrans_dot_before_person
        val beforeMerchant: TextView = itemView.tvTrans_dot_before_merchant
        val beforeProject: TextView = itemView.tvTrans_dot_before_project
    }


    // this two methods useful for avoiding duplicate item
    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }
}