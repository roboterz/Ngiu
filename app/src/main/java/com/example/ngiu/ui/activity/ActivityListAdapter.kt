package com.example.ngiu.ui.activity

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.ngiu.R
import com.example.ngiu.data.entities.returntype.TransactionDetail
import kotlinx.android.synthetic.main.cardview_transaction.view.*
import java.time.format.DateTimeFormatter
import kotlin.collections.ArrayList


class ActivityListAdapter(
    private val onClickListener: OnClickListener
    )
    : RecyclerView.Adapter<ActivityListAdapter.ViewHolder>() {

    private var transDetail: List<TransactionDetail> = ArrayList()

    private val formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy hh:mm")

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
            holder.monthDay.text = Transaction_Date?.format(DateTimeFormatter.ofPattern("MM/dd"))
            holder.week.text =Transaction_Date?.format(DateTimeFormatter.ofPattern("EEEE"))
            //holder.income.text = "Income"
            //holder.dailyIncome.text ="$0.00"
            //holder.expense.text = "Expense"
            //holder.dailyExpense.text ="$0.00"
            holder.time.text = Transaction_Date?.format(DateTimeFormatter.ofPattern("HH:mm"))
            holder.year.text = Transaction_Date?.format(DateTimeFormatter.ofPattern("yyyy"))
            //holder.period.text = ""
            holder.memo.text = Transaction_Memo


            holder.amount.text ="$" + "%.2f".format(Transaction_Amount)

            // TransactionType 1, 2
            if (TransactionType_ID in 1L..2L) {
                holder.subCategory.text = SubCategory_Name
                holder.account.text = Account_Name
                holder.person.text = Person_Name
                holder.merchant.text = Merchant_Name
                holder.project.text = Project_Name
                if (holder.person.text !=""){
                    holder.beforePerson.text =" • "
                }
                if (holder.merchant.text !=""){
                    holder.beforeMerchant.text =" • "
                }
                if (holder.project.text !=""){
                    holder.beforeProject.text =" • "
                }
            }else{
                holder.account.visibility = View.GONE
                holder.person.visibility = View.GONE
                holder.merchant.visibility = View.GONE
                holder.project.visibility = View.GONE
                holder.beforeMerchant.visibility = View.GONE
                holder.beforePerson.visibility = View.GONE
                holder.beforeProject.visibility = View.GONE
            }



            // Transaction Type 3, 4
            if (TransactionType_ID in 3L..4L ){
                holder.subCategory.text = Account_Name
                holder.accountReceiver.text =" ➡️ $AccountRecipient_Name"
                holder.accountReceiver.visibility = View.VISIBLE
            } else {
                holder.accountReceiver.text = ""
            }

            if (Period_ID != 0L) {
                //holder.period.text="Period"
                holder.period.visibility = View.VISIBLE
            }

            holder.amount.setTextColor(
                when (TransactionType_ID.toInt()) {
                    1 -> holder.expenseColor
                    2 -> holder.incomeColor
                    else -> holder.amountColor
                }
            )

            // pass the item click listener to fragment
            holder.aItem.setOnClickListener {
                onClickListener.onItemClick(Transaction_ID)
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
        val monthDay: TextView = itemView.tvTrans_month_day
        val week: TextView = itemView.tvTrans_week
        //val expense: TextView = itemView.tvTrans_daily_expense
        //val income: TextView = itemView.tvTrans_daily_income
        //val dailyExpense: TextView = itemView.tvTrans_daily_expense_amount
        //val dailyIncome: TextView = itemView.tvTrans_daily_income_amount
        val year: TextView = itemView.tvTrans_year
        val subCategory: TextView = itemView.tvTrans_category
        val time: TextView = itemView.tvTrans_time
        val account: TextView = itemView.tvTrans_account_pay
        val accountReceiver: TextView = itemView.tvTrans_account_receive
        val merchant: TextView = itemView.tvTrans_merchant
        val period: TextView = itemView.tvTrans_period
        val memo: TextView = itemView.tvTrans_memo
        val person: TextView = itemView.tvTrans_person
        val project: TextView = itemView.tvTrans_project
        val amount: TextView = itemView.tvTrans_amount
        val beforePerson: TextView = itemView.tvTrans_dot_before_person
        val beforeMerchant: TextView = itemView.tvTrans_dot_before_merchant
        val beforeProject: TextView = itemView.tvTrans_dot_before_project
        val aItem: ConstraintLayout = itemView.layout_transaction_item

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