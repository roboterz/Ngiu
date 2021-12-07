package com.example.ngiu.ui.account.detail

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ngiu.R
import com.example.ngiu.data.entities.Account
import com.example.ngiu.data.entities.Trans
import com.example.ngiu.ui.account.model.AccountTransRecordModel
import kotlinx.android.synthetic.main.cardview_account_records.view.*

class AccountPRDetailAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    private val trans = ArrayList<AccountTransRecordModel>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.cardview_account_records, parent, false)
        return AccountRecordsViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = trans[position]
        val viewHolder = holder as AccountPRDetailAdapter.AccountRecordsViewHolder

        viewHolder.transRecordName.text = item.name
        viewHolder.transRecordBalance.text = item.balance
        viewHolder.transRecordTransAmount.text = item.trans_amount
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

