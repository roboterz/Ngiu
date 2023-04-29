package com.example.ngiu.ui.account.detail


import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ngiu.R
import com.example.ngiu.functions.TRANSACTION_TYPE_DEBIT
import com.example.ngiu.functions.switchToRecordFragment
import com.example.ngiu.ui.account.model.AccountCreditDetailGroupModel
import kotlinx.android.synthetic.main.cardview_account_credit_detail_group.view.*
import java.time.format.DateTimeFormatter


class AccountCreditDetailGroupAdapter(
    private val onClickListener: OnClickListener
) : RecyclerView.Adapter<AccountCreditDetailGroupAdapter.CreditDetailGroupViewHolder>() {
    private val statementList = ArrayList<AccountCreditDetailGroupModel>()

    private val itemDateFormatter = DateTimeFormatter.ofPattern("MM/dd")
    private var currentAccountID = 0L

    // interface for passing the onClick event to fragment.
    interface OnClickListener {
        fun onItemClick(transID: Long)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CreditDetailGroupViewHolder{
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.cardview_account_credit_detail_group, parent, false);
        return CreditDetailGroupViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: CreditDetailGroupViewHolder, position: Int) {

        statementList[position].apply {
            holder.tvMonth.text = TermEndDate.monthValue.toString()
            holder.tvYear.text = TermEndDate.year.toString()

            if  (StatementStatus) {
                holder.tvStatus.text = holder.textAmountDue
                holder.tvAmount.text = "$" + "%.2f".format(DueAmount)
            }else{
                holder.tvStatus.text = holder.textStatementNotGenerated
                holder.tvAmount.text = ""
            }

            //holder.ivExpanded
            if (IsExpanded){
                holder.rvDetail.visibility = View.VISIBLE
                holder.bottomLine.visibility = View.GONE
                holder.ivExpanded.setImageDrawable(holder.iconExpand)
            }else{
                holder.rvDetail.visibility = View.GONE
                holder.bottomLine.visibility = View.VISIBLE
                holder.ivExpanded.setImageDrawable(holder.iconCollapse)
            }

            // todo
            holder.tvTerm.text =  TermStartDate.format(itemDateFormatter) + " - " + TermEndDate.format(itemDateFormatter)


            // adapter
            //val creditDetailAdapter = AccountCreditDetailAdapter()
            val creditDetailAdapter = AccountCreditDetailAdapter(object: AccountCreditDetailAdapter.OnClickListener {
                    // catch the item click event from adapter
                    override fun onItemClick(transID: Long) {
                        // switch to record fragment (Edit mode)
                        onClickListener.onItemClick(transID)
                    }
                })

            holder.rvDetail.layoutManager = LinearLayoutManager(holder.itemView.context, RecyclerView.VERTICAL,false)
            creditDetailAdapter.setList(CDList)
            creditDetailAdapter.setAccountID(currentAccountID)
            holder.rvDetail.adapter = creditDetailAdapter


            holder.groupItem.setOnClickListener {
                if (IsExpanded) {
                    holder.rvDetail.visibility = View.GONE
                    holder.bottomLine.visibility = View.VISIBLE
                    holder.ivExpanded.setImageDrawable(holder.iconCollapse)
                    IsExpanded = false
                }else{
                    holder.rvDetail.visibility = View.VISIBLE
                    holder.bottomLine.visibility = View.GONE
                    holder.ivExpanded.setImageDrawable(holder.iconExpand)
                    IsExpanded = true
                }
            }

        }


    }



    override fun getItemCount(): Int {
        return statementList.size
    }


    @SuppressLint("NotifyDataSetChanged")
    fun setStatementList(data: List<AccountCreditDetailGroupModel>){
        statementList.clear()
        statementList.addAll(data)
        notifyDataSetChanged()
    }

    fun setAccountID(acctID: Long){
        currentAccountID = acctID
    }

    class CreditDetailGroupViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val tvTerm: TextView = itemView.tv_account_credit_detail_group_term
        val tvMonth: TextView = itemView.tv_account_credit_detail_group_month
        val tvYear: TextView = itemView.tv_account_credit_detail_group_year
        val tvStatus: TextView = itemView.tv_account_credit_detail_group_text
        val tvAmount: TextView = itemView.tv_account_credit_detail_group_amount
        val rvDetail: RecyclerView = itemView.recyclerview_account_credit_detail_item
        var ivExpanded: ImageView = itemView.iv_account_credit_detail_group_expand_collapse
        val groupItem: ConstraintLayout = itemView.layout_account_credit_detail_group_item
        val bottomLine: TextView = itemView.tv_account_credit_detail_group_bottom_line

        val textStatementNotGenerated = itemView.context.getString(R.string.option_account_statement_not_generated)
        val textAmountDue = itemView.context.getString(R.string.option_account_amount_due)
        val iconExpand = ContextCompat.getDrawable(itemView.context, R.drawable.ic_baseline_arrow_drop_down_24)
        val iconCollapse = ContextCompat.getDrawable(itemView.context, R.drawable.ic_baseline_arrow_right_24)
    }

}