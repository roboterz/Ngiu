package com.example.ngiu.ui.account.addaccounts


import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.ngiu.R
import com.example.ngiu.ui.account.AccountSectionAdapter
import com.example.ngiu.ui.account.model.AccountTypeUIModel
import kotlinx.android.synthetic.main.account_add_account_item.view.*


class AddAccountAdapter(): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val accountType = ArrayList<AccountTypeUIModel>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddAccountViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.account_add_account_item, parent, false)
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
            when (holder.adapterPosition) {
                0 -> holder.itemView.findNavController().navigate(R.id.addCashFragment)
                1 -> holder.itemView.findNavController().navigate(R.id.addCreditFragment)
                2 -> holder.itemView.findNavController().navigate(R.id.addDebitFragment)
                //3 -> supposed to be investment
                3 -> holder.itemView.findNavController().navigate(R.id.addDebitFragment)
                4 -> holder.itemView.findNavController().navigate(R.id.addWebAccountFragment)
                //5-> supposed to be store value
                5 -> holder.itemView.findNavController().navigate(R.id.addVirtualAccountFragment)
                6 -> holder.itemView.findNavController().navigate(R.id.addVirtualAccountFragment)
                7 -> holder.itemView.findNavController().navigate(R.id.addPermanentAssetFragment)
                //8-> supposed to be receivable/payable
                8 -> holder.itemView.findNavController().navigate(R.id.addPermanentAssetFragment)
            }

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