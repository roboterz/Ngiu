package com.example.ngiu.ui.account.detail

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.ngiu.R
import com.example.ngiu.data.entities.returntype.RecordDetail
import com.example.ngiu.functions.*
//import kotlinx.android.synthetic.main.cardview_account_general_detail_item.view.*
import java.time.format.DateTimeFormatter

class AccountGeneralDetailAdapter(
    private val onClickListener: OnClickListener
    )
    : RecyclerView.Adapter<AccountGeneralDetailAdapter.ViewHolder>() {

    private var listDetail: List<RecordDetail> = ArrayList()
    private var balanceList: List<Double> = ArrayList()
    private var currentAccountID: Long = 0L
    private val recordTimeFormatter = DateTimeFormatter.ofPattern("hh:mm")
    private val groupDateFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy EEEE")

    // interface for passing the onClick event to fragment.
    interface OnClickListener {
        fun onItemClick(transID: Long)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // inflate the custom view from xml layout file
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.cardview_account_general_detail_item,parent,false)


        // return the view holder
        return ViewHolder(view)

    }


    //@SuppressLint("ResourceAsColor")
    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // display the custom class
        listDetail[position].apply {
            holder.recordTime.text = Transaction_Date.format(recordTimeFormatter)

            // category, amount , info
            when (TransactionType_ID){
                TRANSACTION_TYPE_EXPENSE -> {
                    // category
                    holder.recordText.text = Category_Name
                    // amount
                    holder.recordAmount.text = "-" + "%.2f".format(Transaction_Amount)
                    holder.recordAmount.setTextColor(holder.expenseColor)
                    // info
                    holder.recordInfo.visibility = View.GONE
                }
                TRANSACTION_TYPE_INCOME -> {
                    // category
                    holder.recordText.text = Category_Name
                    // amount
                    holder.recordAmount.text = "" + "%.2f".format(Transaction_Amount)
                    holder.recordAmount.setTextColor(holder.incomeColor)
                    // info
                    holder.recordInfo.visibility = View.GONE
                }
                TRANSACTION_TYPE_TRANSFER,TRANSACTION_TYPE_DEBIT ->{
                    // info
                    holder.recordInfo.visibility = View.VISIBLE

                    // category
                    if (TransactionType_ID == TRANSACTION_TYPE_TRANSFER){
                        if (Account_ID == currentAccountID){
                            holder.recordText.text = holder.itemView.context.getString(R.string.record_transfer_out)
                            holder.recordAmount.text = "-" + "%.2f".format(Transaction_Amount)
                        }else{
                            holder.recordText.text = holder.itemView.context.getString(R.string.record_transfer_in)
                            holder.recordAmount.text = "+" + "%.2f".format(Transaction_Amount)
                        }
                        // info
                        holder.recordInfo.text = "$Account_Name " + holder.itemView.context.getString(R.string.record_to) + " $AccountRecipient_Name"
                    }
                    if (TransactionType_ID == TRANSACTION_TYPE_DEBIT){
                        when (Category_ID){
                            CATEGORY_SUB_BORROW -> {
                                holder.recordText.text = holder.itemView.context.getString(R.string.record_borrow_in)
                                holder.recordInfo.text = holder.itemView.context.getString(R.string.record_borrow_from) + " $Account_Name"
                                holder.recordAmount.text = "+" + "%.2f".format(Transaction_Amount)
                            }
                            CATEGORY_SUB_LEND -> {
                                holder.recordText.text = holder.itemView.context.getString(R.string.record_lend_out)
                                holder.recordInfo.text = holder.itemView.context.getString(R.string.record_lend_to) + " $AccountRecipient_Name"
                                holder.recordAmount.text = "-" + "%.2f".format(Transaction_Amount)
                            }
                            CATEGORY_SUB_PAYMENT -> {
                                holder.recordText.text = holder.itemView.context.getString(R.string.record_repay)
                                holder.recordInfo.text = holder.itemView.context.getString(R.string.record_paid_to) + " $AccountRecipient_Name"
                                holder.recordAmount.text = "-" + "%.2f".format(Transaction_Amount)
                            }
                            CATEGORY_SUB_RECEIVE_PAYMENT -> {
                                holder.recordText.text = holder.itemView.context.getString(R.string.record_receive)
                                holder.recordInfo.text = holder.itemView.context.getString(R.string.record_received_from) + " $Account_Name"
                                holder.recordAmount.text = "+" + "%.2f".format(Transaction_Amount)
                            }
                        }
                    }

                    // amount color
                    holder.recordAmount.setTextColor(holder.amountColor)

                }
            }

            // memo
            if (Transaction_Memo.isNotEmpty()) {
                if (holder.recordInfo.text.isNotEmpty()){
                    holder.dotBeforeMemo.visibility = View.VISIBLE
                }
            }
            holder.recordMemo.text = Transaction_Memo


            // date
            holder.groupDate.text = Transaction_Date.format(groupDateFormatter)


            // group subject
            if (position == 0) {
                holder.groupLayout.visibility = View.VISIBLE

            } else {
                if (holder.groupDate.text == listDetail[position-1].Transaction_Date.format(groupDateFormatter)) {
                    holder.groupLayout.visibility = View.GONE
                }else{
                    holder.groupLayout.visibility = View.VISIBLE
                }
            }


            // balance
            holder.recordBalance.text = get2DigitFormat(balanceList[position])


            holder.itemLayout.setOnClickListener {
                onClickListener.onItemClick(Transaction_ID)
            }
        }
    }


    @SuppressLint("NotifyDataSetChanged")
    fun setList(list: List<RecordDetail>, bList: List<Double>, acctID: Long){
        listDetail = list
        balanceList = bList
        currentAccountID = acctID
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        // the data set held by the adapter.
        return listDetail.size
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        val groupDate: TextView = itemView.findViewById(R.id.tv_account_general_item_group_date)
        val recordText: TextView = itemView.findViewById(R.id.tv_account_general_item_text)
        val recordTime: TextView = itemView.findViewById(R.id.tv_account_general_item_time)
        val dotBeforeMemo: TextView = itemView.findViewById(R.id.tv_account_general_item_dot_before_memo)
        val recordMemo: TextView = itemView.findViewById(R.id.tv_account_general_item_memo)
        val recordInfo: TextView = itemView.findViewById(R.id.tv_account_general_item_info)
        val recordAmount: TextView = itemView.findViewById(R.id.tv_account_general_item_amount)
        val recordBalance: TextView = itemView.findViewById(R.id.tv_account_general_item_balance)

        val groupLayout: ConstraintLayout = itemView.findViewById(R.id.layout_account_general_card_view_group)
        val itemLayout: ConstraintLayout = itemView.findViewById(R.id.layout_account_general_card_view_item)

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

    override fun onViewAttachedToWindow(holder: ViewHolder) {
        super.onViewAttachedToWindow(holder)

        holder.itemView.clearAnimation()
        holder.itemView.startAnimation(AnimationUtils.loadAnimation(holder.itemView.context, R.anim.scale_in_scroll))
    }
}