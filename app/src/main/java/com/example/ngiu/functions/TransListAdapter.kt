package com.example.ngiu.functions

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Resources
import android.graphics.Color
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getColor
import androidx.core.graphics.convertTo
import androidx.core.graphics.toColor
import androidx.core.graphics.toColorLong
import androidx.recyclerview.widget.RecyclerView
import androidx.room.TypeConverters
import com.example.ngiu.R
import com.example.ngiu.data.entities.DateTypeConverter
import com.example.ngiu.data.entities.Trans
import com.google.android.material.color.MaterialColors.getColor
import com.google.android.material.internal.ContextUtils.getActivity
import kotlinx.android.synthetic.main.transaction_cardview.view.*
import kotlinx.coroutines.currentCoroutineContext
import java.text.Format
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.*
import kotlin.time.days
import kotlin.time.hours


class TransListAdapter(private val trans: MutableList<Trans>)
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
            holder.day.text = Transaction_Date.date.toString()
            holder.week.text =DateFormat.format("EEEE",Transaction_Date)
            holder.income.text = "Income"
            holder.dailyIncome.text ="$0.00"
            holder.expense.text = "Expense"
            holder.dailyExpense.text ="$0.00"
            holder.subCategory.text ="$SubCategory_ID"
            holder.time.text = DateFormat.format("HH:mm", Transaction_Date)
            holder.period.text = if (Period_ID == (0).toLong()) "" else "$Period_ID"
            holder.memo.text ="$Transaction_Memo"
            holder.payer.text = when (Account_ID.toInt()){
                                    1 -> "Chase Freedom"
                                    2 -> "Walet"
                                    else -> ""
                                }
            holder.person.text = if (Person_ID == (1).toLong()) "Myself" else "$Person_ID"
            holder.merchant.text = if (Merchant_ID == (1).toLong()) "" else "$Merchant_ID"
            holder.project.text = if (Project_ID == (1).toLong()) "" else "$Project_ID"
            holder.amount.text ="$$Transaction_Amount"

            if (holder.person.text !=""){
                holder.beforePerson.text =" • "
            }
            if (holder.merchant.text !=""){
                holder.beforeMerchant.text =" • "
            }
            if (holder.project.text !=""){
                holder.beforeProject.text =" • "
            }

            holder.amount.setTextColor(
                when (TransactionType_ID.toInt()) {
                    1 -> Color.RED
                    2 -> Color.parseColor("#29C010")
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
        val payer: TextView = itemView.tvTrans_account_pay
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