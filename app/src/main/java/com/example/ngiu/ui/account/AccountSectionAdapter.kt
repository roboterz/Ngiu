package com.example.ngiu.ui.account


import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ngiu.R
import com.example.ngiu.ui.account.model.AccountSectionUiModel
import kotlinx.android.synthetic.main.cardview_account_header_item.view.*
import kotlinx.android.synthetic.main.account_section_item.view.*


class AccountSectionAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val accountItems = ArrayList<AccountSectionUiModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.account_section_item, parent, false);
        return AccountSectionViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = accountItems[position]
        val viewHolder = holder as AccountSectionViewHolder
        viewHolder.accountHeaderType.text = item.title
        viewHolder.accountHeaderBalance.text = item.balance
        viewHolder.foldItems(item.isExpanded)
        viewHolder.topheader.setOnClickListener {
            item.isExpanded = !item.isExpanded
            viewHolder.foldItems(item.isExpanded)
        }

        val context = viewHolder.rvAccounts.context
        val adapter = AccountAdapter()
        viewHolder.rvAccounts.layoutManager = LinearLayoutManager(context)
        viewHolder.rvAccounts.adapter = adapter
        adapter.addItems(item.list)
    }

    override fun getItemCount(): Int {
        return accountItems.size
    }


    @SuppressLint("NotifyDataSetChanged")
    fun addItems(data: List<AccountSectionUiModel>){
        accountItems.clear()
        accountItems.addAll(data)
        notifyDataSetChanged()
    }

    inner class AccountSectionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val accountHeaderType: TextView = itemView.tvAccountTypeTitle
        val accountHeaderBalance: TextView = itemView.tvAccountHeaderBalance
        var expandRow: ImageView = itemView.ivExpandRow
        var rvAccounts: RecyclerView = itemView.rvAccounts
        var topheader: View = itemView.includeHeader

        // function to fold child base off the header
        fun foldItems(expand: Boolean){
            if (expand) {
                rvAccounts.visibility = View.VISIBLE
                expandRow.setImageResource(R.drawable.ic_baseline_arrow_drop_down_24)
            } else {
                rvAccounts.visibility = View.GONE
                expandRow.setImageResource(R.drawable.ic_baseline_arrow_right_24)
            }
        }
    }
}