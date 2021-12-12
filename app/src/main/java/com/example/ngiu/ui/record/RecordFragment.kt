package com.example.ngiu.ui.record


import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.core.view.forEach
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.*
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
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
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.*


class RecordFragment : Fragment() {


    private lateinit var recordViewModel: RecordViewModel
    private var _binding: FragmentRecordBinding? = null
    private var vpAdapter: RecordCategoryAdapter? = null
    private var receivedID: Long = 0
    private var receivedString: String = ""

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // hide bottom bar
        (activity as MainActivity).setNavBottomBarVisibility(View.GONE)

        // Pass value from other fragment
        // --implementation "androidx.fragment:fragment-ktx:1.3.6"
        setFragmentResultListener("record_edit_mode") { _, bundle ->
            receivedID = bundle.getLong("rID")


            if (receivedID > 0) {
                // show delete menu
                toolbar_record.menu.findItem(R.id.action_delete).isVisible = true
                // show delete button
                tv_record_left_button.text = getText(R.string.menu_delete)

                recordViewModel.loadTransactionDetail(activity, receivedID)
            }
        }

        // get string from category manage
        setFragmentResultListener("category_manage") { _, bundle ->
            receivedString = bundle.getString("subCategory_Name").toString()
            recordViewModel.transDetail.SubCategory_Name = receivedString
        }


    }

    // Fragments use a layout inflater to create their view at this stage.
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
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
                            override fun onItemClick(subCategoryName: String) {
                                // do something after clicked
                                tv_record_category.text = subCategoryName
                                recordViewModel.setSubCategoryName(subCategoryName)
                                showAccountName(subCategoryName)
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
            setStatus(recordViewModel.setTransactionType(1L))
            loadCommonCategory(1L)
        }
        // touch Income textView, switch to Income page
        tvSectionIncome.setOnClickListener {
            setStatus(recordViewModel.setTransactionType(2L))
            loadCommonCategory(2L)
        }
        // touch Transfer textView, switch to Transfer page
        tvSectionTransfer.setOnClickListener {
            setStatus(recordViewModel.setTransactionType(3L))
            loadCommonCategory(3L)
        }
        // touch DebitCredit textView, switch to DebitCredit page
        tvSectionDebitCredit.setOnClickListener {
            setStatus(recordViewModel.setTransactionType(4L))
            loadCommonCategory( 4L)
        }



        // set up toolbar icon and click event

        // choose items to show
        toolbar_record.menu.findItem(R.id.action_done).isVisible = true



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
                if (saveRecord() == 0) tv_record_amount.text = "0.00"
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
                tv_record_date.text =LocalDate.of(year,month+1, day).format(DateTimeFormatter.ofPattern("MM/dd/yyyy"))

                recordViewModel.transDetail.Transaction_Date = getInternationalDateFromAmericanDate(tv_record_date.text.toString() + " " + tv_record_time.text.toString())
            })

        }

        // time
        tv_record_time.setOnClickListener {
            // time picker
            val time = LocalTime.parse(tv_record_time.text.toString(), DateTimeFormatter.ofPattern("HH:mm:ss"))

            DateTimePicker(
                startHour = time.hour,
                startMinute = time.minute
            ).pickTime(context, TimePickerDialog.OnTimeSetListener { _, hour, minute ->
                val s = Calendar.getInstance().get(Calendar.SECOND)
                tv_record_time.text  = LocalTime.of(hour,minute,s).toString()
                recordViewModel.transDetail.Transaction_Date = getInternationalDateFromAmericanDate(tv_record_date.text.toString() + " " + tv_record_time.text.toString())
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
        tv_record_person.doAfterTextChanged{
            recordViewModel.transDetail.Person_Name = tv_record_person.text.toString()
        }

        // Merchant
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
        tv_record_merchant.doAfterTextChanged{
            recordViewModel.transDetail.Merchant_Name = tv_record_merchant.text.toString()
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
        tv_record_project.doAfterTextChanged{
            recordViewModel.transDetail.Project_Name = tv_record_project.text.toString()
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
            when (recordViewModel.transDetail.TransactionType_ID){
                1L,2L -> openCategoryManager()
            }
        }


        // category
        tv_record_category.setOnClickListener {
            when (recordViewModel.transDetail.TransactionType_ID){
                1L,2L -> openCategoryManager()
            }
        }
        tv_record_category.doAfterTextChanged{
            recordViewModel.transDetail.SubCategory_Name = tv_record_category.text.toString()
        }


        // amount
        tv_record_amount.setOnClickListener {
            Keyboard(view).initKeys(tv_record_amount)
            Keyboard(view).show()
        }
        tv_record_amount.doAfterTextChanged{
            recordViewModel.transDetail.Transaction_Amount = tv_record_amount.text.toString().toDouble()
        }


        // swap
        iv_record_swap.setOnClickListener {
            tv_record_account_receive.text = tv_record_account_pay.text.apply { tv_record_account_pay.text = tv_record_account_receive.text }
            recordViewModel.tempSavedAccountName[0] = recordViewModel.tempSavedAccountName[1].apply { recordViewModel.tempSavedAccountName[1] = recordViewModel.tempSavedAccountName[0] }
        }


        // account pay
        tv_record_account_pay.setOnClickListener {
            if (tv_record_account_pay.text.toString() != "No Account") {
                // load account name as list and show it in a popup window
                val nameList: Array<String> = recordViewModel.getListOfAccountName(tv_record_account_receive.text.toString(),true)
                popupWindow(requireContext(),getText(R.string.setting_merchant).toString(),  nameList,
                    object : SelectItem {
                        override fun clicked(idx: Int) {
                            tv_record_account_pay.text = nameList[idx]
                            recordViewModel.setAccountName(true,tv_record_account_pay.text.toString())
                        }
                    })
            }else{
                // create new account if no account
                createNewAccount(recordViewModel.transDetail.TransactionType_ID, recordViewModel.transDetail.SubCategory_Name,true)
            }
        }
        tv_record_account_pay.doAfterTextChanged{
            recordViewModel.transDetail.Account_Name = tv_record_account_pay.text.toString()
        }


        // account receive
        tv_record_account_receive.setOnClickListener {

            if (tv_record_account_receive.text.toString() != "No Account") {
                // load account name as list and show it in a popup window
                val nameList: Array<String> = recordViewModel.getListOfAccountName(tv_record_account_pay.text.toString(),false)
                popupWindow(requireContext(),getText(R.string.setting_merchant).toString(),  nameList,
                    object : SelectItem {
                        override fun clicked(idx: Int) {
                            tv_record_account_receive.text = nameList[idx]
                            recordViewModel.setAccountName(false,tv_record_account_receive.text.toString())
                        }
                    })
            }else{
                // create new account if no account
                createNewAccount(recordViewModel.transDetail.TransactionType_ID, recordViewModel.transDetail.SubCategory_Name, false)
            }

        }
        tv_record_account_receive.doAfterTextChanged{
            recordViewModel.transDetail.AccountRecipient_Name = tv_record_account_receive.text.toString()
        }


        // memo
        tv_record_memo.doAfterTextChanged {
            recordViewModel.transDetail.Transaction_Memo = tv_record_memo.text.toString()
        }
    }

    private fun getInternationalDateFromAmericanDate(string: String): LocalDateTime {
        val lDateTime: LocalDateTime =  LocalDateTime.parse(string, DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm:ss"))
        return LocalDateTime.parse(lDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")).toString(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
    }


    // called when the fragment is visible and actively running.
    override fun onResume() {
        super.onResume()

        // load data to UI textview
        loadUIData(receivedID)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        //(activity as MainActivity).setNavBottomBarVisibility(View.VISIBLE)
        _binding = null
        vpAdapter = null
    }




    //------------------------------------------Private Functions--------------------------------------------------
    //------------------------------------------Private Functions--------------------------------------------------


    private fun showAccountName(subCategoryName: String) {
        if (recordViewModel.transDetail.TransactionType_ID == 4L){
            when (recordViewModel.getSubCategoryID(subCategoryName)) {
                // borrow in | received
                7L, 10L -> {
                    tv_record_account_pay.text =  recordViewModel.tempSavedAccountName[2]
                    tv_record_account_receive.text = recordViewModel.tempSavedAccountName[3]
                }
                // lend out | repayment
                8L, 9L -> {
                    tv_record_account_pay.text =  recordViewModel.tempSavedAccountName[3]
                    tv_record_account_receive.text = recordViewModel.tempSavedAccountName[2]
                }
            }

        }else{
            tv_record_account_pay.text =  recordViewModel.tempSavedAccountName[0]
            tv_record_account_receive.text = recordViewModel.tempSavedAccountName[1]
        }
    }


    private fun createNewAccount(transactionTypeId: Long, subcategoryName: String, payable: Boolean) {

        when (recordViewModel.getSubCategoryID(subcategoryName)) {
            // borrow in | received
            7L, 10L -> {
                if (payable){
                    // create P/R account
                    val bundle = Bundle().apply {
                        putString("page", "add_payable")
                    }
                    findNavController().navigate(R.id.addCashFragment, bundle)

                }else{
                    findNavController().navigate(R.id.navigation_add_account)
                }
            }
            // lend out | repayment
            8L, 9L -> {
                if (!payable) {
                    // create P/R account
                    // todo create P/R account
                    val bundle = Bundle().apply {
                        putString("page", "add_payable")
                    }
                    findNavController().navigate(R.id.addCashFragment, bundle)

                }else{
                    findNavController().navigate(R.id.navigation_add_account)
                }
            }
            else ->{
                findNavController().navigate(R.id.navigation_add_account)
            }
        }

    }

    @SuppressLint("SetTextI18n")
    private fun loadUIData(transactionID: Long){
        recordViewModel.setTransactionType(recordViewModel.transDetail.TransactionType_ID)

        if (transactionID > 0 || receivedString.isNotEmpty()) {
            // edit record
            //load data to textview
            recordViewModel.setSubCategoryName(recordViewModel.transDetail.SubCategory_Name)

        }else{
            // new record
                /*
            if (recordViewModel.transDetail.Account_Name.isEmpty()) {
                if (recordViewModel.account.isNotEmpty()){
                    recordViewModel.transDetail.Account_Name = recordViewModel.account[0].Account_Name
                }else{
                    recordViewModel.transDetail.Account_Name =  "No Account"
                }
            }
            if (recordViewModel.transDetail.AccountRecipient_Name.isEmpty()) {
                if (recordViewModel.account.size > 1){
                    recordViewModel.transDetail.AccountRecipient_Name = recordViewModel.account[1].Account_Name
                }else{
                    recordViewModel.transDetail.AccountRecipient_Name =  "No Account"
                }
            }

                 */
            if (recordViewModel.transDetail.Person_Name.isEmpty()) {
                recordViewModel.transDetail.Person_Name = recordViewModel.person[0].Person_Name
            }
            if (recordViewModel.transDetail.Merchant_Name.isEmpty()) {
                recordViewModel.transDetail.Merchant_Name = recordViewModel.merchant[0].Merchant_Name
            }
            if (recordViewModel.transDetail.Project_Name.isEmpty()) {
                recordViewModel.transDetail.Project_Name = recordViewModel.project[0].Project_Name
            }
        }


        tv_record_date.text = recordViewModel.transDetail.Transaction_Date.format(DateTimeFormatter.ofPattern("MM/dd/yyyy"))
        tv_record_time.text = recordViewModel.transDetail.Transaction_Date.format(DateTimeFormatter.ofPattern("HH:mm:ss"))
        tv_record_amount.text = "%.2f".format(recordViewModel.transDetail.Transaction_Amount)
        tv_record_account_pay.text = recordViewModel.transDetail.Account_Name
        tv_record_account_receive.text = recordViewModel.transDetail.AccountRecipient_Name
        tv_record_memo.setText(recordViewModel.transDetail.Transaction_Memo)
        tv_record_person.text = recordViewModel.transDetail.Person_Name
        tv_record_merchant.text = recordViewModel.transDetail.Merchant_Name
        tv_record_project.text = recordViewModel.transDetail.Project_Name

        setReimburseIcon(recordViewModel.transDetail.Transaction_ReimburseStatus)
        tv_record_reimburse.text = recordViewModel.getReimbursable(requireContext(), recordViewModel.transDetail.Transaction_ReimburseStatus)
        //todo period,

        setStatus( recordViewModel.currentTransactionType )
        loadCommonCategory( recordViewModel.transDetail.TransactionType_ID,recordViewModel.transDetail.SubCategory_Name )

    }

    // save record
    private fun saveRecord(transactionID: Long = 0) : Int{

        // If amount is 0, do not save
        if (tv_record_amount.text.toString().toDouble() == 0.0) {
            Toast.makeText(
                context,
                getText(R.string.msg_cannot_save_with_zero_amount),
                Toast.LENGTH_SHORT
            ).show()
            //Snackbar.make(requireView(), getText(R.string.msg_cannot_save_with_zero_amount), Snackbar.LENGTH_SHORT).show()
            return 1
        }else {

            val trans = Trans(
                Transaction_ID = transactionID,
                TransactionType_ID = recordViewModel.transDetail.TransactionType_ID,
                SubCategory_ID = recordViewModel.getSubCategoryID(tv_record_category.text.toString()),
                Account_ID = recordViewModel.getAccountID(tv_record_account_pay.text.toString()),
                Transaction_Amount = tv_record_amount.text.toString().toDouble(),
                Transaction_Date = recordViewModel.transDetail.Transaction_Date,
                Transaction_Memo = tv_record_memo.text.toString(),
                Merchant_ID = recordViewModel.merchant[recordViewModel.merchant.indexOfFirst { it.Merchant_Name == tv_record_merchant.text.toString() }].Merchant_ID,
                Person_ID = recordViewModel.person[recordViewModel.person.indexOfFirst { it.Person_Name == tv_record_person.text.toString() }].Person_ID,
                Project_ID = 1L, //recordViewModel.transDetail.Period_ID,
                Transaction_ReimburseStatus = recordViewModel.transDetail.Transaction_ReimburseStatus
            )

            trans.AccountRecipient_ID = if (recordViewModel.transDetail.TransactionType_ID < 3L) trans.Account_ID
                                        else recordViewModel.getAccountID(tv_record_account_receive.text.toString())

            if (trans.Account_ID < 1L || trans.AccountRecipient_ID < 1L) {
                Toast.makeText(
                    context,
                    getText(R.string.msg_cannot_save_with_no_account),
                    Toast.LENGTH_SHORT
                ).show()
                return 1
            }


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

        dialogBuilder.setMessage(getText(R.string.msg_content_transaction_delete))
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
    private fun loadCommonCategory( tyID: Long, categoryString: String = "") {
        Thread {
            activity?.runOnUiThread {

                val cmCategory = when (tyID){
                    1L -> recordViewModel.expenseCommonCategory
                    2L -> recordViewModel.incomeCommonCategory
                    3L -> recordViewModel.transferCommonCategory
                    4L -> recordViewModel.debitCreditCommonCategory
                    else -> emptyList()
                }

                // vpAdapter?.setCategoryString(categoryString)
                vpAdapter?.setList(cmCategory)
                //vpAdapter?.setCategoryString(categoryString)

                vp_record_category.adapter= vpAdapter

            }
        }.start()
    }


    private fun setReimburseIcon(transactionReimburseStatus: Int) {
        when(transactionReimburseStatus){
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

    @SuppressLint("UseCompatLoadingForDrawables")
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
            when (recordViewModel.transDetail.TransactionType_ID){
                1L -> ContextCompat.getColor( requireContext(), R.color.app_expense_amount)
                2L -> ContextCompat.getColor( requireContext(), R.color.app_income_amount)
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
        // Subcategory
        tv_record_category.text = recordViewModel.getSubCategoryName()



        when (recordViewModel.transDetail.TransactionType_ID) {
            1L,2L -> {
                iv_record_swap.visibility = View.INVISIBLE
                tv_record_account_receive.visibility = View.INVISIBLE
                tv_record_common_category.visibility = View.VISIBLE
                tv_record_all_category.visibility = View.VISIBLE
                // acount name
                tv_record_account_pay.text = recordViewModel.getAccountName(true)
            }
            3L -> {
                iv_record_swap.visibility = View.VISIBLE
                iv_record_swap.isClickable = true
                iv_record_swap.setImageDrawable(requireContext().getDrawable(R.drawable.ic_baseline_swap_horiz_24))
                tv_record_account_receive.visibility = View.VISIBLE
                tv_record_common_category.visibility = View.GONE
                tv_record_all_category.visibility = View.GONE
                // acount name
                tv_record_account_pay.text = recordViewModel.getAccountName(true)
                tv_record_account_receive.text = recordViewModel.getAccountName(false)
                if (tv_record_account_pay.text.toString() == tv_record_account_receive.text.toString()){
                    val ltName = recordViewModel.getListOfAccountName(tv_record_account_pay.text.toString(), false)
                    if (ltName.isNotEmpty())  tv_record_account_receive.text = ltName[0]
                    else tv_record_account_receive.text = "No Account"
                }
            }
            4L -> {
                iv_record_swap.visibility = View.VISIBLE
                iv_record_swap.isClickable = false
                iv_record_swap.setImageDrawable(requireContext().getDrawable(R.drawable.ic_baseline_keyboard_arrow_right_24))
                tv_record_account_receive.visibility = View.VISIBLE
                tv_record_common_category.visibility = View.GONE
                tv_record_all_category.visibility = View.GONE
                // acount name
                showAccountName(tv_record_category.text.toString())
            }

        }




    }

    private fun openCategoryManager() {
        // hide nav bottom bar
        //(activity as MainActivity).setNavBottomBarVisibility(View.GONE)

        setFragmentResult("category_manage_mode", bundleOf("edit_mode" to false))
        setFragmentResult("category_manage_type", bundleOf("transaction_type" to recordViewModel.transDetail.TransactionType_ID))

        // switch to category manage fragment
        findNavController().navigate(R.id.navigation_category_manage)
    }

    private fun openAddAccountFragment(tID: Long) {

        //setFragmentResult("category_manage_mode", bundleOf("edit_mode" to false))
        //setFragmentResult("category_manage_type", bundleOf("transaction_type" to recordViewModel.transDetail.TransactionType_ID))

        // switch to category manage fragment
        findNavController().navigate(R.id.navigation_add_account)
    }
}



