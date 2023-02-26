package com.example.ngiu.ui.account.addaccounts


import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.ngiu.R
import com.example.ngiu.functions.ACCOUNT_ADD_MODE
import com.example.ngiu.functions.switchToAccountAttributePage
import com.example.ngiu.ui.account.model.AccountTypeUIModel
import kotlinx.android.synthetic.main.cardview_account_add_account_item.view.*


class AddAccountAdapter(): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val accountType = ArrayList<AccountTypeUIModel>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddAccountViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.cardview_account_add_account_item, parent, false)
        return AddAccountViewHolder(view)

    }



    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = accountType[position]
        val viewHolder = holder as AddAccountViewHolder

        viewHolder.acctTypeTitle.text = item.Name
        if(!item.Memo.isNullOrBlank()){
            viewHolder.acctTypeSubTitle.text = item.Memo
            viewHolder.acctTypeSubTitle.visibility = View.VISIBLE
        }

        holder.itemView.setOnClickListener {

            // switch to new account attribute page
            switchToAccountAttributePage( holder.itemView,
                holder.absoluteAdapterPosition.toLong() + 1L,
                0,0.0, ACCOUNT_ADD_MODE )

        }
    }

    override fun getItemCount(): Int {
        return accountType.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun addItems(data: List<AccountTypeUIModel>){
        accountType.clear()
        accountType.addAll(data)
        notifyDataSetChanged()
    }

    inner class AddAccountViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val acctTypeTitle: TextView = itemView.tv_account_type_title
        val acctTypeSubTitle: TextView = itemView.tv_acct_type_subTitle

    }



}