package com.example.ngiu.ui.report


import com.example.ngiu.data.entities.Account
import android.annotation.SuppressLint
import android.graphics.Paint
import android.graphics.PorterDuff
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.content.res.TypedArrayUtils.getText
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.ngiu.R
import com.example.ngiu.data.entities.returntype.CalendarDetail
import com.example.ngiu.functions.*
import com.example.ngiu.functions.chart.CategoryAmount
import kotlinx.android.synthetic.main.cardview_calendar.view.*
import kotlinx.android.synthetic.main.cardview_report_item.view.*
import kotlinx.android.synthetic.main.cardview_transaction.view.*
import java.time.DayOfWeek
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList


class ReportAdapter(
    private val onClickListener: OnClickListener
)
    : RecyclerView.Adapter<ReportAdapter.ViewHolder>() {

    //
    var categoryAmountList: MutableList<CategoryAmount> = ArrayList()
    var totalAmount: Double = 100.0

    // interface for passing the onClick event to fragment.
    interface OnClickListener {
        fun onItemClick(ID: Long)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // inflate the custom view from xml layout file
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.cardview_report_item,parent,false)


        // return the view holder
        return ViewHolder(view)

    }


    //@SuppressLint("ResourceAsColor")
    @SuppressLint("SetTextI18n", "RestrictedApi")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // display the custom class

        categoryAmountList[position].apply {

            holder.tvCateName.text = Category_Name
            holder.tvCateAmount.text = "%.2f".format(Amount)
            //holder.pBar.max = 100
            holder.pBar.progress = (Amount / totalAmount * 100).toInt()

                //
                //onClickListener.onItemClick(account_id, type)
            holder.tvCateName.setOnClickListener{
                onClickListener.onItemClick(0L)
            }

        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setList(list: MutableList<CategoryAmount>, Amount:Double){
        categoryAmountList = list
        totalAmount = Amount
        notifyDataSetChanged()
    }


    override fun getItemCount(): Int {
        // the data set held by the adapter.
        return categoryAmountList.size
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val tvCateName: TextView = itemView.cv_report_name
        val tvCateAmount: TextView = itemView.cv_report_amount
        val pBar: ProgressBar = itemView.cv_report_bar


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