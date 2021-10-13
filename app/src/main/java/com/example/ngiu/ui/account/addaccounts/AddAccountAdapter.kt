package com.example.ngiu.ui.account.addaccounts

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.ngiu.databinding.AddAccountItemBinding
import com.example.ngiu.ui.account.model.AccountTypeUIModel


class AddAccountAdapter :
    ListAdapter<AccountTypeUIModel, AddAccountAdapter.AddAccountViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddAccountViewHolder {
        return AddAccountItemBinding.inflate(LayoutInflater.from(parent.context), parent, false).run {
            AddAccountViewHolder(this)
        }
    }

    override fun onBindViewHolder(holder: AddAccountViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class AddAccountViewHolder(private val binding: AddAccountItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        // Display memo if account type has memo
        fun bind(uiModel: AccountTypeUIModel) {

          /*  binding.root.setOnClickListener(
                {view->


            })*/
            binding.tvAccountTypeTitle.text = uiModel.Name
            if (!uiModel.Memo.isNullOrBlank()) {
                binding.tvAcctTypeSubTitle.isVisible = true
                binding.tvAcctTypeSubTitle.text = uiModel.Name
            }
        }
    }

    //check to see if items and contents are the same
    private class DiffCallback : DiffUtil.ItemCallback<AccountTypeUIModel>() {
        override fun areItemsTheSame(
            oldItem: AccountTypeUIModel,
            newItem: AccountTypeUIModel,
        ): Boolean = oldItem.Name == newItem.Memo

        override fun areContentsTheSame(
            oldItem: AccountTypeUIModel,
            newItem: AccountTypeUIModel,
        ): Boolean = oldItem == newItem
    }
}