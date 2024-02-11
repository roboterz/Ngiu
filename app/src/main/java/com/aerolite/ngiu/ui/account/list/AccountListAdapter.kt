package com.aerolite.ngiu.ui.account.list


import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.aerolite.ngiu.R
import com.aerolite.ngiu.data.entities.Account
import com.aerolite.ngiu.functions.*
//import kotlinx.android.synthetic.main.cardview_account_cash_item.view.*
//import kotlinx.android.synthetic.main.cardview_account_credit_card_item.view.*


class AccountListAdapter(
    private val onClick: OnClickListener
) : RecyclerView.Adapter<AccountListAdapter.ViewHolder>() {

    private val accounts = ArrayList<Account>()

    // interface for passing the onClick event to fragment.
    interface OnClickListener {
        fun onItemClick(AccountName: String)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.cardview_account_cash_item, parent, false)
        return ViewHolder(view)


    }


    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        accounts[position].apply {

            // Account Name
            holder.accountName.text = Account_Name

            // Account Number
            if (Account_CardNumber.isNotEmpty()) {
                holder.cardNumber.text = Account_CardNumber.takeLast(4)
                holder.cardNumber.visibility = View.VISIBLE
            }

            // balance
            //viewHolder.currentCreditBalance.text = get2DigitFormat(item.Account_Balance )
            //changeColor(viewHolder.currentCreditBalance, item.Account_Balance )


            holder.itemView.setOnClickListener {
                onClick.onItemClick(Account_Name)
            }

        }
    }


    override fun getItemCount(): Int {
        return accounts.size
    }

    // clear data to add new as data is changed
    @SuppressLint("NotifyDataSetChanged")
    fun addItems(data: List<Account>) {
        accounts.clear()
        accounts.addAll(data)
        notifyDataSetChanged()
    }


    // view-holder
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val accountName: TextView = itemView.findViewById(R.id.tvCashAccountName)
        val cardNumber: TextView = itemView.findViewById(R.id.tvCashCardNumber)
        val accountCashBalance: TextView = itemView.findViewById(R.id.tvAccountCashBalance)

    }

}