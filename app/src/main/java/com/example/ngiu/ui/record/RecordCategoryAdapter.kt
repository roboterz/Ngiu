package com.example.ngiu.ui.record

import android.graphics.Color
import android.icu.number.Scale
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.ngiu.R
import com.example.ngiu.data.entities.SubCategory
import kotlinx.android.synthetic.main.record_category_item.view.*
import org.w3c.dom.Text
import kotlin.math.ceil
import kotlin.math.roundToInt


class RecordCategoryAdapter(private val onClickListener: OnClickListener) : RecyclerView.Adapter<RecordCategoryAdapter.PagerViewHolder>( ) {
    private var mList: List<String> = ArrayList()
    private var currentOnFocus: Int = 1

    // interface for passing the onClick event to fragment.
    interface OnClickListener {
        fun onItemClick(string: String)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PagerViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.record_category_item, parent, false)
        return PagerViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: PagerViewHolder, position: Int) {
        //holder.bindData(mList[position])
        val i = position * 8

        if (i < mList.size) {
            holder.tvCate1.text = mList[i]
            setHighlightCategory(holder,0,1)
        }else{
            holder.tvCate1.visibility = View.INVISIBLE
        }
        if (i+1 < mList.size) {
            holder.tvCate2.text = mList[i+1]
        }else{
            holder.tvCate2.visibility = View.INVISIBLE
        }
        if (i+2 < mList.size) {
            holder.tvCate3.text = mList[i+2]
        }else{
            holder.tvCate3.visibility = View.INVISIBLE
        }
        if (i+3 < mList.size) {
            holder.tvCate4.text = mList[i+3]
        }else{
            holder.tvCate4.visibility = View.INVISIBLE
        }
        if (i+4 < mList.size) {
            holder.tvCate5.text = mList[i+4]
        }else{
            holder.tvCate5.visibility = View.INVISIBLE
        }
        if (i+5 < mList.size) {
            holder.tvCate6.text = mList[i+5]
        }else{
            holder.tvCate6.visibility = View.INVISIBLE
        }
        if (i+6 < mList.size) {
            holder.tvCate7.text = mList[i+6]
        }else{
            holder.tvCate7.visibility = View.INVISIBLE
        }
        if (i+7 < mList.size) {
            holder.tvCate8.text = mList[i+7]
        }else{
            holder.tvCate8.visibility = View.INVISIBLE
        }

        //
        holder.tvCate1.setOnClickListener {
            setHighlightCategory(holder, currentOnFocus,1)
        }
        holder.tvCate2.setOnClickListener {
            setHighlightCategory(holder, currentOnFocus,2)
        }
        holder.tvCate3.setOnClickListener {
            setHighlightCategory(holder, currentOnFocus,3)
        }
        holder.tvCate4.setOnClickListener {
            setHighlightCategory(holder, currentOnFocus,4)
        }
        holder.tvCate5.setOnClickListener {
            setHighlightCategory(holder, currentOnFocus,5)
        }
        holder.tvCate6.setOnClickListener {
            setHighlightCategory(holder, currentOnFocus,6)
        }
        holder.tvCate7.setOnClickListener {
            setHighlightCategory(holder, currentOnFocus,7)
        }
        holder.tvCate8.setOnClickListener {
            setHighlightCategory(holder, currentOnFocus, 8)
        }


    }

    private fun setHighlightCategory(holder: PagerViewHolder, currentPointer: Int, setPointer: Int){
        // cancel current highlight button
        when (currentPointer){
            1-> {
                    holder.tvCate1.setTextColor(holder.offFocusColor)
                    holder.tvCate1.setBackgroundResource(R.drawable.textview_border)
                }
            2-> {
                    holder.tvCate2.setTextColor(holder.offFocusColor)
                    holder.tvCate2.setBackgroundResource(R.drawable.textview_border)
                }
            3-> {
                    holder.tvCate3.setTextColor(holder.offFocusColor)
                    holder.tvCate3.setBackgroundResource(R.drawable.textview_border)
                }
            4-> {
                    holder.tvCate4.setTextColor(holder.offFocusColor)
                    holder.tvCate4.setBackgroundResource(R.drawable.textview_border)
                }
            5-> {
                    holder.tvCate5.setTextColor(holder.offFocusColor)
                    holder.tvCate5.setBackgroundResource(R.drawable.textview_border)
                }
            6-> {
                    holder.tvCate6.setTextColor(holder.offFocusColor)
                    holder.tvCate6.setBackgroundResource(R.drawable.textview_border)
                }
            7-> {
                    holder.tvCate7.setTextColor(holder.offFocusColor)
                    holder.tvCate7.setBackgroundResource(R.drawable.textview_border)
                }
            8-> {
                    holder.tvCate8.setTextColor(holder.offFocusColor)
                    holder.tvCate8.setBackgroundResource(R.drawable.textview_border)
                }
        }
        // set touched button with highlight
        when (setPointer){
            1-> {
                holder.tvCate1.setTextColor(holder.onFocusColor)
                holder.tvCate1.setBackgroundResource(R.drawable.textview_border_active)
                onClickListener.onItemClick(holder.tvCate1.text.toString())
            }
            2-> {
                holder.tvCate2.setTextColor(holder.onFocusColor)
                holder.tvCate2.setBackgroundResource(R.drawable.textview_border_active)
                onClickListener.onItemClick(holder.tvCate2.text.toString())
            }
            3-> {
                holder.tvCate3.setTextColor(holder.onFocusColor)
                holder.tvCate3.setBackgroundResource(R.drawable.textview_border_active)
                onClickListener.onItemClick(holder.tvCate3.text.toString())
            }
            4-> {
                holder.tvCate4.setTextColor(holder.onFocusColor)
                holder.tvCate4.setBackgroundResource(R.drawable.textview_border_active)
                onClickListener.onItemClick(holder.tvCate4.text.toString())
            }
            5-> {
                holder.tvCate5.setTextColor(holder.onFocusColor)
                holder.tvCate5.setBackgroundResource(R.drawable.textview_border_active)
                onClickListener.onItemClick(holder.tvCate5.text.toString())
            }
            6-> {
                holder.tvCate6.setTextColor(holder.onFocusColor)
                holder.tvCate6.setBackgroundResource(R.drawable.textview_border_active)
                onClickListener.onItemClick(holder.tvCate6.text.toString())
            }
            7-> {
                holder.tvCate7.setTextColor(holder.onFocusColor)
                holder.tvCate7.setBackgroundResource(R.drawable.textview_border_active)
                onClickListener.onItemClick(holder.tvCate7.text.toString())
            }
            8-> {
                holder.tvCate8.setTextColor(holder.onFocusColor)
                holder.tvCate8.setBackgroundResource(R.drawable.textview_border_active)
                onClickListener.onItemClick(holder.tvCate8.text.toString())
            }
        }
        // set current pointer
        currentOnFocus = setPointer
    }

    fun setList(list: List<String>) {
        mList = list
    }

    override fun getItemCount(): Int {
        return  ceil(mList.size.toDouble()/8).toInt()
    }


    //	ViewHolder需要继承RecycleView.ViewHolder
    class PagerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvCate1: TextView = itemView.findViewById(R.id.tv_record_category_1)
        val tvCate2: TextView = itemView.findViewById(R.id.tv_record_category_2)
        val tvCate3: TextView = itemView.findViewById(R.id.tv_record_category_3)
        val tvCate4: TextView = itemView.findViewById(R.id.tv_record_category_4)
        val tvCate5: TextView = itemView.findViewById(R.id.tv_record_category_5)
        val tvCate6: TextView = itemView.findViewById(R.id.tv_record_category_6)
        val tvCate7: TextView = itemView.findViewById(R.id.tv_record_category_7)
        val tvCate8: TextView = itemView.findViewById(R.id.tv_record_category_8)

        val onFocusColor: Int = ContextCompat.getColor(itemView.context,R.color.app_button_text)
        val offFocusColor: Int = ContextCompat.getColor(itemView.context,R.color.app_option_text)

    }


}
