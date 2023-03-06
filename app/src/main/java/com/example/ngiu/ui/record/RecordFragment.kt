package com.example.ngiu.ui.record


import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.res.Resources
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.*
import android.widget.EditText
import android.widget.ListPopupWindow.MATCH_PARENT
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.core.view.forEach
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.*
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import com.example.ngiu.MainActivity
import com.example.ngiu.R
import com.example.ngiu.data.entities.Category
import com.example.ngiu.data.entities.Event
import com.example.ngiu.data.entities.Trans
import com.example.ngiu.databinding.FragmentRecordBinding
import com.example.ngiu.functions.*
import kotlinx.android.synthetic.main.fragment_record.*
import kotlin.collections.ArrayList
import com.example.ngiu.ui.keyboard.Keyboard
import kotlinx.android.synthetic.main.fragment_account_list.*
import kotlinx.android.synthetic.main.popup_title.view.*
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.concurrent.fixedRateTimer


class RecordFragment : Fragment() {


    private lateinit var recordViewModel: RecordViewModel
    private var _binding: FragmentRecordBinding? = null
    private var vpAdapter: RecordCategoryAdapter? = null
    private var receivedTransID: Long = 0
    private var receivedAccountID: Long = 0
    private var receivedTransTypeID: Long = TRANSACTION_TYPE_EXPENSE
    private var receivedString: String = ""

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // hide bottom bar
        (activity as MainActivity).setNavBottomBarVisibility(View.GONE)


        //*** CHECK IF ANY DATA WAS PASSED FROM OTHER FRAGMENTS

            // receive data from other fragment
/*            receivedTransID = arguments?.getLong(KEY_RECORD_TRANSACTION_ID)!!
            receivedAccountID = arguments?.getLong(KEY_RECORD_ACCOUNT_ID)!!
            receivedTransTypeID = arguments?.getLong(KEY_RECORD_TRANSACTION_TYPE_ID)!!*/
            setFragmentResultListener(KEY_RECORD){ _, bundle ->
                receivedTransID = bundle.getLong(KEY_RECORD_TRANSACTION_ID)
                receivedAccountID = bundle.getLong(KEY_RECORD_ACCOUNT_ID)
                receivedTransTypeID = bundle.getLong(KEY_RECORD_TRANSACTION_TYPE_ID)
                Toast.makeText(context, receivedTransID.toString(), Toast.LENGTH_LONG)
            }

