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
import androidx.core.content.res.TypedArrayUtils.getText
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.ngiu.R
import com.example.ngiu.data.entities.returntype.CalendarDetail
import com.example.ngiu.functions.*
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
        fun onItemClick(cDetail: CalendarDetail)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // inflate the custom view from xml layout file
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.cardview_calendar,parent,false)


        // return the view holder
        return ViewHolder(view)

    }


    //@SuppressLint("ResourceAsColor")
    @SuppressLint("SetTextI18n", "RestrictedApi")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // display the custom class

        calendarDetail[position].apply {

            holder.monthDay.text = date.format(DateTimeFormatter.ofPattern("MM/dd"))

            //hide the date if same day as above
            if (position>0) {
                if (date == calendarDetail[position - 1].date){
                    holder.monthDay.text = ""
                }
            }

            //title
            holder.eventTitle.text = title
            //amount
            holder.amount.text ="$" + "%.2f".format(amount)

            // todo Event
            when (type){
                //Credit Card
                EVENT_CREDIT_PAYMENT -> {
                    holder.amount.visibility = View.VISIBLE
                    //account name
                    holder.content.text = "$account_out_name $account_last_four_number"

                    // dot color
                    holder.dot.setColorFilter(holder.expenseColor)
                    holder.amount.setTextColor(holder.expenseColor)
                }
                //Periodic Bill
                EVENT_PERIODIC_BILL -> {
                    holder.amount.visibility = View.VISIBLE
                }
                //Note, Periodic Note
                EVENT_NOTE, EVENT_PERIODIC_NOTE -> {
                    //color
                    holder.dot.setColorFilter(holder.eventColor)

                    holder.content.text = memo
                }
                //else -> holder.eventType.text = ""
            }




            // pass the item click listener to fragment
            holder.aItem.setOnClickListener {
                // todo 点击进入相应的账号查看
                when (type) {
                    // credit card
                    EVENT_CREDIT_PAYMENT -> {
                        val bundle = Bundle().apply {
                            putLong(KEY_ACCOUNT_ID, id)
                            putString(KEY_ACCOUNT_NAME, account_out_name)
                            putDouble(KEY_ACCOUNT_BALANCE, amount)
                            putDouble(KEY_ACCOUNT_LIMIT, 5000.00)
                            putInt(KEY_ACCOUNT_PAYMENT_DAY, 23)
                            putInt(KEY_ACCOUNT_STATEMENT_DATE, 23)
                            putLong(KEY_ACCOUNT_TYPE, ACCOUNT_TYPE_CREDIT)
                        }

                        holder.itemView.findNavController()
                            .navigate(R.id.accountCreditRecords, bundle)
                    }

                    // Note
                    EVENT_NOTE, EVENT_PERIODIC_NOTE -> onClickListener.onItemClick(this)
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
        val content: TextView = itemView.cv_calendar_tv_content
        val amount: TextView = itemView.cv_calendar_tv_amount
        val aItem: ConstraintLayout = itemView.layout_calendar_item
        //val cbox: CheckBox = itemView.cv_calendar_checkBox
        val dot: ImageView = itemView.cv_calendar_img_circle
        val eventTitle: TextView = itemView.cv_calendar_tv_event


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