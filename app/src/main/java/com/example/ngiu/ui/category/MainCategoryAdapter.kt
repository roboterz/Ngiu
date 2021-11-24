package com.example.ngiu.ui.category


import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.ngiu.R
import com.example.ngiu.data.entities.MainCategory
import kotlinx.android.synthetic.main.cardview_main_category.view.*
import kotlin.collections.ArrayList


class MainCategoryAdapter(
    private val onClickListener: OnClickListener
)
    : RecyclerView.Adapter<MainCategoryAdapter.ViewHolder>() {

    private var mainCategory: List<MainCategory> = ArrayList()
    private var currentArrow: Int = 0

    // interface for passing the onClick event to fragment.
    interface OnClickListener {
        fun onItemClick(rID: Long)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // inflate the custom view from xml layout file
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.cardview_main_category,parent,false)


        // return the view holder
        return ViewHolder(view)

    }


    //@SuppressLint("ResourceAsColor")
    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // display the custom class
        mainCategory[position].apply {

            holder.mainCategoryName.text = MainCategory_Name

            // selected item status
            if (position == currentArrow){
                holder.arrow.visibility = View.VISIBLE
                holder.itemBackground.setBackgroundColor(holder.activeItem)
            }

            // click event
            holder.mainCategoryName.setOnClickListener {
                currentArrow = position
                onClickListener.onItemClick(MainCategory_ID)
            }


        }
    }


    @SuppressLint("NotifyDataSetChanged")
    fun setList(list: List<MainCategory>){
        mainCategory = list
        notifyDataSetChanged()
    }


    override fun getItemCount(): Int {
        // the data set held by the adapter.
        return mainCategory.size
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val mainCategoryName: TextView = itemView.tv_main_category_name
        val arrow: ImageView = itemView.img_main_category_arrow
        val itemBackground: ConstraintLayout = itemView.layout_main_category_item


        val activeItem = ContextCompat.getColor(itemView.context, R.color.app_select_item)
        //val incomeColor = ContextCompat.getColor(itemView.context, R.color.app_income_amount)
        //val amountColor = ContextCompat.getColor(itemView.context, R.color.app_amount)


    }


    // this two methods useful for avoiding duplicate item
    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }
}