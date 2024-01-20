package com.aerolite.ngiu.ui.rewards

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.aerolite.ngiu.R
import com.aerolite.ngiu.data.entities.returntype.AccountIcon
//import kotlinx.android.synthetic.main.cardview_rewards_card.view.iv_rewards_card_item_image
//import kotlinx.android.synthetic.main.cardview_rewards_card.view.tv_rewards_card_item_name
//import kotlinx.android.synthetic.main.fragment_rewards.recyclerView_rewards

class CardsAdapter(private val onClickListener: OnClickListener)
    : RecyclerView.Adapter<CardsAdapter.PagerViewHolder>( ) {

        private var mList: List<AccountIcon> = ArrayList()

        private lateinit var rewardsViewModel: RewardsViewModel

        // interface for passing the onClick event to fragment.
        interface OnClickListener {
            fun onItemClick(accountID: Long)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PagerViewHolder {
            val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.cardview_rewards_card, parent, false)

            return PagerViewHolder(itemView)
        }

        @SuppressLint("ClickableViewAccessibility")
        override fun onBindViewHolder(holder: PagerViewHolder, position: Int) {

            //holder.bindData(mList[position])
            //val i = position * 8
            //mList[position].apply {

            if (mList[position].Icon_Image != null) {
                holder.imgCard.setImageBitmap(mList[position].Icon_Image)
            }
                //holder.imgCard.setImageBitmap(mList[0].Icon_Image)
            holder.tvName.text = mList[position].Account_Name
            //}

            onClickListener.onItemClick(mList[position].Account_ID)
        }



        @SuppressLint("NotifyDataSetChanged")
        fun setList(list: List<AccountIcon>) {
            mList = list
            notifyDataSetChanged()
        }



        override fun getItemCount(): Int {
            return mList.size
        }


        //	ViewHolder base from RecycleView.ViewHolder
        class PagerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            var imgCard: ImageView = itemView.findViewById(R.id.iv_rewards_card_item_image)
            var tvName: TextView = itemView.findViewById(R.id.tv_rewards_card_item_name)

        }

        // this two methods useful for avoiding duplicate item
        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getItemViewType(position: Int): Int {
            return position
        }


    }