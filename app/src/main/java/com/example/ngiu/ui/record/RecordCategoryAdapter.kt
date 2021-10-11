package com.example.ngiu.ui.record

import android.graphics.Color
import android.icu.number.Scale
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ngiu.R
import com.example.ngiu.data.entities.SubCategory
import kotlinx.android.synthetic.main.record_category_item.view.*
import org.w3c.dom.Text
import kotlin.math.roundToInt


class RecordCategoryAdapter : RecyclerView.Adapter<RecordCategoryAdapter.PagerViewHolder>() {
    private var mList: List<String> = ArrayList()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PagerViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.record_category_item, parent, false)
        return PagerViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: PagerViewHolder, position: Int) {
        //holder.bindData(mList[position])
        val i = position * 8

        if (i < mList.size) {
            holder.tvCate1.text = mList[i]
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


    }

    fun setList(list: List<String>) {
        mList = list
    }

    override fun getItemCount(): Int {
        return  Math.ceil(mList.size.toDouble()/8).toInt()
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



    }
}
