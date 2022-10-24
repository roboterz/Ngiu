package com.example.ngiu.ui.calendar

import com.example.ngiu.data.entities.Account
import android.annotation.SuppressLint
import android.graphics.Paint
import android.graphics.PorterDuff
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.ngiu.R
import kotlinx.android.synthetic.main.cardview_calendar.view.*
import kotlinx.android.synthetic.main.cardview_transaction.view.*
import java.time.DayOfWeek
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList


class CalendarAdapter(
    private val onClickListener: OnClickListener
    )
    : RecyclerView.Adapter<CalendarAdapter.ViewHolder>() {

    var accountList: List<Account> = ArrayList()

    private val formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy hh:mm")

    // interface for passing the onClick event to fragment.
    interface OnClickListener {
        fun onItemClick(accountID: Long, blnFixed: Boolean)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // inflate the custom view from xml layout file
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.cardview_calendar,parent,false)


        // return the view holder
        return ViewHolder(view)

    }


    //@SuppressLint("ResourceAsColor")
    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // display the custom class
        accountList[position].apply {
            //holder.monthDay.text = Account_PaymentDay?.format(DateTimeFormatter.ofPattern("MM/dd"))
            //if (Account_PaymentDay >= Calendar.DAY_OF_MONTH){
            if (Account_PaymentDay < 10) {
                holder.monthDay.text =
                    LocalDateTime.now().month.value.toString() + "/0$Account_PaymentDay"
            }else{
                holder.monthDay.text =
                    LocalDateTime.now().month.value.toString() + "/$Account_PaymentDay"
            }
            //}
            //holder.monthDay.text = "$Account_PaymentDay"

            //name
            holder.name.text = Account_Name
            //amount
            holder.amount.text ="$" + "%.2f".format(Account_Balance)
            //checkbox
            holder.cbox.isChecked = !Account_FixedPaymentDay
            if (Account_FixedPaymentDay) {
                holder.name.paintFlags = 0
            }else{
                holder.name.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
            }

            if (Account_PaymentDay < LocalDateTime.now().dayOfMonth){
                holder.name.setTextColor(holder.textColorDone)
                holder.amount.setTextColor(holder.textColorDone)
                holder.monthDay.setTextColor(holder.textColorDone)

            }else if ((Account_PaymentDay - LocalDateTime.now().dayOfMonth) >=0 && (Account_PaymentDay - LocalDateTime.now().dayOfMonth) <=5){
                //holder.name.setTextColor(holder.textColorDue)
                //holder.amount.setTextColor(holder.textColorDue)
                holder.monthDay.setTextColor(holder.textColorDue)
                holder.dot.setColorFilter(holder.textColorDue)

            }else{
                holder.name.setTextColor(holder.textColorFuture)
                holder.amount.setTextColor(holder.textColorFuture)
                holder.monthDay.setTextColor(holder.textColorFuture)
            }

            // pass the item click listener to fragment
            holder.cbox.setOnClickListener {
                Account_FixedPaymentDay = !Account_FixedPaymentDay
                //holder.cbox.isChecked = Account_FixedPaymentDay

                onClickListener.onItemClick(Account_ID, Account_FixedPaymentDay)
                //in CalendarFragment, to save the value into database

            }
        }
    }


    @SuppressLint("NotifyDataSetChanged")
    fun setList(list: List<Account>){
        accountList = list
        notifyDataSetChanged()
    }


    override fun getItemCount(): Int {
        // the data set held by the adapter.
        return accountList.size
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val monthDay: TextView = itemView.cv_calendar_tv_month_day
        val name: TextView = itemView.cv_calendar_tv_account_name
        val amount: TextView = itemView.cv_calendar_tv_amount
        val aItem: ConstraintLayout = itemView.layout_calendar_item
        val cbox: CheckBox = itemView.cv_calendar_checkBox
        val dot: ImageView = itemView.cv_calendar_img_circle

        //val expenseColor = ContextCompat.getColor(itemView.context, R.color.app_expense_amount)
        //val incomeColor = ContextCompat.getColor(itemView.context, R.color.app_income_amount)
        //val amountColor = ContextCompat.getColor(itemView.context, R.color.app_amount)
        val drawableDone = ContextCompat.getDrawable(itemView.context, R.drawable.ic_baseline_check_box_24)
        val drawableBlank = ContextCompat.getDrawable(itemView.context, R.drawable.ic_baseline_check_box_outline_blank_24)

        val textColorDone = ContextCompat.getColor(itemView.context, R.color.app_sub_line_text)
        val textColorDue = ContextCompat.getColor(itemView.context, R.color.app_expense_amount)
        val textColorFuture = ContextCompat.getColor(itemView.context, R.color.app_amount)

    }


    // this two methods useful for avoiding duplicate item
    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }
}