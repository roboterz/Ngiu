package com.example.ngiu.ui.account.addaccounts

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.ngiu.R
import com.example.ngiu.databinding.AccountAddAccountItemBinding
import com.example.ngiu.ui.account.model.AccountTypeUIModel


class AddAccountAdapter(val context: Context) :
    ListAdapter<AccountTypeUIModel, AddAccountAdapter.AddAccountViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddAccountViewHolder {
        return AccountAddAccountItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            .run {
                val holder = AddAccountViewHolder(this);

                this.root.setOnClickListener {
                    when (holder.adapterPosition) {
                        0 -> holder.itemView.findNavController().navigate(R.id.addCashFragment)
                        1 -> holder.itemView.findNavController().navigate(R.id.addCreditFragment)
                        2 -> holder.itemView.findNavController().navigate(R.id.addDebitFragment)
                        //3 -> supposed to be investment
                        3 -> holder.itemView.findNavController().navigate(R.id.addDebitFragment)
                        4 -> holder.itemView.findNavController().navigate(R.id.addWebAccountFragment)
                        //5-> supposed to be store value
                        5 -> holder.itemView.findNavController().navigate(R.id.addVirtualAccountFragment)
                        6 -> holder.itemView.findNavController().navigate(R.id.addVirtualAccountFragment)
                        7 -> holder.itemView.findNavController().navigate(R.id.addPermanentAssetFragment)
                        //8-> supposed to be receivable/payable
                        8 -> holder.itemView.findNavController().navigate(R.id.addVirtualAccountFragment)
                    }
                }

                holder
            }
    }

    override fun onBindViewHolder(holder: AddAccountViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class AddAccountViewHolder(private val binding: AccountAddAccountItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        // Display memo if account type has memo
        fun bind(uiModel: AccountTypeUIModel) {
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