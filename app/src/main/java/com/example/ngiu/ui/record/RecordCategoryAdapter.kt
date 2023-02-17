package com.example.ngiu.ui.record

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.forEach
import androidx.recyclerview.widget.RecyclerView
import com.example.ngiu.R
import com.example.ngiu.data.entities.Category
import kotlinx.android.synthetic.main.cardview_record_category_item.view.*
import kotlin.math.ceil


class RecordCategoryAdapter(
    private val onClickListener: OnClickListener)
    : RecyclerView.Adapter<RecordCategoryAdapter.PagerViewHolder>( ) {

    private var mList: List<Category> = ArrayList()
    private var currentOnFocus: Int = 1
    private var currentPage: Int = 0


    // interface for passing the onClick event to fragment.
    interface OnClickListener {
        fun onItemClick(categoryName: String)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PagerViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.cardview_record_category_item, parent, false)
        return PagerViewHolder(itemView)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onBindViewHolder(holder: PagerViewHolder, position: Int) {

        //holder.bindData(mList[position])
        val i = position * 8

        // load common categories to textview
        if (i < mList.size) {
            holder.tvCate1.text = mList[i].Category_Name
        }else{
            holder.tvCate1.visibility = View.INVISIBLE
        }
        if (i+1 < mList.size) {
            holder.tvCate2.text = mList[i+1].Category_Name
        }else{
            holder.tvCate2.visibility = View.INVISIBLE
        }
        if (i+2 < mList.size) {
            holder.tvCate3.text = mList[i+2].Category_Name
        }else{
            holder.tvCate3.visibility = View.INVISIBLE
        }
        if (i+3 < mList.size) {
            holder.tvCate4.text = mList[i+3].Category_Name
        }else{
            holder.tvCate4.visibility = View.INVISIBLE
        }
        if (i+4 < mList.size) {
            holder.tvCate5.text = mList[i+4].Category_Name
        }else{
            holder.tvCate5.visibility = View.INVISIBLE
        }
        if (i+5 < mList.size) {
            holder.tvCate6.text = mList[i+5].Category_Name
        }else{
            holder.tvCate6.visibility = View.INVISIBLE
        }
        if (i+6 < mList.size) {
            holder.tvCate7.text = mList[i+6].Category_Name
        }else{
            holder.tvCate7.visibility = View.INVISIBLE
        }
        if (i+7 < mList.size) {
            holder.tvCate8.text = mList[i+7].Category_Name
        }else{
            holder.tvCate8.visibility = View.INVISIBLE
        }

        /*
        //Edit mode, highlight the category
        if (strCurrentCategory != ""){
            for (idx in i..(i+7)){
                if (strCurrentCategory == mList[idx].SubCategory_Name){
                    setHighlightCategory(holder, position,idx-i+1, false)
                    strCurrentCategory=""
                }
            }
        }

         */


        //
        holder.tvCate1.setOnClickListener {
            setHighlightCategory(holder, position,1)
        }
        holder.tvCate2.setOnClickListener {
            setHighlightCategory(holder, position,2)
        }
        holder.tvCate3.setOnClickListener {
            setHighlightCategory(holder, position,3)
        }
        holder.tvCate4.setOnClickListener {
            setHighlightCategory(holder, position,4)
        }
        holder.tvCate5.setOnClickListener {
            setHighlightCategory(holder, position,5)
        }
        holder.tvCate6.setOnClickListener {
            setHighlightCategory(holder, position,6)
        }
        holder.tvCate7.setOnClickListener {
            setHighlightCategory(holder, position,7)
        }
        holder.tvCate8.setOnClickListener {
            setHighlightCategory(holder, position,8)
        }

        // touch feedback
        holder.tvItems.forEach {
            it.setOnTouchListener { _, motionEvent ->
                when (motionEvent.actionMasked){
                    MotionEvent.ACTION_DOWN -> it.setBackgroundResource(R.drawable.textview_border_press)
                    MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> it.setBackgroundResource(R.drawable.textview_border)
                }
                false
            }

        }





    }

    private fun setHighlightCategory(holder: PagerViewHolder, pageNumber: Int, setPointer: Int, change: Boolean = true){
        // cancel current highlight button
        /*
        holder.tvCate1.setTextColor(holder.offFocusColor)
        holder.tvCate1.setBackgroundResource(R.drawable.textview_border)
        holder.tvCate2.setTextColor(holder.offFocusColor)
        holder.tvCate2.setBackgroundResource(R.drawable.textview_border)
        holder.tvCate3.setTextColor(holder.offFocusColor)
        holder.tvCate3.setBackgroundResource(R.drawable.textview_border)
        holder.tvCate4.setTextColor(holder.offFocusColor)
        holder.tvCate4.setBackgroundResource(R.drawable.textview_border)
        holder.tvCate5.setTextColor(holder.offFocusColor)
        holder.tvCate5.setBackgroundResource(R.drawable.textview_border)
        holder.tvCate6.setTextColor(holder.offFocusColor)
        holder.tvCate6.setBackgroundResource(R.drawable.textview_border)
        holder.tvCate7.setTextColor(holder.offFocusColor)
        holder.tvCate7.setBackgroundResource(R.drawable.textview_border)
        holder.tvCate8.setTextColor(holder.offFocusColor)
        holder.tvCate8.setBackgroundResource(R.drawable.textview_border)

         */


        // set touched button with highlight
        when (setPointer){
            1-> {
                //holder.tvCate1.setTextColor(holder.onFocusColor)
                //holder.tvCate1.setBackgroundResource(R.drawable.textview_border_active)
                if (change) onClickListener.onItemClick(holder.tvCate1.text.toString())
            }
            2-> {
                //holder.tvCate2.setTextColor(holder.onFocusColor)
                //holder.tvCate2.setBackgroundResource(R.drawable.textview_border_active)
                if (change) onClickListener.onItemClick(holder.tvCate2.text.toString())
            }
            3-> {
                //holder.tvCate3.setTextColor(holder.onFocusColor)
                //holder.tvCate3.setBackgroundResource(R.drawable.textview_border_active)
                if (change) onClickListener.onItemClick(holder.tvCate3.text.toString())
            }
            4-> {
                //holder.tvCate4.setTextColor(holder.onFocusColor)
                //holder.tvCate4.setBackgroundResource(R.drawable.textview_border_active)
                if (change) onClickListener.onItemClick(holder.tvCate4.text.toString())
            }
            5-> {
                //holder.tvCate5.setTextColor(holder.onFocusColor)
                //holder.tvCate5.setBackgroundResource(R.drawable.textview_border_active)
                if (change) onClickListener.onItemClick(holder.tvCate5.text.toString())
            }
            6-> {
                //holder.tvCate6.setTextColor(holder.onFocusColor)
                //holder.tvCate6.setBackgroundResource(R.drawable.textview_border_active)
                if (change) onClickListener.onItemClick(holder.tvCate6.text.toString())
            }
            7-> {
                //holder.tvCate7.setTextColor(holder.onFocusColor)
                //holder.tvCate7.setBackgroundResource(R.drawable.textview_border_active)
                if (change) onClickListener.onItemClick(holder.tvCate7.text.toString())
            }
            8-> {
                //holder.tvCate8.setTextColor(holder.onFocusColor)
                //holder.tvCate8.setBackgroundResource(R.drawable.textview_border_active)
                if (change) onClickListener.onItemClick(holder.tvCate8.text.toString())
            }
        }
        // set current pointer
        currentOnFocus = setPointer
        currentPage = pageNumber

    }

    @SuppressLint("NotifyDataSetChanged")
    fun setList(list: List<Category>) {
        mList = list
        notifyDataSetChanged()
    }



    override fun getItemCount(): Int {
        return  ceil(mList.size.toDouble()/8).toInt()
    }


    //	ViewHolder base from RecycleView.ViewHolder
    class PagerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvCate1: TextView = itemView.tv_record_category_1
        val tvCate2: TextView = itemView.tv_record_category_2
        val tvCate3: TextView = itemView.tv_record_category_3
        val tvCate4: TextView = itemView.tv_record_category_4
        val tvCate5: TextView = itemView.tv_record_category_5
        val tvCate6: TextView = itemView.tv_record_category_6
        val tvCate7: TextView = itemView.tv_record_category_7
        val tvCate8: TextView = itemView.tv_record_category_8
        val tvItems: ConstraintLayout = itemView.tv_record_category_items

        //val onFocusColor: Int = ContextCompat.getColor(itemView.context,R.color.app_section_background)
        //val offFocusColor: Int = R.drawable.textview_border

    }

    // this two methods useful for avoiding duplicate item
    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }


}


