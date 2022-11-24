package com.example.ngiu.ui.calendar

import com.example.ngiu.data.entities.Account
import android.annotation.SuppressLint
import android.graphics.Paint
import android.graphics.PorterDuff
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.Switch
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.ngiu.R
import com.example.ngiu.data.entities.returntype.CalendarDetail
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

    var calendarDetail: MutableList<CalendarDetail> = ArrayList()

    //private val formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy hh:mm")

    // interface for passing the onClick event to fragment.
    interface OnClickListener {
        fun onItemClick(accountID: Long, type: Int)
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

        calendarDetail[position].apply {
            //holder.monthDay.text = Account_PaymentDay?.format(DateTimeFormatter.ofPattern("MM/dd"))
            //if (Account_PaymentDay >= Calendar.DAY_OF_MONTH){


            holder.monthDay.text = date?.format(DateTimeFormatter.ofPattern("MM/dd"))


            //hide the date if same day as above
            if (position>0) {
                if (date == calendarDetail[position - 1].date){
                    holder.monthDay.text = ""
                }
            }
            //}
            //holder.monthDay.text = "$Account_PaymentDay"

            // todo Event
            when (type){
                //Credit Card
                1 -> {
                    holder.eventType.visibility = View.VISIBLE
                    holder.amount.visibility = View.VISIBLE
                    holder.acctName.visibility = View.VISIBLE
                    holder.eventType.text = "Credit Card Payment"

                    // todo dot color
                    holder.dot.setColorFilter(holder.expenseColor)
                    holder.amount.setTextColor(holder.expenseColor)
                }
                //周期帐
                2 -> {
                    holder.eventType.visibility = View.VISIBLE
                    holder.eventType.text = "Payment"
                }
                //Event
                3 -> {
                    //color
                    holder.dot.setColorFilter(holder.eventColor)

                    holder.memo.visibility = View.VISIBLE
                    holder.memo.text = memo
                }
                else -> holder.eventType.text = ""
            }


            //name
            holder.acctName.text = "$title $account_last_four_number"
            //amount
            holder.amount.text ="$" + "%.2f".format(amount)
            //checkbox
            //holder.cbox.isChecked = !Account_FixedPaymentDay

            //添加删除线
            /*if (Account_FixedPaymentDay) {
                holder.name.paintFlags = 0
            }else{
                holder.name.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
            }*/

/*            if (Account_PaymentDay < today){
                holder.name.setTextColor(holder.textColorDone)
                holder.amount.setTextColor(holder.textColorDone)
                //holder.monthDay.setTextColor(holder.textColorDone)

            }else if ((Account_PaymentDay - today) >=0 && (Account_PaymentDay - today) <=5){
                //holder.name.setTextColor(holder.textColorDue)
                //holder.amount.setTextColor(holder.textColorDue)
                //holder.monthDay.setTextColor(holder.textColorDue)
                holder.dot.setColorFilter(holder.textColorDue)

            }else{
                holder.name.setTextColor(holder.textColorFuture)
                holder.amount.setTextColor(holder.textColorFuture)
                //holder.monthDay.setTextColor(holder.textColorFuture)
            }*/



            // pass the item click listener to fragment
            holder.aItem.setOnClickListener {

                // todo 点击进入相应的账号查看

                when (type) {
                    // credit card
                    1 -> {
                        val bundle = Bundle().apply {
                            putLong("accountId", account_id)
                            putString("accountName", account_out_name)
                            putDouble("balance", amount)
                            putDouble("creditLimit", 5000.00)
                            putInt("paymentDate", 23)
                            putInt("statementDate", 23)
                            putLong("accountType", 2L)
                        }

                        holder.itemView.findNavController()
                            .navigate(R.id.accountCreditRecords, bundle)
                    }
                }

                //
                //onClickListener.onItemClick(account_id, type)
            }

        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setList(list: MutableList<CalendarDetail>){
        calendarDetail = list
        notifyDataSetChanged()
    }


    override fun getItemCount(): Int {
        // the data set held by the adapter.
        return calendarDetail.size
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val monthDay: TextView = itemView.cv_calendar_tv_month_day
        val acctName: TextView = itemView.cv_calendar_tv_account_name
        val amount: TextView = itemView.cv_calendar_tv_amount
        val aItem: ConstraintLayout = itemView.layout_calendar_item
        //val cbox: CheckBox = itemView.cv_calendar_checkBox
        val dot: ImageView = itemView.cv_calendar_img_circle
        val eventType: TextView = itemView.cv_calendar_tv_event
        val memo: TextView = itemView.cv_calendar_tv_memo


        //icon
        val drawableDone = ContextCompat.getDrawable(itemView.context, R.drawable.ic_baseline_check_box_24)
        val drawableBlank = ContextCompat.getDrawable(itemView.context, R.drawable.ic_baseline_check_box_outline_blank_24)

        //color
        val circleColor = ContextCompat.getColor(itemView.context, R.color.app_sub_line_text)
        val expenseColor = ContextCompat.getColor(itemView.context, R.color.app_expense_amount)
        val incomeColor = ContextCompat.getColor(itemView.context, R.color.app_income_amount)
        val amountColor = ContextCompat.getColor(itemView.context, R.color.app_amount)
        val eventColor = ContextCompat.getColor(itemView.context, R.color.app_title_background)

    }


    // this two methods useful for avoiding duplicate item
    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }
}