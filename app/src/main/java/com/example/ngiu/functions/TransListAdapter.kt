package com.example.ngiu.functions

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.room.TypeConverters
import com.example.ngiu.R
import com.example.ngiu.data.entities.DateTypeConverter
import com.example.ngiu.data.entities.Person
import com.example.ngiu.data.entities.Trans
import kotlinx.android.synthetic.main.transaction_cardview.view.*
import java.util.*


class TransListAdapter(private val trans: MutableList<Trans>)
    : RecyclerView.Adapter<TransListAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // inflate the custom view from xml layout file
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.transaction_cardview,parent,false)


        // return the view holder
        return ViewHolder(view)

    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // display the custom class
        trans[position].apply {
            holder.ID.text = "$ID"
            //holder.Name.text = "$Name"


            if (holder.ID.text.toString().toLong() > 10) {
                holder.ID.setTextColor(-0xffff01)
            }
            if (holder.ID.text.toString().toLong() > 20) {
                holder.ID.setTextColor(-0xff5501)
            }
            if (holder.ID.text.toString().toLong() > 30) {
                holder.ID.setTextColor(-0xff0101)
            }
        }
    }


    override fun getItemCount(): Int {
        // the data set held by the adapter.
        return trans.size
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val ID: TextView = itemView.tvTrans_day
    }


    // this two methods useful for avoiding duplicate item
    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }
}