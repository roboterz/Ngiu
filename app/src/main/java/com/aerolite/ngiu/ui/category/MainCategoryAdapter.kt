package com.aerolite.ngiu.ui.category


import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.aerolite.ngiu.R
import com.aerolite.ngiu.data.entities.Category
//import kotlinx.android.synthetic.main.cardview_main_category.view.*
import kotlin.collections.ArrayList


class MainCategoryAdapter(
    private val onClick: OnClickListener
)
    : RecyclerView.Adapter<MainCategoryAdapter.ViewHolder>() {

    private var mainCategory: List<Category> = ArrayList()
    private var currentArrow: Int = 0
    //private var editMode: Boolean =false

    // interface for passing the onClick event to fragment.
    interface OnClickListener {
        fun onItemClick(rID: Long, addNew: Boolean = false)
        fun onItemLongClick(rID: Long, mainCategoryName: String, nextRowID: Long)
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
    override fun onBindViewHolder(holder: ViewHolder, @SuppressLint("RecyclerView") position: Int) {
        // display the custom class
        mainCategory[position].apply {

            holder.mainCategoryName.text = Category_Name

            if (position == mainCategory.size - 1 && TransactionType_ID == 1L){
                // add New
                holder.mainCategoryName.setTextColor(holder.addButtonTextColor)
                holder.mainCategoryName.setOnClickListener {
                    onClick.onItemClick(Category_ID,true)
                }

            }else{

                /*
                if (editMode){
                    holder.mainCategoryName.setCompoundDrawables(null,null, holder.iconDelete,null)
                }

                 */
                holder.mainCategoryName.setTextColor(holder.itemTextColor)

                // selected item status
                if (position == currentArrow){
                    holder.arrow.visibility = View.VISIBLE
                    //holder.itemBackground.setBackgroundColor(holder.activeItem)
                    holder.mainCategoryName.setTextColor(holder.activeItem)
                }
                // click event
                holder.mainCategoryName.setOnClickListener {
                    currentArrow = position
                    onClick.onItemClick(Category_ID)
                }
                // long click event
                holder.mainCategoryName.setOnLongClickListener {
                    if (position>0){
                        onClick.onItemLongClick(Category_ID, Category_Name, mainCategory[position-1].Category_ID)
                    }
                    true
                }
            }


        }
    }


    @SuppressLint("NotifyDataSetChanged")
    fun setList(list: List<Category>){
        mainCategory = list
        notifyDataSetChanged()
    }

    fun setArrowAfterDelete(){
        if (currentArrow > 0) currentArrow--
    }

    /*
    fun setEditMode(boolean: Boolean){
        editMode = boolean
    }

     */


    override fun getItemCount(): Int {
        // the data set held by the adapter.
        return mainCategory.size
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val mainCategoryName: TextView = itemView.findViewById(R.id.tv_main_category_name)
        val arrow: ImageView = itemView.findViewById(R.id.img_main_category_arrow)
        val itemBackground: ConstraintLayout = itemView.findViewById(R.id.layout_main_category_item)


        val activeItem = ContextCompat.getColor(itemView.context, R.color.app_expense_amount)
        val addButtonTextColor = ContextCompat.getColor(itemView.context, R.color.app_sub_line_text2)
        val itemTextColor = ContextCompat.getColor(itemView.context, R.color.app_text)
        val iconDelete = ContextCompat.getDrawable(itemView.context, R.drawable.ic_baseline_delete_forever_24)

    }


    // this two methods useful for avoiding duplicate item
    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }
}