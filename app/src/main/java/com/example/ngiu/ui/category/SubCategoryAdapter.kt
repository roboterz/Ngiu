package com.example.ngiu.ui.category


import android.annotation.SuppressLint
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.ngiu.R
import com.example.ngiu.data.entities.MainCategory
import com.example.ngiu.data.entities.SubCategory
import com.example.ngiu.data.entities.returntype.TransactionDetail
import kotlinx.android.synthetic.main.cardview_sub_category.view.*
import kotlin.collections.ArrayList


class SubCategoryAdapter(
    private val onClickListener: OnClickListener
)
    : RecyclerView.Adapter<SubCategoryAdapter.ViewHolder>() {

    private var subCategory: List<SubCategory> = ArrayList()
    private var currentArrow: Int = 0

    // interface for passing the onClick event to fragment.
    interface OnClickListener {
        fun onItemClick(rID: Long, subCategoryName: String)
        fun onItemLongClick(rID: Long)
        fun onStarClick(rID: Long, commonValue: Boolean)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // inflate the custom view from xml layout file
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.cardview_sub_category,parent,false)


        // return the view holder
        return ViewHolder(view)

    }


    //@SuppressLint("ResourceAsColor")
    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // display the custom class
        subCategory[position].apply {
            // load data
            holder.subCategoryName.text = SubCategory_Name

            if (SubCategory_Common) holder.star.setImageDrawable(holder.drawableStar)
            else holder.star.setImageDrawable(holder.drawableStarBorder)



            holder.subCategoryName.setOnClickListener {
                onClickListener.onItemClick(SubCategory_ID, SubCategory_Name)
            }
            holder.subCategoryName.setOnLongClickListener {
                onClickListener.onItemLongClick(SubCategory_ID)
                true
            }

            holder.star.setOnClickListener {
                if (SubCategory_Common) {
                    SubCategory_Common = false
                    holder.star.setImageDrawable(holder.drawableStarBorder)
                }
                else{
                    SubCategory_Common = true
                    holder.star.setImageDrawable(holder.drawableStar)
                }
                onClickListener.onStarClick(SubCategory_ID, SubCategory_Common)
            }

        }
    }


    @SuppressLint("NotifyDataSetChanged")
    fun setList(list: List<SubCategory>){
        subCategory = list
        notifyDataSetChanged()
    }


    override fun getItemCount(): Int {
        // the data set held by the adapter.
        return subCategory.size
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val subCategoryName: TextView = itemView.tv_sub_category_name
        val star: ImageView = itemView.img_sub_category_star

        val drawableStar = ContextCompat.getDrawable(itemView.context, R.drawable.ic_baseline_star_24)
        val drawableStarBorder = ContextCompat.getDrawable(itemView.context, R.drawable.ic_baseline_star_border_24)


    }


    // this two methods useful for avoiding duplicate item
    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }
}