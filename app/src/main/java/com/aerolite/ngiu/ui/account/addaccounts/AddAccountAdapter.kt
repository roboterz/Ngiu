package com.aerolite.ngiu.ui.account.addaccounts


import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.aerolite.ngiu.R
import com.aerolite.ngiu.functions.NEW_MODE
import com.aerolite.ngiu.functions.switchToAccountAttributePage2
import com.aerolite.ngiu.data.entities.returntype.AccountTypeUIModel

//import kotlinx.android.synthetic.main.cardview_account_add_account_item.view.*


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
            switchToAccountAttributePage2( holder.itemView,
                holder.absoluteAdapterPosition.toLong() + 1L,
                0, NEW_MODE )

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
        val acctTypeTitle: TextView = itemView.findViewById(R.id.tv_account_type_title)
        val acctTypeSubTitle: TextView = itemView.findViewById(R.id.tv_acct_type_subTitle)

    }



}