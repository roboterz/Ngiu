package com.example.ngiu.ui.report


import com.example.ngiu.data.entities.Account
import android.annotation.SuppressLint
import android.graphics.Paint
import android.graphics.PorterDuff
import android.icu.text.NumberFormat
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
        fun onItemClick(categoryID: Long)
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

            // Progress Bar
            when (TransactionType_ID){
                TRANSACTION_TYPE_EXPENSE -> {
                    holder.pBar.progressDrawable.setTint(holder.expenseColor)
                }
                TRANSACTION_TYPE_INCOME -> {
                    holder.pBar.progressDrawable.setTint(holder.incomeColor)
                }
                else ->{}
            }

            //holder.pBar.max = 100
            holder.pBar.progress = (Amount / totalAmount * 100).toInt()

            // count
            holder.tvCateCount.text = "$Count " + holder.itemView.context.getString(R.string.report_records)

            // percentage
            holder.tvCatePercentage.text = NumberFormat.getPercentInstance().format(Amount / totalAmount)

            holder.lyReport.setOnClickListener{
                onClickListener.onItemClick(Category_ID)
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
        val tvCateCount: TextView = itemView.cv_report_count
        val tvCatePercentage: TextView = itemView.cv_report_percentage
        val lyReport: ConstraintLayout = itemView.card_view_report


        //color
        val expenseColor = ContextCompat.getColor(itemView.context, R.color.app_expense_amount)
        val incomeColor = ContextCompat.getColor(itemView.context, R.color.app_income_amount)

    }


    // this two methods useful for avoiding duplicate item
    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }
}