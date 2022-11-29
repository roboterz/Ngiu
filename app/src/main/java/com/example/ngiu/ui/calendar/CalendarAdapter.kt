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
        fun onItemClick(ID: Long, type: Int)
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
                1 -> {
                    holder.amount.visibility = View.VISIBLE
                    //account name
                    holder.content.text = "$account_out_name $account_last_four_number"

                    // todo dot color
                    holder.dot.setColorFilter(holder.expenseColor)
                    holder.amount.setTextColor(holder.expenseColor)
                }
                //周期帐
                2 -> {
                    holder.amount.visibility = View.VISIBLE
                }
                //Event
                3 -> {
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
                    1 -> {
                        val bundle = Bundle().apply {
                            putLong("accountId", id)
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

                    // Event
                    3 -> onClickListener.onItemClick(id, type)
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