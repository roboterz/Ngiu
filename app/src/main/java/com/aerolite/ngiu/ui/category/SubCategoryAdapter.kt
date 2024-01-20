package com.aerolite.ngiu.ui.category


import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.aerolite.ngiu.R
import com.aerolite.ngiu.data.entities.Category
//import kotlinx.android.synthetic.main.cardview_sub_category.view.*
import kotlin.collections.ArrayList


class SubCategoryAdapter(
    private val onClickListener: OnClickListener
)
    : RecyclerView.Adapter<SubCategoryAdapter.ViewHolder>() {

    private var subCategory: List<Category> = ArrayList()
    private var currentArrow: Int = 0

    // interface for passing the onClick event to fragment.
    interface OnClickListener {
        fun onItemClick(rID: Long, subCategoryName: String, addNew: Boolean = false)
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
            holder.subCategoryName.text = Category_Name

            if (position == subCategory.size - 1 && Category_ID == 0L){
                // add
                holder.subCategoryName.setTextColor(holder.addButtonTextColor)
                holder.star.visibility = View.GONE
                holder.subCategoryName.setOnClickListener {
                    onClickListener.onItemClick(Category_ID, Category_Name, true)
                }

            }else{
                // set color
                holder.subCategoryName.setTextColor(holder.itemTextColor)
                holder.star.visibility = View.VISIBLE

                // set star icon
                if (Category_Common) holder.star.setImageDrawable(holder.drawableStar)
                else holder.star.setImageDrawable(holder.drawableStarBorder)

                // click item
                holder.subCategoryName.setOnClickListener {
                    onClickListener.onItemClick(Category_ID, Category_Name)
                }
                // long click item
                holder.subCategoryName.setOnLongClickListener {
                    onClickListener.onItemLongClick(Category_ID)
                    true
                }

                // click star
                holder.star.setOnClickListener {
                    if (Category_Common) {
                        Category_Common = false
                        holder.star.setImageDrawable(holder.drawableStarBorder)
                    }
                    else{
                        Category_Common = true
                        holder.star.setImageDrawable(holder.drawableStar)
                    }
                    onClickListener.onStarClick(Category_ID, Category_Common)
                }
            }

        }
    }


    @SuppressLint("NotifyDataSetChanged")
    fun setList(list: List<Category>){
        subCategory = list
        notifyDataSetChanged()
    }


    override fun getItemCount(): Int {
        // the data set held by the adapter.
        return subCategory.size
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val subCategoryName: TextView = itemView.findViewById(R.id.tv_sub_category_name)
        val star: ImageView = itemView.findViewById(R.id.img_sub_category_star)

        val drawableStar = ContextCompat.getDrawable(itemView.context, R.drawable.ic_baseline_star_24)
        val drawableStarBorder = ContextCompat.getDrawable(itemView.context, R.drawable.ic_baseline_star_border_24)

        val addButtonTextColor = ContextCompat.getColor(itemView.context, R.color.app_sub_line_text)
        val itemTextColor = ContextCompat.getColor(itemView.context, R.color.app_amount)


    }


    // this two methods useful for avoiding duplicate item
    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }
}