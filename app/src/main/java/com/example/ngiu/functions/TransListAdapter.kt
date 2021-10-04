package com.example.ngiu.functions

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.room.TypeConverters
import com.example.ngiu.R
import com.example.ngiu.data.entities.DateTypeConverter
import com.example.ngiu.data.entities.Trans
import kotlinx.android.synthetic.main.transaction_cardview.view.*
import java.util.*


class TransListAdapter(private val trans: MutableList<Trans>)
    : RecyclerView.Adapter<TransListAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // inflate the custom view from xml layout file
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.transaction_cardview,parent,false)


        // return the view holder
        return ViewHolder(view)

    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // display the custom class
        trans[position].apply {
            holder.day.text = "$ID"
            //holder.week.text =""
            //holder.dailyIncome.text =""
            //holder.dailyExpense.text =""
            holder.subCategory.text ="$SubCategoryID"
            //holder.time.text =""
            holder.period.text ="$PeriodID"
            holder.memo.text ="$Memo"
            holder.payer.text ="$PayerID"
            holder.person.text ="$PersonID"
            holder.merchant.text ="$MerchantID"
            holder.project.text ="$ProjectID"
            holder.amount.text ="$Amount"

            /*
            if (holder.ID.text.toString().toLong() > 10) {
                holder.ID.setTextColor(-0xffff01)
            }
            if (holder.ID.text.toString().toLong() > 20) {
                holder.ID.setTextColor(-0xff5501)
            }
            if (holder.ID.text.toString().toLong() > 30) {
                holder.ID.setTextColor(-0xff0101)
            }

             */
        }
    }


    override fun getItemCount(): Int {
        // the data set held by the adapter.
        return trans.size
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val day: TextView = itemView.tvTrans_day
        val week: TextView = itemView.tvTrans_week
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
    }


    // this two methods useful for avoiding duplicate item
    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }
}