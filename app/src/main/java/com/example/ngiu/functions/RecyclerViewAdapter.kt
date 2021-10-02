package com.example.ngiu.functions

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ngiu.R
import com.example.ngiu.data.entities.Person
import kotlinx.android.synthetic.main.custom_item.view.*


class RecyclerViewAdapter(private val person: MutableList<Person>)
    : RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // inflate the custom view from xml layout file
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.custom_item,parent,false)


        // return the view holder
        return ViewHolder(view)

    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // display the custom class
        person[position].apply {
            holder.ID.text = "$ID"
            holder.Name.text = "$Name"
        }
    }


    override fun getItemCount(): Int {
        // the data set held by the adapter.
        return person.size
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val ID: TextView = itemView.tvName
        val Name: TextView = itemView.tvLocation
    }


    // this two methods useful for avoiding duplicate item
    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }
}