package com.example.ngiu.ui.account.detail

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.ngiu.R
import com.example.ngiu.data.entities.returntype.RecordDetail
import kotlinx.android.synthetic.main.cardview_account_credit_detail_item.view.*
import java.time.format.DateTimeFormatter

class AccountCreditDetailAdapter()
    : RecyclerView.Adapter<AccountCreditDetailAdapter.ViewHolder>() {

    private var listDetail: List<RecordDetail> = ArrayList()
    private var currentAccountID: Long = 0L
    private val itemDateFormatter = DateTimeFormatter.ofPattern("MM/dd")


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // inflate the custom view from xml layout file
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.cardview_account_credit_detail_item,parent,false)


        // return the view holder
        return ViewHolder(view)

    }


    //@SuppressLint("ResourceAsColor")
    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // display the custom class
        listDetail[position].apply {
            holder.recordDate.text = Transaction_Date.format(itemDateFormatter)

            // category, amount , info
            when (TransactionType_ID){
                1L -> {
                    // category
                    holder.recordText.text = Category_Name
                    // amount
                    holder.recordAmount.text = "$" + "%.2f".format(Transaction_Amount)
                    holder.recordAmount.setTextColor(holder.expenseColor)

                }
                2L -> {
                    // category
                    holder.recordText.text = Category_Name
                    // amount
                    holder.recordAmount.text = "$" + "%.2f".format(Transaction_Amount)
                    holder.recordAmount.setTextColor(holder.incomeColor)
                }
                3L,4L ->{
                    // category
                    if (TransactionType_ID == 3L){
                        if (Account_ID == currentAccountID){
                            holder.recordText.text = holder.itemView.context.getString(R.string.record_transfer_to) + " $AccountRecipient_Name"
                        }else{
                            holder.recordText.text = holder.itemView.context.getString(R.string.record_transfer_from) + " $Account_Name"
                        }
                    }
                    if (TransactionType_ID == 4L){
                        when (Category_ID){
                            7L -> {
                                holder.recordText.text = holder.itemView.context.getString(R.string.record_borrow_from) + " $Account_Name"
                            }
                            8L -> {
                                holder.recordText.text = holder.itemView.context.getString(R.string.record_lend_to) + " $AccountRecipient_Name"
                            }
                            9L -> {
                                holder.recordText.text = holder.itemView.context.getString(R.string.record_paid_to) + " $AccountRecipient_Name"
                            }
                            10L -> {
                                holder.recordText.text = holder.itemView.context.getString(R.string.record_received_from) + " $Account_Name"
                            }
                        }
                    }
                    // amount
                    holder.recordAmount.text = "$" + "%.2f".format(Transaction_Amount)
                    holder.recordAmount.setTextColor(holder.amountColor)

                }
            }

            holder.recordMemo.text = Transaction_Memo


            holder.itemLayout.setOnClickListener {
                val bundle = Bundle().apply {
                    putLong("Transaction_ID", Transaction_ID)
                }
                // switch to record fragment
                holder.itemView.findNavController().navigate(R.id.navigation_record, bundle)
            }
        }
    }


    @SuppressLint("NotifyDataSetChanged")
    fun setList(list: List<RecordDetail>){
        listDetail = list
        notifyDataSetChanged()
    }

    fun setAccountID(acctID: Long){
        currentAccountID = acctID
    }


    override fun getItemCount(): Int {
        // the data set held by the adapter.
        return listDetail.size
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        val recordText: TextView = itemView.tv_account_credit_detail_item_category
        val recordDate: TextView = itemView.tv_account_credit_detail_item_date
        val recordMemo: TextView = itemView.tv_account_credit_detail_item_memo
        val recordAmount: TextView = itemView.tv_account_credit_detail_item_amount

        val itemLayout: ConstraintLayout = itemView.layout_account_credit_detail_card_view_item

        val expenseColor = ContextCompat.getColor(itemView.context, R.color.app_expense_amount)
        val incomeColor = ContextCompat.getColor(itemView.context, R.color.app_income_amount)
        val amountColor = ContextCompat.getColor(itemView.context, R.color.app_amount)

    }


    // this two methods useful for avoiding duplicate item
    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }
}