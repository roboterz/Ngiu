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
            when (holder.absoluteAdapterPosition) {
                //cash
                0 -> {
                    val bundle = Bundle().apply {
                        putString("page", "add_cash")
                    }
                    holder.itemView.findNavController().navigate(R.id.addCashFragment, bundle)
                }
                //credit
                1 -> {
                    val bundle = Bundle().apply {
                        putString("page", "add_credit")
                    }
                    holder.itemView.findNavController().navigate(R.id.addCreditFragment, bundle)
                }
                //debit
                2 -> {
                    val bundle = Bundle().apply {
                        putString("page", "add_debit")
                    }
                    holder.itemView.findNavController().navigate(R.id.addDebitFragment, bundle)
                }
                // investment account
                3 -> {
                    val bundle = Bundle().apply {
                        putString("page", "add_investment")
                    }
                    holder.itemView.findNavController().navigate(R.id.addWebAccountFragment, bundle)
                }
                // web account
                4 -> {
                    val bundle = Bundle().apply {
                        putString("page", "add_web")
                    }
                    holder.itemView.findNavController().navigate(R.id.addWebAccountFragment, bundle)
                }
                //store valued card
                5 -> {
                    val bundle = Bundle().apply {
                        putString("page", "add_valueCard")
                    }
                    holder.itemView.findNavController().navigate(R.id.addPermanentAssetFragment, bundle)
                }
                // virtual account
                6 -> {
                    val bundle = Bundle().apply {
                        putString("page", "add_virtual")
                    }
                    holder.itemView.findNavController().navigate(R.id.addWebAccountFragment, bundle)
                }
                // permanent assets
                7 -> {
                    val bundle = Bundle().apply {
                        putString("page", "add_perm")
                    }
                    holder.itemView.findNavController().navigate(R.id.addPermanentAssetFragment, bundle)
                }
                //receivable/payable
                8 ->  {
                    val bundle = Bundle().apply {
                        putString("page", "add_payable")
                    }
                    holder.itemView.findNavController().navigate(R.id.addCashFragment, bundle)}
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