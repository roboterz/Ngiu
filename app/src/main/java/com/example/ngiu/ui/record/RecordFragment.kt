package com.example.ngiu.ui.record


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.ViewModelProvider
import com.example.ngiu.MainActivity
import com.example.ngiu.R
import com.example.ngiu.data.AppDatabase
import com.example.ngiu.data.entities.SubCategory
import com.example.ngiu.data.entities.Trans
import com.example.ngiu.data.entities.returntype.RecordSubCategory
import com.example.ngiu.databinding.FragmentRecordBinding
import kotlinx.android.synthetic.main.fragment_record.*
import kotlin.collections.ArrayList
import com.example.ngiu.functions.addDecimalLimiter
import kotlinx.android.synthetic.main.record_category_item.*
import java.util.*


class RecordFragment : Fragment() {


    private lateinit var recordViewModel: RecordViewModel
    private var _binding: FragmentRecordBinding? = null
    private var vpAdapter: RecordCategoryAdapter? = null
    private var receivedID: Long = 0

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        // Pass value from other fragment
        // --implementation "androidx.fragment:fragment-ktx:1.3.6"
        setFragmentResultListener("requestKey") { _, bundle ->
            receivedID = bundle.getLong("rID")


            if (receivedID > 0) {
                // show delete menu
                toolbar_record.menu.findItem(R.id.action_delete).isVisible = true

                recordViewModel.loadTransactionDetail(activity, receivedID)

            }


        }



    }

    // Fragments use a layout inflater to create their view at this stage.
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        recordViewModel =
            ViewModelProvider(this).get(RecordViewModel::class.java)

        _binding = FragmentRecordBinding.inflate(inflater, container, false)

        // load Data to Ram
        recordViewModel.loadDataToRam(activity)

        Thread {
            // load Data to Ram
            //recordViewModel.loadDataToRam(activity)

            activity?.runOnUiThread {
                // pass the value to fragment from adapter when item clicked
                vpAdapter =
                    this.context?.let {
                        RecordCategoryAdapter(object : RecordCategoryAdapter.OnClickListener {

                            // catch the item click event from adapter
                            override fun onItemClick(string: String) {
                                // do something after clicked
                                tv_record_category.text = string
                                recordViewModel.setSubCategoryName(string)
                            }
                        })
                    }
                // load viewpager2 adapter
                vp_record_category.adapter = vpAdapter
            }
        }.start()

        //recordViewModel.loadDataToRam(activity)



        return binding.root
    }



    // called when the onCreate() method of the view has completed.
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tv_record_amount.addDecimalLimiter()

        // touch Expense textView, switch to Expense page
        tvSectionExpense.setOnClickListener {
            setStatus(recordViewModel.setTransactionType(1))
            switchPage()
            loadCommonCategory(1)
        }
        // touch Income textView, switch to Income page
        tvSectionIncome.setOnClickListener {
            setStatus(recordViewModel.setTransactionType(2))
            switchPage()
            loadCommonCategory(2)
        }
        // touch Transfer textView, switch to Transfer page
        tvSectionTransfer.setOnClickListener {
            setStatus(recordViewModel.setTransactionType(3))
            switchPage()
            loadCommonCategory(3)
        }
        // touch DebitCredit textView, switch to DebitCredit page
        tvSectionDebitCredit.setOnClickListener {
            setStatus(recordViewModel.setTransactionType(4))
            switchPage()
            loadCommonCategory( 4)
        }



        // set up toolbar icon and click event

        // choose items to show
        toolbar_record.menu.findItem(R.id.action_done).isVisible = true
        //toolbar_record.title = "sadfdafdfa"


        // click the navigation Icon in the left side of toolbar
        toolbar_record.setNavigationOnClickListener(View.OnClickListener {

            // call back button event to switch to previous fragment
            requireActivity().onBackPressed()

        })

        // menu item clicked
        toolbar_record.setOnMenuItemClickListener{
            when (it.itemId) {
                R.id.action_done -> {

                    // todo save record
                    saveRecord(receivedID)
                    //tv_record_category.text = recordViewModel.subCategory[recordViewModel.subCategory.indexOfFirst{it.SubCategory_Name== tv_record_category.text}].SubCategory_ID.toString()
                    // call back button event to switch to previous fragment
                    requireActivity().onBackPressed()

                    true
                }
                R.id.action_delete -> {
                    // todo delete record

                    // call back button event to switch to previous fragment
                    requireActivity().onBackPressed()
                    true
                }
                else -> super.onOptionsItemSelected(it)
            }
        }

        /*
        // common categories click event
        tv_record_category_1.setOnClickListener {
            // Reset Text View
            resetTextView()
            // highlight selected textview
            tv_record_category_1.setTextColor(ContextCompat.getColor(requireContext(),R.color.app_button_text))
            tv_record_category_1.setBackgroundResource(R.drawable.textview_border_active)
            // pass the string to category textview
            tv_record_category.text = tv_record_category_1.text
        }

         */

    }



    // called when the fragment is visible and actively running.
    override fun onResume() {
        super.onResume()

        if (receivedID > 0) {

            //recordViewModel.loadTransactionDetail(activity, receivedID)

            // edit record
            //load data to textview
            recordViewModel.setTransactionType(recordViewModel.transDetail.TransactionType_ID.toInt())
            recordViewModel.setSubCategoryName(recordViewModel.transDetail.SubCategory_Name)
            //tv_record_category.text = recordViewModel.transDetail.SubCategory_Name
            tv_record_amount.setText( recordViewModel.transDetail.Transaction_Amount.toString())
            tv_record_account_pay.text = recordViewModel.transDetail.Account_Name
            tv_record_account_pay.text = recordViewModel.transDetail.AccountRecipient_Name
            tv_record_memo.setText(recordViewModel.transDetail.Transaction_Memo)

            setStatus( recordViewModel.currentTransactionType )
            loadCommonCategory( recordViewModel.currentTransactionType.currentTyID,recordViewModel.transDetail.SubCategory_Name )

            //receivedID = 0
        }else{

            // new record
            setStatus( recordViewModel.currentTransactionType.setID(recordViewModel.currentTransactionType.currentTyID) )
            loadCommonCategory( recordViewModel.currentTransactionType.currentTyID )
        }
    }



    override fun onDestroyView() {
        super.onDestroyView()
        (activity as MainActivity).setNavBottomBarVisibility(View.VISIBLE)
        _binding = null
        vpAdapter = null
    }

    // save record
    private fun saveRecord(transactionID: Long = 0) {
        val trans = Trans(
            Transaction_ID = transactionID,
            TransactionType_ID = recordViewModel.currentTransactionType.currentTyID.toLong(),
            SubCategory_ID = recordViewModel.subCategory[recordViewModel.subCategory.indexOfFirst{it.SubCategory_Name== tv_record_category.text}].SubCategory_ID,
            Account_ID = 1L, /* recordViewModel.account[recordViewModel.account.indexOfFirst{it.Account_Name == tv_record_account_pay.text}].Account_ID,*/
            AccountRecipient_ID = 1L, /*if (tv_record_account_receive.text != "") recordViewModel.account[recordViewModel.account.indexOfFirst{it.Account_Name == tv_record_account_receive.text}].Account_ID else 1L,*/
            Transaction_Amount = tv_record_amount.text.toString().toDouble(),
            Transaction_Date = Date(),
            Transaction_Memo = tv_record_memo.text.toString(),
            Merchant_ID = 1L,
            Person_ID = 1L,
            Project_ID = 1L
        )

        if (transactionID > 0) {
            AppDatabase.getDatabase(requireContext()).trans().updateTransaction(trans)
        }else{
            AppDatabase.getDatabase(requireContext()).trans().addTransaction(trans)
        }

    }

    //
    private fun loadCommonCategory( tyID: Int, categoryString: String = "") {
        Thread {
            activity?.runOnUiThread {
                /*
                val cmCategory = ArrayList<RecordSubCategory>()

                for (i in recordViewModel.subCategory.indices){
                    if (recordViewModel.subCategory[i].TransactionType_ID.toInt() == tyID){
                        cmCategory.add(recordViewModel.subCategory[i])
                    }
                }

                 */
                val cmCategory = when (tyID){
                    1 -> recordViewModel.expenseCommonCategory
                    2 -> recordViewModel.incomeCommonCategory
                    3 -> recordViewModel.transferCommonCategory
                    4 -> recordViewModel.debitCreditCommonCategory
                    else -> ArrayList()
                }

                // vpAdapter?.setCategoryString(categoryString)

                vpAdapter?.setList(cmCategory)
                //vpAdapter?.setCategoryString(categoryString)

                vp_record_category.adapter= vpAdapter

            }
        }.start()
    }



    private fun setStatus(ctt: CurrentTransactionType){
        tvSectionExpense.setTextColor(ContextCompat.getColor(requireContext(),ctt.expense))
        tvSectionExpensePointer.visibility = ctt.expensePointer
        tvSectionIncome.setTextColor(ContextCompat.getColor(requireContext(),ctt.income))
        tvSectionIncomePointer.visibility = ctt.incomePointer
        tvSectionTransfer.setTextColor(ContextCompat.getColor(requireContext(),ctt.transfer))
        tvSectionTransferPointer.visibility = ctt.transferPointer
        tvSectionDebitCredit.setTextColor(ContextCompat.getColor(requireContext(),ctt.debitCredit))
        tvSectionDebitCreditPointer.visibility = ctt.debitCreditPointer
        tv_record_amount.setTextColor(
            when (ctt.currentTyID){
                1 -> ContextCompat.getColor( requireContext(), R.color.app_expense_amount)
                2 -> ContextCompat.getColor( requireContext(), R.color.app_income_amount)
                else -> ContextCompat.getColor( requireContext(), R.color.app_amount)
            }
        )
        /*
        for (i in recordViewModel.subCategory.indices){
            if (recordViewModel.subCategory[i].TransactionType_ID.toInt() == ctt.currentTyID){
                tv_record_category.text = recordViewModel.subCategory[i].SubCategory_Name
                break
            }
        }

         */
        /*
        tv_record_category.text = when (ctt.currentTyID) {
            1 -> recordViewModel.expenseCommonCategory[0].SubCategory_Name
            2 -> recordViewModel.incomeCommonCategory[0].SubCategory_Name
            3 -> recordViewModel.transferCommonCategory[0].SubCategory_Name
            4 -> recordViewModel.debitCreditCommonCategory[0].SubCategory_Name
            else -> ""
        }

         */
        tv_record_category.text = recordViewModel.getSubCategoryName()

    }

    private fun switchPage(){

    }



}



