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
import kotlin.math.roundToInt


class RecordCategoryAdapter : RecyclerView.Adapter<RecordCategoryAdapter.PagerViewHolder>() {
    private var mList: List<Int> = ArrayList()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PagerViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.record_category_item, parent, false)
        return PagerViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: PagerViewHolder, position: Int) {
        //holder.bindData(mList[position])
    }

    fun setList(list: List<Int>) {
        mList = list
    }

    override fun getItemCount(): Int {
        return  Math.ceil(mList.size.toDouble()/8).toInt()
    }
    //	ViewHolder需要继承RecycleView.ViewHolder
    class PagerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val mTextView: TextView = itemView.findViewById(R.id.tv_record_category_1)


    }
}
