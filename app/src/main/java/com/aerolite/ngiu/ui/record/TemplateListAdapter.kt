package com.aerolite.ngiu.ui.record

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.aerolite.ngiu.R
import com.aerolite.ngiu.data.entities.returntype.TemplateDetail
import com.aerolite.ngiu.functions.TRANSACTION_TYPE_DEBIT
import com.aerolite.ngiu.functions.TRANSACTION_TYPE_EXPENSE
import com.aerolite.ngiu.functions.TRANSACTION_TYPE_INCOME
import com.aerolite.ngiu.functions.TRANSACTION_TYPE_TRANSFER

class TemplateListAdapter(
    private val onClickListener: OnClickListener
) : RecyclerView.Adapter<TemplateListAdapter.ViewHolder>() {

        private var mItem: List<TemplateDetail> = ArrayList()
        //private var editMode: Boolean =false

        // interface for passing the onClick event to fragment.
        interface OnClickListener {
            fun onItemClick(rID: Long)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            // inflate the custom view from xml layout file
            val view: View = LayoutInflater.from(parent.context)
                .inflate(R.layout.cardview_template_item,parent,false)


            // return the view holder
            return ViewHolder(view)

        }


        //@SuppressLint("ResourceAsColor")
        @SuppressLint("SetTextI18n")
        override fun onBindViewHolder(holder: ViewHolder, @SuppressLint("RecyclerView") position: Int) {
            // display the custom class
            mItem[position].apply {

                // Category
                holder.tvCategory.text = Category_Name

                // Account
                when (TransactionType_ID){
                    TRANSACTION_TYPE_EXPENSE, TRANSACTION_TYPE_INCOME -> {
                        holder.tvAccount.text = Account_Name
                    }
                    TRANSACTION_TYPE_TRANSFER, TRANSACTION_TYPE_DEBIT -> {
                        holder.tvAccount.text = "$Account_Name ➡️ $AccountRecipient_Name"
                    }
                }

                // Info
                //holder.tvInfo.text = ""

                // Memo
                holder.tvMemo.text = Transaction_Memo

                // Amount
                holder.tvAmount.text = "%.2f".format(Transaction_Amount)
                when (TransactionType_ID){
                    TRANSACTION_TYPE_EXPENSE -> {
                        holder.tvAmount.setTextColor(holder.itemExpenseColor)
                    }
                    TRANSACTION_TYPE_INCOME -> {
                        holder.tvAmount.setTextColor(holder.itemIncomeColor)
                    }
                    TRANSACTION_TYPE_TRANSFER, TRANSACTION_TYPE_DEBIT -> {
                        holder.tvAmount.setTextColor(holder.itemAmountColor)
                    }
                }

                // Click Event
                holder.lyItem.setOnClickListener {
                    onClickListener.onItemClick(Template_ID)
                }


            }
        }


        @SuppressLint("NotifyDataSetChanged")
        fun setList(list: List<TemplateDetail>){
            mItem = list
            notifyDataSetChanged()
        }


        /*
        fun setEditMode(boolean: Boolean){
            editMode = boolean
        }

         */


        override fun getItemCount(): Int {
            // the data set held by the adapter.
            return mItem.size
        }


        class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

            val tvCategory: TextView = itemView.findViewById(R.id.tv_template_item_category)
            val tvAccount: TextView = itemView.findViewById(R.id.tv_template_item_account)
            //val tvInfo: TextView = itemView.findViewById(R.id.tv_template_item_info)
            val tvMemo: TextView = itemView.findViewById(R.id.tv_template_item_memo)
            val lyItem: ConstraintLayout = itemView.findViewById(R.id.ly_template_card_view_item)
            val tvAmount: TextView = itemView.findViewById(R.id.tv_template_item_amount)


//            val addButtonTextColor = ContextCompat.getColor(itemView.context, R.color.app_sub_line_text)
//            val itemTextColor = ContextCompat.getColor(itemView.context, R.color.app_amount)
            val itemExpenseColor = ContextCompat.getColor(itemView.context, R.color.app_expense_amount)
            val itemIncomeColor = ContextCompat.getColor(itemView.context, R.color.app_income_amount)
            val itemAmountColor = ContextCompat.getColor(itemView.context, R.color.app_amount)
        }


        // this two methods useful for avoiding duplicate item
        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getItemViewType(position: Int): Int {
            return position
        }
}