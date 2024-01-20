package com.aerolite.ngiu.ui.activity

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.aerolite.ngiu.R
import com.aerolite.ngiu.data.entities.returntype.TransactionDetail
import com.aerolite.ngiu.functions.MERCHANT_NO_LOCATION
import com.aerolite.ngiu.functions.TRANSACTION_TYPE_DEBIT
import com.aerolite.ngiu.functions.TRANSACTION_TYPE_EXPENSE
import com.aerolite.ngiu.functions.TRANSACTION_TYPE_INCOME
import com.aerolite.ngiu.functions.TRANSACTION_TYPE_TRANSFER
//import kotlinx.android.synthetic.main.cardview_transaction.view.*
import java.time.format.DateTimeFormatter
import kotlin.collections.ArrayList


class ActivityListAdapter(
    private val onClick: OnClickListener
    )
    : RecyclerView.Adapter<ActivityListAdapter.ViewHolder>() {

    //
    private var transDetail: List<TransactionDetail> = ArrayList()

    // interface for passing the onClick event to fragment.
    interface OnClickListener {
        fun onItemClick(transID: Long)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // inflate the custom view from xml layout file
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.cardview_transaction,parent,false)


        // return the view holder
        return ViewHolder(view)

    }


    //@SuppressLint("ResourceAsColor")
    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // display the custom class
        transDetail[position].apply {
            // Date
            holder.monthDay.text = Transaction_Date.format(DateTimeFormatter.ofPattern("MM/dd"))

            // year and week
            holder.yearWeek.text = Transaction_Date.format(DateTimeFormatter.ofPattern("yyyy EEEE"))

            // second line
            holder.secondLine.text = Transaction_Date.format(DateTimeFormatter.ofPattern("HH:mm"))
            if (Period_ID > 0L) holder.secondLine.text = holder.secondLine.text.toString() + "    🔁"
            if (Transaction_Memo.isNotEmpty()) holder.secondLine.text = holder.secondLine.text.toString() + "    $Transaction_Memo"


            when (TransactionType_ID){
                TRANSACTION_TYPE_EXPENSE, TRANSACTION_TYPE_INCOME ->{

                    // first line
                    holder.subCategory.text = Category_Name

                    // third line
                    holder.thirdLine.text = Account_Name
                    if (Person_ID > 0L) holder.thirdLine.text = holder.thirdLine.text.toString() + " • $Person_Name"
                    if (Merchant_ID > MERCHANT_NO_LOCATION) holder.thirdLine.text = holder.thirdLine.text.toString() + " • $Merchant_Name"
                    if (Project_ID > 0L) holder.thirdLine.text = holder.thirdLine.text.toString() + " • $Project_Name"
                }
                TRANSACTION_TYPE_TRANSFER, TRANSACTION_TYPE_DEBIT ->{
                    // first line
                    holder.subCategory.text = "$Account_Name ➡️ $AccountRecipient_Name"
                    // third line
                    holder.thirdLine.visibility = View.GONE
                }
            }

            // Amount
            holder.amount.text ="" + "%.2f".format(Transaction_Amount)
            // Amount Color
            holder.amount.setTextColor(
                when (TransactionType_ID.toInt()) {
                    1 -> holder.expenseColor
                    2 -> holder.incomeColor
                    else -> holder.amountColor
                }
            )

            // pass the item click listener to fragment
            holder.aItem.setOnClickListener {
                onClick.onItemClick(Transaction_ID)
            }

        }
    }


    @SuppressLint("NotifyDataSetChanged")
    fun setList(list: List<TransactionDetail>){
        transDetail = list
        notifyDataSetChanged()
    }


    override fun getItemCount(): Int {
        // the data set held by the adapter.
        return transDetail.size
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val monthDay: TextView = itemView.findViewById(R.id.tvTrans_month_day)
        //val dailyInfo: TextView = itemView.tvTrans_daily_info
        val yearWeek: TextView = itemView.findViewById(R.id.tvTrans_year_week)
        val subCategory: TextView = itemView.findViewById(R.id.tvTrans_category)
        val secondLine: TextView = itemView.findViewById(R.id.tvTrans_second_line)
        val thirdLine: TextView = itemView.findViewById(R.id.tvTrans_third_line)
        val amount: TextView = itemView.findViewById(R.id.tvTrans_amount)
        val aItem: ConstraintLayout = itemView.findViewById(R.id.layout_transaction_item)

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