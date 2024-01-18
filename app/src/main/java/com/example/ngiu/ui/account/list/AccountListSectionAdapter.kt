package com.example.ngiu.ui.account.list


import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ngiu.R
import com.example.ngiu.ui.account.model.AccountSectionUiModel
import com.example.ngiu.ui.activity.ActivityListAdapter
//import kotlinx.android.synthetic.main.cardview_account_section_item.view.*


class AccountListSectionAdapter(
    private val onClick: OnClickListener
    )
    : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val accountItems = ArrayList<AccountSectionUiModel>()

    // interface for passing the onClick event to fragment.
    interface OnClickListener {
        fun onItemClick(AccountName: String)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.cardview_account_section_item, parent, false);
        return AccountSectionViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = accountItems[position]
        val viewHolder = holder as AccountSectionViewHolder
        viewHolder.accountHeaderType.text = item.title
        //viewHolder.accountHeaderBalance.text = item.balance
        viewHolder.foldItems(item.isExpanded)
        viewHolder.topHeader.setOnClickListener {
            item.isExpanded = !item.isExpanded
            viewHolder.foldItems(item.isExpanded)
        }

        val context = viewHolder.rvAccounts.context

        val adapter = AccountListAdapter(object: AccountListAdapter.OnClickListener {
            override fun onItemClick(AccountName: String) {
                // select
                onClick.onItemClick(AccountName )
            }
        })

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
        val accountHeaderType: TextView = itemView.findViewById(R.id.tvAccountTypeTitle)
        val accountHeaderBalance: TextView = itemView.findViewById(R.id.tvAccountHeaderBalance)
        var expandRow: ImageView = itemView.findViewById(R.id.ivExpandRow)
        var rvAccounts: RecyclerView = itemView.findViewById(R.id.rvAccounts)
        var topHeader: View = itemView.findViewById(R.id.cardview_account_section_item)

        // function to fold child base off the header
        @SuppressLint("ResourceAsColor")
        fun foldItems(expand: Boolean){
            if (expand) {
                rvAccounts.visibility = View.VISIBLE
                expandRow.setImageResource(R.drawable.ic_baseline_arrow_drop_down_24)
                accountHeaderBalance.setTextColor(ContextCompat.getColor(itemView.context, R.color.app_sub_line_text))

            } else {
                rvAccounts.visibility = View.GONE
                expandRow.setImageResource(R.drawable.ic_baseline_arrow_right_24)
                accountHeaderBalance.setTextColor(ContextCompat.getColor(itemView.context, R.color.app_option_text))
            }
        }
    }
}