            // get string from category manage
            setFragmentResultListener(KEY_RECORD_CATEGORY) { _, bundle ->
                receivedString = bundle.getString(KEY_RECORD_CATEGORY_NAME).toString()
                recordViewModel.transDetail.Category_Name = receivedString
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
        recordViewModel.loadDataToRam(requireContext())
        if (receivedTransTypeID > 0) {
            recordViewModel.transDetail.TransactionType_ID = receivedTransTypeID
            recordViewModel.setTransactionTypeTextViewColor(receivedTransTypeID)
        }
        // todo category未选中（Transfer and Debit & Credit），账号不固定，
        // 初始化Cate

        Thread {
            // load Data to Ram
            //recordViewModel.loadDataToRam(activity)

            activity?.runOnUiThread {
                // pass the value to fragment from adapter when item clicked
                vpAdapter =
                    this.context?.let {
                        RecordCategoryAdapter(object : RecordCategoryAdapter.OnClickListener {

                            // catch the item click event from adapter
                            override fun onItemClick(categoryName: String) {
                                // do something after clicked
                                tv_record_category.text = categoryName
                                recordViewModel.setSubCategoryName(categoryName)
                                showAccountName(categoryName)
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

        if (receivedTransID > 0L ) {
            // show delete menu
            toolbar_record.menu.findItem(R.id.action_delete).isVisible = true
            // show delete button
            tv_record_left_button.text = getText(R.string.menu_delete)

            recordViewModel.loadTransactionDetail(view.context, receivedTransID)
        }


        // -------------------------- TOOLBAR -------------------------------------
        // choose items to show
        toolbar_record.menu.findItem(R.id.action_done).isVisible = true

        // click the navigation Icon in the left side of toolbar
        toolbar_record.setNavigationOnClickListener{
            // call back button event to switch to previous fragment
            //requireActivity().onBackPressed()
            NavHostFragment.findNavController(this).navigateUp()
        }

        // menu item clicked
        toolbar_record.setOnMenuItemClickListener{
            when (it.itemId) {
                // done menu
                R.id.action_done -> {
                    // save record
                    if (saveRecord(receivedTransID) == 0) {
                        // call back button event to switch to previous fragment
                        //requireActivity().onBackPressed()
                        NavHostFragment.findNavController(this).navigateUp()
                    }
                    true
                }
                // delete menu
                R.id.action_delete -> {
                    // delete record
                    deleteRecord(activity, receivedTransID)
                    true
                }

                else -> true
            }
        }
        // ---------------------------- TOOLBAR ---------------------------------------


        // touch Expense textView, switch to Expense page
        tvSectionExpense.setOnClickListener {
            setStatus(recordViewModel.setTransactionType(TRANSACTION_TYPE_EXPENSE))
            loadCommonCategory(TRANSACTION_TYPE_EXPENSE)
        }
        // touch Income textView, switch to Income page
        tvSectionIncome.setOnClickListener {
            setStatus(recordViewModel.setTransactionType(TRANSACTION_TYPE_INCOME))
            loadCommonCategory(TRANSACTION_TYPE_INCOME)
        }
        // touch Transfer textView, switch to Transfer page
        tvSectionTransfer.setOnClickListener {
            setStatus(recordViewModel.setTransactionType(TRANSACTION_TYPE_TRANSFER))
            loadCommonCategory(TRANSACTION_TYPE_TRANSFER)
        }
        // touch DebitCredit textView, switch to DebitCredit page
        tvSectionDebitCredit.setOnClickListener {
            setStatus(recordViewModel.setTransactionType(TRANSACTION_TYPE_DEBIT))
            loadCommonCategory(TRANSACTION_TYPE_DEBIT)
        }


        // --------------- TOUCH EVENTS ---------------------------------------------------------
        // Save Button
        tv_record_right_button.setOnClickListener {
            if (saveRecord(receivedTransID) == 0) {
                // exit
                //requireActivity().onBackPressed()
                NavHostFragment.findNavController(this).navigateUp()
            }
        }

        // Save and Next Button | Delete Button
        tv_record_left_button.setOnClickListener {
            if (receivedTransID > 0){
                // delete
                deleteRecord(activity, receivedTransID)
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
            ).pickDate(view.context) { _, year, month, day ->
                tv_record_date.text = LocalDate.of(year, month + 1, day)
                    .format(DateTimeFormatter.ofPattern("MM/dd/yyyy"))

                recordViewModel.transDetail.Transaction_Date =
                    getInternationalDateFromAmericanDate(tv_record_date.text.toString() + " " + tv_record_time.text.toString())
            }

        }

        // time
        tv_record_time.setOnClickListener {
            // time picker
            val time = LocalTime.parse(tv_record_time.text.toString(), DateTimeFormatter.ofPattern("HH:mm:ss"))

            DateTimePicker(
                startHour = time.hour,
                startMinute = time.minute
            ).pickTime(context) { _, hour, minute ->
                val s = Calendar.getInstance().get(Calendar.SECOND)
                tv_record_time.text = LocalTime.of(hour, minute, s).toString()
                recordViewModel.transDetail.Transaction_Date =
                   getInternationalDateFromAmericanDate(tv_record_date.text.toString() + " " + tv_record_time.text.toString())
            }
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
            val tList: MutableList<String> = ArrayList()
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
            val tList: MutableList<String> = ArrayList()
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
            val tList: MutableList<String> = ArrayList()
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
        // other info -- under Memo section
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
                TRANSACTION_TYPE_EXPENSE, TRANSACTION_TYPE_INCOME ->
                    switchToCategoryManager(view, this, SELECT_MODE, recordViewModel.transDetail.TransactionType_ID)
                    //openCategoryManager(recordViewModel.transDetail.TransactionType_ID)
            }
        }


        // category
        tv_record_category.setOnClickListener {
            when (recordViewModel.transDetail.TransactionType_ID){
                TRANSACTION_TYPE_EXPENSE, TRANSACTION_TYPE_INCOME ->
                    switchToCategoryManager(view, this, SELECT_MODE,recordViewModel.transDetail.TransactionType_ID)
                //openCategoryManager(recordViewModel.transDetail.TransactionType_ID)
            }
        }
        tv_record_category.doAfterTextChanged{
            recordViewModel.transDetail.Category_Name = tv_record_category.text.toString()
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

            //showAccountListDialog(view.context, 0L)

            if (tv_record_account_pay.text.toString() != getString(R.string.msg_no_account)) {
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
                createNewAccount(view, recordViewModel.transDetail.Category_Name, true)
            }
        }
        tv_record_account_pay.doAfterTextChanged{
            recordViewModel.transDetail.Account_Name = tv_record_account_pay.text.toString()
        }


        // account receive
        tv_record_account_receive.setOnClickListener {

            if (tv_record_account_receive.text.toString() != getString(R.string.msg_no_account)) {
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
                createNewAccount(view, recordViewModel.transDetail.Category_Name, false)
            }

        }
        tv_record_account_receive.doAfterTextChanged{
            recordViewModel.transDetail.AccountRecipient_Name = tv_record_account_receive.text.toString()
        }


        // memo
        tv_record_memo.doAfterTextChanged {
            recordViewModel.transDetail.Transaction_Memo = tv_record_memo.text.toString()
        }
        // --------------- TOUCH EVENTS -----------------------------------------------------------------------------
    }




    // called when the fragment is visible and actively running.
    override fun onResume() {
        super.onResume()

        // load data to UI textview
        loadUIData(receivedTransID)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        //(activity as MainActivity).setNavBottomBarVisibility(View.VISIBLE)
        _binding = null
        vpAdapter = null
    }




    //------------------------------------------ Private Functions --------------------------------------------------
    //------------------------------------------  Private Functions --------------------------------------------------

    private fun selectTransactionType(transType: Long, transID: Long, acctID: Long, cateID: Long){
        //*** prepare the data after selected transaction type


        // show data
        showData(transType)

    }

    private fun showData(transType: Long){
        //*** show data on UI
        // todo show data

        // category

        // budget

        // account

        // data time

        // reimbursable

        // person

        // location

        // project

        // button
    }

    private fun selectCategory(){
        //*** prepare the data after selected Category



    }



    private fun showAccountName(categoryName: String, blnNew: Boolean = true) {
        if (recordViewModel.transDetail.TransactionType_ID == TRANSACTION_TYPE_DEBIT){
            when (recordViewModel.getCategoryID(categoryName)) {
                // borrow in | received
                CATEGORY_SUB_BORROW, CATEGORY_SUB_RECEIVE_PAYMENT -> {
                    tv_record_account_pay.text =  recordViewModel.tempSavedAccountName[2]
                    tv_record_account_receive.text = recordViewModel.tempSavedAccountName[3]
                }
                // lend out | repayment
                CATEGORY_SUB_LEND, CATEGORY_SUB_PAYMENT -> {
                    tv_record_account_pay.text =  recordViewModel.tempSavedAccountName[3]
                    tv_record_account_receive.text = recordViewModel.tempSavedAccountName[2]
                }
            }

        }else{
            tv_record_account_pay.text =  recordViewModel.tempSavedAccountName[0]
            tv_record_account_receive.text = recordViewModel.tempSavedAccountName[1]
        }
    }


    private fun createNewAccount(view: View, categoryName: String, payable: Boolean) {

        when (recordViewModel.getCategoryID(categoryName)) {
            // borrow in | received
            CATEGORY_SUB_BORROW, CATEGORY_SUB_RECEIVE_PAYMENT -> {
                if (payable){
                    // create P/R account
                    switchToAccountAttributePage(view, ACCOUNT_TYPE_RECEIVABLE
                                            ,0,0.0,
                                            NEW_MODE)
                }else{
                    findNavController().navigate(R.id.navigation_add_account)
                }

            }
            // lend out | repayment
            CATEGORY_SUB_LEND, CATEGORY_SUB_PAYMENT -> {
                if (!payable) {
                    // create P/R account
                    switchToAccountAttributePage(view, ACCOUNT_TYPE_RECEIVABLE
                        ,0,0.0,
                        NEW_MODE)

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
        recordViewModel.setSubCategoryName(recordViewModel.transDetail.Category_Name)

        if (transactionID > 0 || receivedString.isNotEmpty()) {
            // edit record
            //load data to textview
            recordViewModel.setTransactionType(recordViewModel.transDetail.TransactionType_ID)



        }else{
            // new record
//            if (receivedTransTypeID > 0) {
//                recordViewModel.setTransactionType(receivedTransTypeID)
//            }

            // todo when add a record from account detail frame
            // set category as "Borrow" when open a new record from RP account
            if (receivedTransTypeID == TRANSACTION_TYPE_DEBIT) {
                //val tempCate = recordViewModel.category[recordViewModel.category.indexOfFirst { it.Category_ID == CATEGORY_SUB_BORROW }].Category_Name
                //tv_record_category.text = categoryName
                recordViewModel.setSubCategoryName(recordViewModel.debitCreditCommonCategory[0].Category_Name)
                showAccountName(recordViewModel.debitCreditCommonCategory[0].Category_Name)
                recordViewModel.setAccountName(true, recordViewModel.account[recordViewModel.account.indexOfFirst { it.Account_ID == receivedAccountID }].Account_Name)
                //recordViewModel.transDetail.Category_Name = recordViewModel.debitCreditCommonCategory[0].Category_Name
                //todo bug: account name was changed after click category.

                //bug: open category manager then go back will lock on debit transition type.(fixed)
                setStatus(recordViewModel.setTransactionType(TRANSACTION_TYPE_DEBIT))
                //loadCommonCategory(TRANSACTION_TYPE_DEBIT)
                receivedTransTypeID=0
            }
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
        tv_record_account_pay.text = recordViewModel.getPayOutAccountName(requireContext(), 0L, 1L, receivedAccountID)
        tv_record_account_receive.text = recordViewModel.transDetail.AccountRecipient_Name
        tv_record_memo.setText(recordViewModel.transDetail.Transaction_Memo)
        tv_record_person.text = recordViewModel.transDetail.Person_Name
        tv_record_merchant.text = recordViewModel.transDetail.Merchant_Name
        tv_record_project.text = recordViewModel.transDetail.Project_Name

        setReimburseIcon(recordViewModel.transDetail.Transaction_ReimburseStatus)
        tv_record_reimburse.text = recordViewModel.getReimbursable(requireContext(), recordViewModel.transDetail.Transaction_ReimburseStatus)
        //todo period,

        setStatus( recordViewModel.currentTransactionType )
        loadCommonCategory(recordViewModel.transDetail.TransactionType_ID)

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
            //Snack.make(requireView(), getText(R.string.msg_cannot_save_with_zero_amount), Snack.LENGTH_SHORT).show()
            return 1
        }else {

            val trans = Trans(
                Transaction_ID = transactionID,
                TransactionType_ID = recordViewModel.transDetail.TransactionType_ID,
                Category_ID = recordViewModel.getCategoryID(tv_record_category.text.toString()),
                Account_ID = recordViewModel.getAccountID(tv_record_account_pay.text.toString()),
                Transaction_Amount = tv_record_amount.text.toString().toDouble(),
                Transaction_Date = recordViewModel.transDetail.Transaction_Date,
                Transaction_Memo = tv_record_memo.text.toString(),
                Merchant_ID = recordViewModel.merchant[recordViewModel.merchant.indexOfFirst { it.Merchant_Name == tv_record_merchant.text.toString() }].Merchant_ID,
                Person_ID = recordViewModel.person[recordViewModel.person.indexOfFirst { it.Person_Name == tv_record_person.text.toString() }].Person_ID,
                Project_ID = 1L, //recordViewModel.transDetail.Period_ID,
                Transaction_ReimburseStatus = recordViewModel.transDetail.Transaction_ReimburseStatus
            )

            trans.AccountRecipient_ID = if (recordViewModel.transDetail.TransactionType_ID < TRANSACTION_TYPE_TRANSFER) trans.Account_ID
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
                recordViewModel.updateTransaction(requireContext(), trans)
            } else {
                recordViewModel.addTransaction(requireContext(),trans)
            }

            Toast.makeText(context,getText(R.string.msg_saved),Toast.LENGTH_SHORT).show()
            return 0
        }
    }

    // delete record
    @SuppressLint("InflateParams")
    private fun deleteRecord(activity: FragmentActivity?, transactionID: Long) {

        val dialogBuilder = AlertDialog.Builder(activity)

        dialogBuilder.setMessage(getText(R.string.msg_content_transaction_delete))
            .setCancelable(true)
            .setPositiveButton(getText(R.string.msg_button_confirm)) { _, _ ->

                // delete record
                val trans = Trans(Transaction_ID = transactionID)
                recordViewModel.deleteTrans(requireContext(),trans)
                // exit
                //requireActivity().onBackPressed()
                NavHostFragment.findNavController(this).navigateUp()

            }
            .setNegativeButton(getText(R.string.msg_button_cancel)) { dialog, _ ->
                // cancel
                dialog.cancel()
            }

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
    private fun loadCommonCategory(tyID: Long) {
        Thread {
            activity?.runOnUiThread {

                val cmCategory = when (tyID){
                    TRANSACTION_TYPE_EXPENSE -> recordViewModel.expenseCommonCategory
                    TRANSACTION_TYPE_INCOME -> recordViewModel.incomeCommonCategory
                    TRANSACTION_TYPE_TRANSFER -> recordViewModel.transferCommonCategory
                    TRANSACTION_TYPE_DEBIT -> recordViewModel.debitCreditCommonCategory
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
            NON_REIMBURSABLE -> {
                tv_record_reimburse.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_check_box_outline_blank_24,0,0,0)
            }
            REIMBURSABLE -> {
                tv_record_reimburse.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_indeterminate_check_box_24,0,0,0)
            }
            REIMBURSED -> {
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
                TRANSACTION_TYPE_EXPENSE -> ContextCompat.getColor( requireContext(), R.color.app_expense_amount)
                TRANSACTION_TYPE_INCOME -> ContextCompat.getColor( requireContext(), R.color.app_income_amount)
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
            TRANSACTION_TYPE_EXPENSE, TRANSACTION_TYPE_INCOME -> {
                iv_record_swap.visibility = View.INVISIBLE
                tv_record_account_receive.visibility = View.INVISIBLE
                tv_record_common_category.visibility = View.VISIBLE
                tv_record_all_category.visibility = View.VISIBLE
                // account name
                if (recordViewModel.transDetail.Account_Name.isEmpty()) tv_record_account_pay.text = recordViewModel.getAccountName(true)
            }
            TRANSACTION_TYPE_TRANSFER -> {
                iv_record_swap.visibility = View.VISIBLE
                iv_record_swap.isClickable = true
                iv_record_swap.setImageDrawable(requireContext().getDrawable(R.drawable.ic_baseline_swap_horiz_24))
                tv_record_account_receive.visibility = View.VISIBLE
                tv_record_common_category.visibility = View.GONE
                tv_record_all_category.visibility = View.GONE
                // account name
                if (recordViewModel.transDetail.Account_Name.isEmpty()) tv_record_account_pay.text = recordViewModel.getAccountName(true)
                if (recordViewModel.transDetail.AccountRecipient_Name.isEmpty()) tv_record_account_receive.text = recordViewModel.getAccountName(false)
                if (tv_record_account_pay.text.toString() == tv_record_account_receive.text.toString()){
                    val ltName = recordViewModel.getListOfAccountName(tv_record_account_pay.text.toString(), false)
                    if (ltName.isNotEmpty())  tv_record_account_receive.text = ltName[0]
                    else tv_record_account_receive.text = getString(R.string.msg_no_account)
                }
            }
            TRANSACTION_TYPE_DEBIT -> {
                iv_record_swap.visibility = View.VISIBLE
                iv_record_swap.isClickable = false
                iv_record_swap.setImageDrawable(requireContext().getDrawable(R.drawable.ic_baseline_keyboard_arrow_right_24))
                tv_record_account_receive.visibility = View.VISIBLE
                tv_record_common_category.visibility = View.GONE
                tv_record_all_category.visibility = View.GONE
                // account name
                if (recordViewModel.transDetail.Account_Name.isEmpty()) showAccountName(tv_record_category.text.toString())
            }

        }




    }

    private fun openCategoryManager(transactionID: Long) {
        // hide nav bottom bar
        //(activity as MainActivity).setNavBottomBarVisibility(View.GONE)

        // Put Data Before switch
        setFragmentResult(KEY_CATEGORY_MANAGER, bundleOf(
            KEY_CATEGORY_MANAGER_MODE to SELECT_MODE,
            KEY_CATEGORY_MANAGER_TRANSACTION_TYPE to transactionID))

        // switch to category manage fragment
        findNavController().navigate(R.id.navigation_category_manage)
    }


    @SuppressLint("CutPasteId")
    private fun showAccountListDialog(context: Context, event_ID: Long = 0L){

//        val dialog = MaterialDialog(context)
//            .noAutoDismiss()
//            .customView(R.layout.fragment_account_list, noVerticalPadding = true)
//
//        //val displayMetrics = DisplayMetrics()
//
//        dialog.account_list_layout.minHeight = Resources.getSystem().displayMetrics.heightPixels
//        dialog.account_list_layout.minWidth = Resources.getSystem().displayMetrics.widthPixels
//
//        //dialog.window?.setLayout(Resources.getSystem().displayMetrics.widthPixels,Resources.getSystem().displayMetrics.heightPixels)
//
//        dialog.show()

        val dialog = Dialog(context,android.R.style.Theme_DeviceDefault_NoActionBar)

        dialog.setContentView(R.layout.fragment_account_list)

        dialog.toolbar_account_list.setNavigationOnClickListener{
            dialog.dismiss()
        }


        dialog.show()



    }



}
