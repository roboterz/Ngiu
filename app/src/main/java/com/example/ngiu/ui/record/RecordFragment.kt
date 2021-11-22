package com.example.ngiu.ui.record


import android.animation.Animator
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.DialogInterface
import android.os.Bundle
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.core.content.ContextCompat
import androidx.core.content.contentValuesOf
import androidx.core.view.forEach
import androidx.fragment.app.*
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.transition.Fade
import androidx.transition.TransitionManager
import com.example.ngiu.MainActivity
import com.example.ngiu.R
import com.example.ngiu.data.AppDatabase
import com.example.ngiu.data.entities.Trans
import com.example.ngiu.databinding.FragmentRecordBinding
import com.example.ngiu.functions.DateTimePicker
import com.example.ngiu.functions.SelectItem
import kotlinx.android.synthetic.main.fragment_record.*
import kotlin.collections.ArrayList
import com.example.ngiu.functions.popupWindow
import com.example.ngiu.ui.keyboard.Keyboard
import kotlinx.android.synthetic.main.popup_title.view.*
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
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
        // hide bottom bar
        (activity as MainActivity).setNavBottomBarVisibility(View.GONE)

        // Pass value from other fragment
        // --implementation "androidx.fragment:fragment-ktx:1.3.6"
        setFragmentResultListener("requestKey") { _, bundle ->
            receivedID = bundle.getLong("rID")


            if (receivedID > 0) {
                // show delete menu
                toolbar_record.menu.findItem(R.id.action_delete).isVisible = true
                // show delete button
                tv_record_left_button.text = getText(R.string.menu_delete)

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
    @SuppressLint("SetTextI18n", "ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //tv_record_amount.addDecimalLimiter()

        // touch Expense textView, switch to Expense page
        tvSectionExpense.setOnClickListener {
            setStatus(recordViewModel.setTransactionType(1))
            loadCommonCategory(1)
        }
        // touch Income textView, switch to Income page
        tvSectionIncome.setOnClickListener {
            setStatus(recordViewModel.setTransactionType(2))
            loadCommonCategory(2)
        }
        // touch Transfer textView, switch to Transfer page
        tvSectionTransfer.setOnClickListener {
            setStatus(recordViewModel.setTransactionType(3))
            loadCommonCategory(3)
        }
        // touch DebitCredit textView, switch to DebitCredit page
        tvSectionDebitCredit.setOnClickListener {
            setStatus(recordViewModel.setTransactionType(4))
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
                // done menu
                R.id.action_done -> {
                    // save record
                    if (saveRecord(receivedID) == 0) {
                        // call back button event to switch to previous fragment
                        requireActivity().onBackPressed()
                    }
                    true
                }
                // delete menu
                R.id.action_delete -> {
                    // delete record
                    deleteRecord(activity, receivedID)
                    true
                }

                else -> super.onOptionsItemSelected(it)
            }
        }

        // Save Button
        tv_record_right_button.setOnClickListener {
            if (saveRecord(receivedID) == 0) {
                // exit
                requireActivity().onBackPressed()
            }
        }

        // Save and Next Button | Delete Button
        tv_record_left_button.setOnClickListener {
            if (receivedID > 0){
                // delete
                deleteRecord(activity, receivedID)
            }else{
                // save and next
                saveRecord()
                tv_record_amount.setText("0.00")
            }
        }

        // date
        tv_record_date.setOnClickListener {
            // date picker
            val date = LocalDate.parse(tv_record_date.text.toString(), DateTimeFormatter.ofPattern("MM/dd/yyyy"))

            DateTimePicker(
                date.year,
                date.monthValue-1,
                date.dayOfMonth
            ).pickDate(view.context, DatePickerDialog.OnDateSetListener{ _, year, month, day ->
                val mth = month + 1
                val m = if (mth < 10) "0$mth" else "$mth"
                val d = if (day < 10) "0$day" else "$day"
                tv_record_date.text = "$m/$d/$year"
            })

        }

        // time
        tv_record_time.setOnClickListener {
            // time picker
            val time = LocalTime.parse(tv_record_time.text.toString(), DateTimeFormatter.ofPattern("HH:mm"))

            DateTimePicker(
                startHour = time.hour,
                startMinute = time.minute
            ).pickTime(context, TimePickerDialog.OnTimeSetListener { _, hour, minute ->
                val h = if (hour < 10) "0$hour" else "$hour"
                val m = if (minute < 10) "0$minute" else "$minute"
                tv_record_time.text  = "$h:$m"
            })
        }

        // reimburse
        tv_record_reimburse.setOnClickListener {
            // change text
            tv_record_reimburse.text = recordViewModel.setReimbursable(it.context)
            // change icon
            setReimburseIcon(recordViewModel.transDetail.Transaction_ReimburseStatus)
        }

        // person
        tv_record_person.setOnClickListener {
            val tList: MutableList<String> = ArrayList<String>()
            for (person in recordViewModel.person){
                tList.add(person.Person_Name)
            }
            popupWindow(requireContext(),getText(R.string.setting_merchant).toString(),  tList.toTypedArray(),
                object : SelectItem {
                    override fun clicked(idx: Int) {
                        tv_record_person.text = tList[idx]
                    }
                })
        }

        // Merchat
        tv_record_merchant.setOnClickListener {
            val tList: MutableList<String> = ArrayList<String>()
            for (merchant in recordViewModel.merchant){
                tList.add(merchant.Merchant_Name)
            }
            popupWindow(requireContext(),getText(R.string.setting_merchant).toString(),  tList.toTypedArray(),
                object : SelectItem {
                    override fun clicked(idx: Int) {
                        tv_record_merchant.text = tList[idx]
                    }
                })
        }

        // Project
        tv_record_project.setOnClickListener {
            val tList: MutableList<String> = ArrayList<String>()
            for (project in recordViewModel.project){
                tList.add(project.Project_Name)
            }
            popupWindow(requireContext(),getText(R.string.setting_merchant).toString(),  tList.toTypedArray(),
                object : SelectItem {
                    override fun clicked(idx: Int) {
                        tv_record_project.text = tList[idx]
                    }
                })
        }

        // touch feedback
        layout_record_other_info.forEach {
            if (it.tag == "other_info"){
                it.setOnTouchListener { _, motionEvent ->
                    when (motionEvent.actionMasked){
                        MotionEvent.ACTION_DOWN -> it.setBackgroundResource(R.drawable.textview_border_press)
                        MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> it.setBackgroundResource(R.drawable.textview_additional_border)
                    }
                    false
                }
            }
        }

        // all category
        tv_record_all_category.setOnClickListener{
            // hide nav bottom bar
            //(activity as MainActivity).setNavBottomBarVisibility(View.GONE)

            //parentFragmentManager.setFragmentResult("requestKey", bundleOf("bundleKey" to transactionList[position].Transaction_ID))
            //if (transID >0) setFragmentResult("requestKey", bundleOf("rID" to transID))

            // switch to category manage fragment
            findNavController().navigate(R.id.navigation_category_manage)
        }

        // category
        tv_record_category.setOnClickListener {
            // hide nav bottom bar
            //(activity as MainActivity).setNavBottomBarVisibility(View.GONE)

            // switch to category manage fragment
            findNavController().navigate(R.id.navigation_category_manage)
        }

        // amount
        tv_record_amount.setOnClickListener {
            Keyboard(view).initKeys(tv_record_amount)
            Keyboard(view).show()
        }

        // swap
        iv_record_swap.setOnClickListener {
            tv_record_account_receive.text = tv_record_account_pay.text.apply { tv_record_account_pay.text = tv_record_account_receive.text }
        }

        // account pay
        tv_record_account_pay.setOnClickListener {
            val tList: MutableList<String> = ArrayList<String>()
            for (account in recordViewModel.account){
                tList.add(account.Account_Name)
            }
            popupWindow(requireContext(),getText(R.string.setting_merchant).toString(),  tList.toTypedArray(),
                object : SelectItem {
                    override fun clicked(idx: Int) {
                        tv_record_account_pay.text = tList[idx]
                    }
                })
        }

        // account receive
        tv_record_account_receive.setOnClickListener {
            val tList: MutableList<String> = ArrayList<String>()
            for (account in recordViewModel.account){
                tList.add(account.Account_Name)
            }
            popupWindow(requireContext(),getText(R.string.setting_merchant).toString(),  tList.toTypedArray(),
                object : SelectItem {
                    override fun clicked(idx: Int) {
                        tv_record_account_receive.text = tList[idx]
                    }
                })
        }
    }



    // called when the fragment is visible and actively running.
    override fun onResume() {
        super.onResume()

        // load data to UI textview
        loadUIData(receivedID)
    }




    override fun onDestroyView() {
        super.onDestroyView()
        (activity as MainActivity).setNavBottomBarVisibility(View.VISIBLE)
        _binding = null
        vpAdapter = null
    }




    //------------------------------------------Privete Functions--------------------------------------------------

    @SuppressLint("SetTextI18n")
    private fun loadUIData(transactionID: Long){
        if (transactionID > 0) {

            // edit record

            //load data to textview
            recordViewModel.setTransactionType(recordViewModel.transDetail.TransactionType_ID.toInt())
            recordViewModel.setSubCategoryName(recordViewModel.transDetail.SubCategory_Name)
            //tv_record_category.text = recordViewModel.transDetail.SubCategory_Name
            tv_record_date.text = DateFormat.format("MM/dd/yyy", recordViewModel.transDetail.Transaction_Date)
            tv_record_time.text = DateFormat.format("HH:mm", recordViewModel.transDetail.Transaction_Date)

            if (recordViewModel.transDetail.Transaction_Amount.toString().contains('.') && recordViewModel.transDetail.Transaction_Amount.toString().last() =='0'){
                tv_record_amount.text = recordViewModel.transDetail.Transaction_Amount.toString() + "0"
            }else{
                tv_record_amount.text = recordViewModel.transDetail.Transaction_Amount.toString()
            }
            //tv_record_amount.text = recordViewModel.transDetail.Transaction_Amount.toString()
            tv_record_account_pay.text = recordViewModel.transDetail.Account_Name
            tv_record_account_receive.text = recordViewModel.transDetail.AccountRecipient_Name
            tv_record_memo.setText(recordViewModel.transDetail.Transaction_Memo)
            tv_record_person.text = recordViewModel.transDetail.Person_Name
            tv_record_merchant.text = recordViewModel.transDetail.Merchant_Name
            tv_record_project.text = recordViewModel.transDetail.Project_Name
            tv_record_reimburse.text = recordViewModel.getReimbursable(requireContext(), recordViewModel.transDetail.Transaction_ReimburseStatus)
            setReimburseIcon(recordViewModel.transDetail.Transaction_ReimburseStatus)
            //todo period,

            setStatus( recordViewModel.currentTransactionType )
            loadCommonCategory( recordViewModel.currentTransactionType.currentTyID,recordViewModel.transDetail.SubCategory_Name )

        }else{

            // new record
            tv_record_date.text = DateFormat.format("MM/dd/yyy", Date())
            tv_record_time.text = DateFormat.format("HH:mm", Date())
            tv_record_account_pay.text = recordViewModel.account[0].Account_Name
            tv_record_account_receive.text = recordViewModel.account[1].Account_Name
            tv_record_person.text = recordViewModel.person[0].Person_Name
            tv_record_merchant.text = recordViewModel.merchant[0].Merchant_Name
            tv_record_project.text = recordViewModel.project[0].Project_Name
            tv_record_reimburse.text = recordViewModel.getReimbursable(requireContext(), 0)
            //todo  period

            setStatus( recordViewModel.currentTransactionType.setID(recordViewModel.currentTransactionType.currentTyID) )
            loadCommonCategory( recordViewModel.currentTransactionType.currentTyID )
        }
    }

    // save record
    private fun saveRecord(transactionID: Long = 0) : Int{

        // If amount is 0, do not save
        if (tv_record_amount.text.toString().toDouble() == 0.0) {
            Toast.makeText(context,getText(R.string.msg_cannot_save_with_zero_amount),Toast.LENGTH_SHORT).show()
            //Snackbar.make(requireView(), getText(R.string.msg_cannot_save_with_zero_amount), Snackbar.LENGTH_SHORT).show()
            return 1

        }else {
            // get date time
            val strDate = tv_record_date.text.toString() + " " + tv_record_time.text.toString()
            // get trans data
            // todo get data from sub table
            val trans = Trans(
                Transaction_ID = transactionID,
                TransactionType_ID = recordViewModel.currentTransactionType.currentTyID.toLong(),
                SubCategory_ID = recordViewModel.subCategory[recordViewModel.subCategory.indexOfFirst { it.SubCategory_Name == tv_record_category.text }].SubCategory_ID,
                Account_ID = recordViewModel.account[recordViewModel.account.indexOfFirst{it.Account_Name == tv_record_account_pay.text}].Account_ID,
                AccountRecipient_ID = if (tv_record_account_receive.text !="") recordViewModel.account[recordViewModel.account.indexOfFirst{it.Account_Name == tv_record_account_receive.text}].Account_ID else 1L,
                Transaction_Amount = tv_record_amount.text.toString().toDouble(),
                Transaction_Date = Date(strDate),
                Transaction_Memo = tv_record_memo.text.toString(),
                Merchant_ID = recordViewModel.merchant[recordViewModel.merchant.indexOfFirst { it.Merchant_Name == tv_record_merchant.text.toString() }].Merchant_ID,
                Person_ID = recordViewModel.person[recordViewModel.person.indexOfFirst { it.Person_Name == tv_record_person.text.toString() }].Person_ID,
                Project_ID = 1L, //recordViewModel.transDetail.Period_ID,
                Transaction_ReimburseStatus = recordViewModel.transDetail.Transaction_ReimburseStatus
            )
            // save
            if (transactionID > 0) {
                AppDatabase.getDatabase(requireContext()).trans().updateTransaction(trans)
            } else {
                AppDatabase.getDatabase(requireContext()).trans().addTransaction(trans)
            }

            Toast.makeText(context,getText(R.string.msg_saved),Toast.LENGTH_SHORT).show()
            //Snackbar.make(requireView(), getText(R.string.msg_saved), Snackbar.LENGTH_SHORT).show()
            return 0
        }
    }

    // delete record
    private fun deleteRecord(activity: FragmentActivity?, transactionID: Long) {

        val dialogBuilder = AlertDialog.Builder(activity)

        dialogBuilder.setMessage(getText(R.string.msg_content_delete))
            .setCancelable(true)
            .setPositiveButton(getText(R.string.msg_button_confirm),DialogInterface.OnClickListener{ _,_->
                // delete record
                val trans = Trans(Transaction_ID = transactionID)
                AppDatabase.getDatabase(requireContext()).trans().deleteTransaction(trans)
                // exit
                requireActivity().onBackPressed()
            })
            .setNegativeButton(getText(R.string.msg_button_cancel),DialogInterface.OnClickListener{ dialog, _ ->
                // cancel
                dialog.cancel()
            })

        // set Title Style
        val titleView = layoutInflater.inflate(R.layout.popup_title,null)
        // set Title Text
        titleView.tv_popup_title_text.text = getText(R.string.msg_Title_prompt)

        val alert = dialogBuilder.create()
        //alert.setIcon(R.drawable.ic_baseline_delete_forever_24)
        alert.setCustomTitle(titleView)
        alert.show()
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


    private fun setReimburseIcon(transactionReimbursestatus: Int) {
        when(transactionReimbursestatus){
            0 -> {
                tv_record_reimburse.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_check_box_outline_blank_24,0,0,0)
            }
            1 -> {
                tv_record_reimburse.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_indeterminate_check_box_24,0,0,0)
            }
            2 -> {
                tv_record_reimburse.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_check_box_24,0,0,0)
            }
        }
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

        when (ctt.currentTyID) {
            1,2 -> {
                iv_record_swap.visibility = View.INVISIBLE
                tv_record_account_receive.visibility = View.INVISIBLE
                tv_record_common_category.visibility = View.VISIBLE
                tv_record_all_category.visibility = View.VISIBLE
            }
            3,4 -> {
                iv_record_swap.visibility = View.VISIBLE
                tv_record_account_receive.visibility = View.VISIBLE
                tv_record_common_category.visibility = View.GONE
                tv_record_all_category.visibility = View.GONE
            }
        }

        tv_record_category.text = recordViewModel.getSubCategoryName()

    }


}



