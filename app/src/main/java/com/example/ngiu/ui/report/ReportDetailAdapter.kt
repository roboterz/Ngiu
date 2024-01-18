package com.example.ngiu.ui.report


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
import com.example.ngiu.data.entities.returntype.TransactionDetail
import com.example.ngiu.functions.MERCHANT_NO_LOCATION
import com.example.ngiu.functions.TRANSACTION_TYPE_EXPENSE
import com.example.ngiu.functions.TRANSACTION_TYPE_INCOME
import com.example.ngiu.ui.account.detail.AccountGeneralDetailAdapter
//import kotlinx.android.synthetic.main.cardview_account_general_detail_item.view.layout_account_general_card_view_group
//import kotlinx.android.synthetic.main.cardview_account_general_detail_item.view.layout_account_general_card_view_item
//import kotlinx.android.synthetic.main.cardview_account_general_detail_item.view.tv_account_general_item_account
//import kotlinx.android.synthetic.main.cardview_account_general_detail_item.view.tv_account_general_item_amount
//import kotlinx.android.synthetic.main.cardview_account_general_detail_item.view.tv_account_general_item_balance
//import kotlinx.android.synthetic.main.cardview_account_general_detail_item.view.tv_account_general_item_dot_before_memo
//import kotlinx.android.synthetic.main.cardview_account_general_detail_item.view.tv_account_general_item_dot_before_merchant
//import kotlinx.android.synthetic.main.cardview_account_general_detail_item.view.tv_account_general_item_group_date
//import kotlinx.android.synthetic.main.cardview_account_general_detail_item.view.tv_account_general_item_info
//import kotlinx.android.synthetic.main.cardview_account_general_detail_item.view.tv_account_general_item_memo
//import kotlinx.android.synthetic.main.cardview_account_general_detail_item.view.tv_account_general_item_merchant
//import kotlinx.android.synthetic.main.cardview_account_general_detail_item.view.tv_account_general_item_text
//import kotlinx.android.synthetic.main.cardview_account_general_detail_item.view.tv_account_general_item_time
//import kotlinx.android.synthetic.main.cardview_main_category.view.*
//import kotlinx.android.synthetic.main.cardview_mpp_item.view.*
import java.time.format.DateTimeFormatter
import kotlin.collections.ArrayList


class ReportDetailAdapter(
    private val onClickListener: OnClickListener
)
    : RecyclerView.Adapter<ReportDetailAdapter.ViewHolder>() {

    private var listDetail: List<TransactionDetail> = ArrayList()

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
            // category
            holder.recordText.text = Category_Name
            // info
            holder.recordInfo.visibility = View.GONE
            // Account
            holder.recordAccount.visibility = View.VISIBLE
            holder.recordAccount.text = Account_Name
            // Merchant
            if (Merchant_ID > MERCHANT_NO_LOCATION ) {
                holder.recordMerchantDot.visibility = View.VISIBLE
                holder.recordMerchant.visibility = View.VISIBLE
                holder.recordMerchant.text = Merchant_Name
            } else {
                holder.recordMerchantDot.visibility = View.GONE
                holder.recordMerchant.visibility = View.GONE
            }
            // Memo
            if (Transaction_Memo.isNotEmpty()) {
                holder.recordMemoDot.visibility = View.VISIBLE
                holder.recordMemo.visibility = View.VISIBLE
                holder.recordMemo.text = Transaction_Memo
            } else {
                holder.recordMemoDot.visibility = View.GONE
                holder.recordMemo.visibility = View.GONE
            }

            // amount, color
            when (TransactionType_ID){
                TRANSACTION_TYPE_EXPENSE -> {
                    holder.recordBalance.text = "-" + "%.2f".format(Transaction_Amount)
                    holder.recordBalance.setTextColor(holder.expenseColor)
                }
                TRANSACTION_TYPE_INCOME -> {
                    holder.recordBalance.text = "" + "%.2f".format(Transaction_Amount)
                    holder.recordBalance.setTextColor(holder.incomeColor)
                }
            }




            if (position == 0){
                // date
                holder.groupLayout.visibility = View.VISIBLE
                holder.groupDate.text = Transaction_Date.format(groupDateFormatter)
                // balance
                //holder.recordBalance.text = "$" + "%.2f".format(totalAccountBalance)

            }else{
                // date
                holder.groupDate.text = Transaction_Date.format(groupDateFormatter)

                if (holder.groupDate.text == listDetail[position-1].Transaction_Date.format(groupDateFormatter)) {
                    holder.groupLayout.visibility = View.GONE
                }else{
                    holder.groupLayout.visibility = View.VISIBLE
                }
                // balance
                //holder.recordBalance.text = "$" + "%.2f".format(totalAccountBalance)

            }

            holder.itemLayout.setOnClickListener {
                onClickListener.onItemClick(Transaction_ID)
            }
        }
    }


    @SuppressLint("NotifyDataSetChanged")
    fun setList(list: List<TransactionDetail>){
        listDetail = list
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
        val recordMemo: TextView = itemView.findViewById(R.id.tv_account_general_item_memo)
        val recordInfo: TextView = itemView.findViewById(R.id.tv_account_general_item_info)
        val recordAmount: TextView = itemView.findViewById(R.id.tv_account_general_item_amount)
        val recordBalance: TextView = itemView.findViewById(R.id.tv_account_general_item_balance)
        val recordAccount: TextView = itemView.findViewById(R.id.tv_account_general_item_account)
        val recordMerchant: TextView = itemView.findViewById(R.id.tv_account_general_item_merchant)
        val recordMemoDot: TextView = itemView.findViewById(R.id.tv_account_general_item_dot_before_memo)
        val recordMerchantDot: TextView = itemView.findViewById(R.id.tv_account_general_item_dot_before_merchant)

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