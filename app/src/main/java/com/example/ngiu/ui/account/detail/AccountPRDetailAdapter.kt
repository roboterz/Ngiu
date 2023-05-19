package com.example.ngiu.ui.account.detail

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.ngiu.R
import com.example.ngiu.functions.*
import com.example.ngiu.data.entities.returntype.RecordDetail
import kotlinx.android.synthetic.main.cardview_account_p_r_detail_item.view.*
import java.time.format.DateTimeFormatter

class AccountPRDetailAdapter(
    private val onClickListener: OnClickListener
    )
    : RecyclerView.Adapter<AccountPRDetailAdapter.ViewHolder>() {

        private var listPRDetail: List<RecordDetail> = ArrayList()

        private val recordTimeFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy hh:mm")
        private val groupDateFormatter = DateTimeFormatter.ofPattern("MM/yyyy")

        // interface for passing the onClick event to fragment.
        interface OnClickListener {
            fun onItemClick(transID: Long)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            // inflate the custom view from xml layout file
            val view: View = LayoutInflater.from(parent.context)
                .inflate(R.layout.cardview_account_p_r_detail_item,parent,false)


            // return the view holder
            return ViewHolder(view)

        }


        //@SuppressLint("ResourceAsColor")
        @SuppressLint("SetTextI18n")
        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            // display the custom class
            listPRDetail[position].apply {
                when (Category_ID){
                    CATEGORY_SUB_BORROW -> {
                        holder.recordText.text = holder.itemView.context.getString(R.string.record_borrow_from) + " $Account_Name"
                        holder.recordAmount.text = "+" + "%.2f".format(Transaction_Amount)
                    }
                    CATEGORY_SUB_LEND -> {
                        holder.recordText.text = holder.itemView.context.getString(R.string.record_lend_to) + " $AccountRecipient_Name"
                        holder.recordAmount.text = "-" + "%.2f".format(Transaction_Amount)
                    }
                    CATEGORY_SUB_PAYMENT -> {
                        holder.recordText.text = holder.itemView.context.getString(R.string.record_paid_to) + " $AccountRecipient_Name"
                        holder.recordAmount.text = "-" + "%.2f".format(Transaction_Amount)
                    }
                    CATEGORY_SUB_RECEIVE_PAYMENT -> {
                        holder.recordText.text = holder.itemView.context.getString(R.string.record_received_from) + " $Account_Name"
                        holder.recordAmount.text = "+" + "%.2f".format(Transaction_Amount)
                    }
                }

                holder.recordTime.text = Transaction_Date.format(recordTimeFormatter)
                holder.recordMemo.text = Transaction_Memo

                if (position == 0){
                    holder.groupLayout.visibility = View.VISIBLE
                    holder.groupDate.text = Transaction_Date.format(groupDateFormatter)
                }else{
                    if (Transaction_Date.month == listPRDetail[position-1].Transaction_Date.month) {
                        holder.groupLayout.visibility = View.GONE
                    }else{
                        holder.groupLayout.visibility = View.VISIBLE
                        holder.groupDate.text = Transaction_Date.format(groupDateFormatter)
                    }
                }

                holder.itemLayout.setOnClickListener {
                    onClickListener.onItemClick(Transaction_ID)
                }
            }
        }


        @SuppressLint("NotifyDataSetChanged")
        fun setList(list: List<RecordDetail>){
            listPRDetail = list
            notifyDataSetChanged()
        }


        override fun getItemCount(): Int {
            // the data set held by the adapter.
            return listPRDetail.size
        }


        class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

            val groupDate: TextView = itemView.tv_account_pr_item_group_time
            val recordText: TextView = itemView.tv_account_pr_item_text
            val recordTime: TextView = itemView.tv_account_pr_item_time
            val recordMemo: TextView = itemView.tv_account_pr_item_memo
            val recordAmount: TextView = itemView.tv_account_pr_item_amount

            val groupLayout: ConstraintLayout = itemView.layout_account_pr_card_view_group
            val itemLayout: ConstraintLayout = itemView.layout_account_pr_card_view_item
        }


        // this two methods useful for avoiding duplicate item
        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getItemViewType(position: Int): Int {
            return position
        }
    }