package com.example.ngiu.ui.account.detail

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.ngiu.R
import com.example.ngiu.data.entities.Account
import com.example.ngiu.data.entities.Trans
import com.example.ngiu.data.entities.returntype.TransactionDetail
import com.example.ngiu.ui.account.model.AccountTransRecordModel
import kotlinx.android.synthetic.main.cardview_account_p_r_detail_item.view.*
import kotlinx.android.synthetic.main.cardview_account_records.view.*
import kotlinx.android.synthetic.main.cardview_transaction.view.*
import org.w3c.dom.Text
import java.time.format.DateTimeFormatter

class AccountPRDetailAdapter(
    private val onClickListener: OnClickListener
    )
    : RecyclerView.Adapter<AccountPRDetailAdapter.ViewHolder>() {

        private var listPRDetail: List<Trans> = ArrayList()

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
                holder.recordText.text = holder.itemView.context.getString(R.string.record_lend_to) + " $Account_ID"
                holder.recordTime.text = Transaction_Date.format(recordTimeFormatter)
                holder.recordAmount.text = "$" + "%.2f".format(Transaction_Amount)
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
        fun setList(list: List<Trans>){
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