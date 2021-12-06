package com.example.ngiu.ui.setting.mppmanage


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
import com.example.ngiu.data.entities.returntype.MPPItem
import kotlinx.android.synthetic.main.cardview_main_category.view.*
import kotlinx.android.synthetic.main.cardview_mpp_item.view.*
import kotlin.collections.ArrayList


class MPPAdapter(
    private val onClickListener: OnClickListener
)
    : RecyclerView.Adapter<MPPAdapter.ViewHolder>() {

    private var mItem: List<MPPItem> = ArrayList()
    //private var editMode: Boolean =false

    // interface for passing the onClick event to fragment.
    interface OnClickListener {
        fun onItemClick(rID: Long, itemName: String, addNew: Boolean = false)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // inflate the custom view from xml layout file
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.cardview_mpp_item,parent,false)


        // return the view holder
        return ViewHolder(view)

    }


    //@SuppressLint("ResourceAsColor")
    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, @SuppressLint("RecyclerView") position: Int) {
        // display the custom class
        mItem[position].apply {

            holder.name.text = Name

            if (position == mItem.size - 1 && ID == 0L){
                // add
                holder.name.setTextColor(holder.addButtonTextColor)
                holder.name.setOnClickListener {
                    onClickListener.onItemClick(ID,Name,true)
                }

            }else{
                /*
                if (editMode){
                    holder.mainCategoryName.setCompoundDrawables(null,null, holder.iconDelete,null)
                }

                 */
                holder.name.setTextColor(holder.itemTextColor)


                if (position > 0) {
                    // click event
                    holder.name.setOnClickListener {
                        onClickListener.onItemClick(ID, Name)
                    }

                }
            }


        }
    }


    @SuppressLint("NotifyDataSetChanged")
    fun setList(list: List<MPPItem>){
        mItem = list
        notifyDataSetChanged()
    }


    /*
    fun setEditMode(boolean: Boolean){
        editMode = boolean
    }

     */


    override fun getItemCount(): Int {
        // the data set held by the adapter.
        return mItem.size
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val name: TextView = itemView.tv_mpp_name

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