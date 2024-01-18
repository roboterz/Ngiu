package com.example.ngiu.ui.rewards


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
import com.example.ngiu.data.entities.returntype.RewardsDetail
import com.example.ngiu.functions.*
import com.example.ngiu.functions.chart.CategoryAmount
//import kotlinx.android.synthetic.main.cardview_calendar.view.*
//import kotlinx.android.synthetic.main.cardview_report_item.view.*
//import kotlinx.android.synthetic.main.cardview_rewards_item.view.iv_rewards_item_icon
//import kotlinx.android.synthetic.main.cardview_rewards_item.view.tv_rewards_cate
//import kotlinx.android.synthetic.main.cardview_rewards_item.view.tv_rewards_number
//import kotlinx.android.synthetic.main.cardview_transaction.view.*
import java.time.DayOfWeek
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList


class RewardsAdapter(
    private val onClickListener: OnClickListener
)
    : RecyclerView.Adapter<RewardsAdapter.ViewHolder>() {

    // todo
    private var rewardsList: List<RewardsDetail> = ArrayList()


    // interface for passing the onClick event to fragment.
    interface OnClickListener {
        fun onItemClick(rewardsID: Long)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // inflate the custom view from xml layout file
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.cardview_rewards_item,parent,false)


        // return the view holder
        return ViewHolder(view)

    }


    //@SuppressLint("ResourceAsColor")
    @SuppressLint("SetTextI18n", "RestrictedApi")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // display the custom class

        rewardsList[position].apply {

            if (Category_ID >0L ) {
                holder.tvCateName.text = Category_Name
            }else{
                holder.tvCateName.text = Merchant_Name
            }

            holder.tvCateAmount.text = "%.1f".format(Reward_Percentage)

            if (Icon_ID > 1L){
                holder.ivIcon.setImageBitmap(Icon_Image)
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setList(list: List<RewardsDetail>){
        rewardsList = list
        notifyDataSetChanged()
    }


    override fun getItemCount(): Int {
        // the data set held by the adapter.
        return rewardsList.size
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val ivIcon: ImageView = itemView.findViewById(R.id.iv_rewards_item_icon)
        val tvCateName: TextView = itemView.findViewById(R.id.tv_rewards_cate)
        val tvCateAmount: TextView = itemView.findViewById(R.id.tv_rewards_number)

    }


    // this two methods useful for avoiding duplicate item
    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }
}