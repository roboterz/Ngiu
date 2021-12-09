package com.example.ngiu.ui.account

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.ngiu.R
import com.example.ngiu.functions.changeColor
import com.example.ngiu.ui.account.model.AccountTransRecordModel
import kotlinx.android.synthetic.main.cardview_account_records.view.*

class AccountDetailAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    private val trans = ArrayList<AccountTransRecordModel>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.cardview_account_records, parent, false)
        return AccountRecordsViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = trans[position]
        val viewHolder = holder as AccountDetailAdapter.AccountRecordsViewHolder

        viewHolder.transRecordName.text = item.name
        changeColor(viewHolder.transRecordBalance, item.balance.toDouble())


        // condition to check to show +/- trans_amount and recolor to red/green base off expense/income
        if(item.trans_id == 2L || (item.trans_id == 3L || item.trans_id == 4L) &&
            (item.account_ID != item.id && item.recipient_ID == item.id))  {
            viewHolder.transRecordTransAmount.text = "+"+item.trans_amount
            viewHolder.transRecordTransAmount.setTextColor(ContextCompat.getColor(viewHolder.transRecordTransAmount.context,
                R.color.app_income_amount))
        } else {
            viewHolder.transRecordTransAmount.text = "-"+item.trans_amount
            viewHolder.transRecordTransAmount.setTextColor(ContextCompat.getColor(viewHolder.transRecordTransAmount.context,
                R.color.app_expense_amount))
        }



        viewHolder.transRecordDate.text = item.date



    }

    // clear data to add new as data is changed
    @SuppressLint("NotifyDataSetChanged")
    fun addItems(data: List<AccountTransRecordModel>) {
        trans.clear()
        trans.addAll(data)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return trans.size
    }

    inner class AccountRecordsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val transRecordName: TextView = itemView.tvbAccountRecordsName
        val transRecordTransAmount: TextView = itemView.tvAccountRecordTransaction
        val transRecordDate: TextView = itemView.tvAccountRecordDate
        val transRecordBalance: TextView = itemView.tvAccountRecordsBalance


    }


}

