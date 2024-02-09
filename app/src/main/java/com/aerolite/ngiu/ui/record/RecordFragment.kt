package com.aerolite.ngiu.ui.record


import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.forEach
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.*
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.aerolite.ngiu.functions.SelectItem
import com.aerolite.ngiu.functions.getInternationalDateFromAmericanDate
import com.aerolite.ngiu.functions.hideSoftKeyboard
import com.aerolite.ngiu.functions.popupWindow
import com.aerolite.ngiu.functions.switchToAccountAttributePage
import com.aerolite.ngiu.functions.switchToAccountListFragment
import com.aerolite.ngiu.functions.switchToCategoryManager
import com.aerolite.ngiu.MainActivity
import com.aerolite.ngiu.R
import com.aerolite.ngiu.data.entities.Template
import com.aerolite.ngiu.data.entities.Trans
import com.aerolite.ngiu.functions.ACCOUNT_TYPE_RECEIVABLE
import com.aerolite.ngiu.functions.CATEGORY_SUB_BORROW
import com.aerolite.ngiu.functions.CATEGORY_SUB_LEND
import com.aerolite.ngiu.functions.CATEGORY_SUB_PAYMENT
import com.aerolite.ngiu.functions.CATEGORY_SUB_RECEIVE_PAYMENT
import com.aerolite.ngiu.functions.DateTimePicker
import com.aerolite.ngiu.functions.KEY_ACCOUNT_LIST_MODE_PAY
import com.aerolite.ngiu.functions.KEY_ACCOUNT_LIST_MODE_RECEIVE
import com.aerolite.ngiu.functions.KEY_RECORD
import com.aerolite.ngiu.functions.KEY_RECORD_ACCOUNT_ID
import com.aerolite.ngiu.functions.KEY_RECORD_ACCOUNT_LIST
import com.aerolite.ngiu.functions.KEY_RECORD_CATEGORY
import com.aerolite.ngiu.functions.KEY_RECORD_CATEGORY_ID
import com.aerolite.ngiu.functions.KEY_RECORD_PAY_ACCOUNT_NAME
import com.aerolite.ngiu.functions.KEY_RECORD_RECEIVE_ACCOUNT_NAME
import com.aerolite.ngiu.functions.KEY_RECORD_TRANSACTION_ID
import com.aerolite.ngiu.functions.KEY_RECORD_TRANSACTION_TYPE_ID
import com.aerolite.ngiu.functions.NEW_MODE
import com.aerolite.ngiu.functions.NON_REIMBURSABLE
import com.aerolite.ngiu.functions.REIMBURSABLE
import com.aerolite.ngiu.functions.REIMBURSED
import com.aerolite.ngiu.functions.SELECT_MODE
import com.aerolite.ngiu.functions.TRANSACTION_TYPE_DEBIT
import com.aerolite.ngiu.functions.TRANSACTION_TYPE_EXPENSE
import com.aerolite.ngiu.functions.TRANSACTION_TYPE_INCOME
import com.aerolite.ngiu.functions.TRANSACTION_TYPE_TRANSFER
import com.aerolite.ngiu.databinding.FragmentRecordBinding
import com.aerolite.ngiu.functions.*
import kotlin.collections.ArrayList
import com.aerolite.ngiu.ui.keyboard.Keyboard
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.*


class RecordFragment : Fragment() {


    private lateinit var recordViewModel: RecordViewModel
    private var _binding: FragmentRecordBinding? = null
    private var vpAdapter: RecordCategoryAdapter? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // hide bottom bar
        (activity as MainActivity).setNavBottomBarVisibility(View.GONE)


        /**
            CHECK IF ANY DATA WAS PASSED FROM OTHER FRAGMENTS
        */

            setFragmentResultListener(KEY_RECORD){ _, bundle ->
                val receivedTransID = bundle.getLong(KEY_RECORD_TRANSACTION_ID)
                val receivedAccountID = bundle.getLong(KEY_RECORD_ACCOUNT_ID)
                val receivedTransTypeID = bundle.getLong(KEY_RECORD_TRANSACTION_TYPE_ID)

                prepareUIData(receivedTransTypeID, receivedTransID, receivedAccountID, 0L)
                //Toast.makeText(context, receivedTransID.toString(),Toast.LENGTH_LONG).show()
            }

            // get string from category manage
            setFragmentResultListener(KEY_RECORD_CATEGORY) { _, bundle ->
                val receivedCateID = bundle.getLong(KEY_RECORD_CATEGORY_ID)

                recordViewModel.reloadCategory(requireContext(), recordViewModel.transDetail.TransactionType_ID)

                setCategory(recordViewModel.transDetail.TransactionType_ID, receivedCateID)
                //Toast.makeText(context,"".toString(),Toast.LENGTH_LONG).show()
                //loadCommonCategory(recordViewModel.transDetail.TransactionType_ID)
            }

            // get Account from Account List
            setFragmentResultListener(KEY_RECORD_ACCOUNT_LIST) { _, bundle ->
                val receivedPayAcctName = bundle.getString(KEY_RECORD_PAY_ACCOUNT_NAME).toString()
                val receivedReceiveAcctName = bundle.getString(KEY_RECORD_RECEIVE_ACCOUNT_NAME).toString()

                if (receivedPayAcctName.isNotEmpty()) {
                    recordViewModel.setAccountName( recordViewModel.transDetail.TransactionType_ID, receivedPayAcctName, true)
                }
                if (receivedReceiveAcctName.isNotEmpty()) {
                    recordViewModel.setAccountName( recordViewModel.transDetail.TransactionType_ID, receivedReceiveAcctName, false)
                }

            }

            // get transactionDetail from Template List
            setFragmentResultListener(KEY_TEMPLATE) { _, bundle ->
                val templateID = bundle.getLong(KEY_TEMPLATE_ID)

                if (templateID > 0L){
//                    recordViewModel.loadDataToRam(requireContext())
//                    recordViewModel.setTransactionDetailFromTemplate(templateID)
//                    recordViewModel.loadTransactionDetail(requireContext(), 0L, true)
//
//                    showDataOnUI(recordViewModel.transDetail.TransactionType_ID)

                    prepareUIData(0L, 0L, 0L, templateID)
                }
            }

    }

    // Fragments use a layout inflater to create their view at this stage.
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        recordViewModel =
            ViewModelProvider(this)[RecordViewModel::class.java]

        _binding = FragmentRecordBinding.inflate(inflater, container, false)




        /******* Common Category  *******/

        Thread {
            activity?.runOnUiThread {
                // pass the value to fragment from adapter when item clicked
                vpAdapter =
                    this.context?.let {
                        RecordCategoryAdapter(object : RecordCategoryAdapter.OnClickListener {

                            // catch the item click event from adapter
                            override fun onItemClick(categoryID: Long) {
                                // *** do something after clicked
                                // set category
                                setCategory(recordViewModel.transDetail.TransactionType_ID, categoryID)
                                // set account name
                                recordViewModel.setAccountName(recordViewModel.transDetail.TransactionType_ID)
                                // show data
                                setTextViewData()
                            }
                        })
                    }
                // load viewpager2 adapter
                binding.vpRecordCategory.adapter = vpAdapter
            }
        }.start()

        return binding.root
    }




    // called when the onCreate() method of the view has completed.
    @SuppressLint("SetTextI18n", "ClickableViewAccessibility", "CutPasteId")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //tv_record_amount.addDecimalLimiter()



        /**-------------------------- TOOLBAR -------------------------------------**/
        // choose items to show
        binding.toolbarRecord.menu.findItem(R.id.action_done).isVisible = true




        // click the navigation Icon in the left side of toolbar
        binding.toolbarRecord.setNavigationOnClickListener{
            // call back button event to switch to previous fragment
            //requireActivity().onBackPressed()
            NavHostFragment.findNavController(this).navigateUp()
        }

        // menu item clicked
        binding.toolbarRecord.setOnMenuItemClickListener{
            when (it.itemId) {
                // done menu
                R.id.action_done -> {
                    // save record
                    if (saveRecord() == 0) {
                        // call back button event to switch to previous fragment
                        //requireActivity().onBackPressed()
                        NavHostFragment.findNavController(this).navigateUp()
                    }
                    true
                }
                // delete menu
                R.id.action_delete -> {
                    // delete record
                    deleteRecord(activity, recordViewModel.transDetail.Transaction_ID, recordViewModel.TempLateID)
                    true
                }

                // template
//                R.id.action_template -> {
//                    // template list
//                    view.findNavController().navigate(R.id.navigation_template_list)
//                    true
//                }

                else -> true
            }
        }
        /**---------------------------- TOOLBAR ---------------------------------------*/


        // touch Expense textView, switch to Expense page
        binding.tvSectionExpense.setOnClickListener {
            selectTransactionType( TRANSACTION_TYPE_EXPENSE)
        }
        // touch Income textView, switch to Income page
        binding.tvSectionIncome.setOnClickListener {
            selectTransactionType(TRANSACTION_TYPE_INCOME)
        }
        // touch Transfer textView, switch to Transfer page
        binding.tvSectionTransfer.setOnClickListener {
            selectTransactionType(TRANSACTION_TYPE_TRANSFER)
        }
        // touch DebitCredit textView, switch to DebitCredit page
        binding.tvSectionDebitCredit.setOnClickListener {
            selectTransactionType(TRANSACTION_TYPE_DEBIT)
        }


        /**--------------- BUTTON TOUCH EVENTS ----------------------------------------------------*/
        // click Save Button: save record
        binding.tvRecordRightButton.setOnClickListener {
            if (saveRecord() == 0) {
                // exit
                //requireActivity().onBackPressed()
                NavHostFragment.findNavController(this).navigateUp()
            }
        }
        // Long click save button: save as template
        binding.tvRecordRightButton.setOnLongClickListener {
            popUpMsgSaveAsTemplate(activity)

            true
        }

        // Save and Next Button | Delete Button
        binding.tvRecordLeftButton.setOnClickListener {
            if (recordViewModel.transDetail.Transaction_ID > 0 || recordViewModel.TempLateID > 0){
                // delete
                deleteRecord(activity, recordViewModel.transDetail.Transaction_ID, recordViewModel.TempLateID)
            }else{
                // save and next
                if (saveRecord() == 0) {
                    // reset TextViews
                    resetTextViews()
                }
            }
        }

        /** date  **/
        binding.tvRecordDate.setOnClickListener {
            // date picker
            val date = LocalDate.parse(requireView().findViewById<TextView>(R.id.tv_record_date).text.toString(), DateTimeFormatter.ofPattern("MM/dd/yyyy"))

            DateTimePicker(
                date.year,
                date.monthValue-1,
                date.dayOfMonth
            ).pickDate(view.context) { _, year, month, day ->
                requireView().findViewById<TextView>(R.id.tv_record_date).text = LocalDate.of(year, month + 1, day)
                    .format(DateTimeFormatter.ofPattern("MM/dd/yyyy"))

                recordViewModel.transDetail.Transaction_Date =
                    getInternationalDateFromAmericanDate(requireView().findViewById<TextView>(R.id.tv_record_date).text.toString() + " " + requireView().findViewById<TextView>(R.id.tv_record_time).text.toString())
            }

        }

        /** time  **/
        binding.tvRecordTime.setOnClickListener {
            // time picker
            val time = LocalTime.parse(requireView().findViewById<TextView>(R.id.tv_record_time).text.toString(), DateTimeFormatter.ofPattern("HH:mm:ss"))

            DateTimePicker(
                startHour = time.hour,
                startMinute = time.minute
            ).pickTime(context) { _, hour, minute ->
                val s = Calendar.getInstance().get(Calendar.SECOND)
                requireView().findViewById<TextView>(R.id.tv_record_time).text = LocalTime.of(hour, minute, s).toString()
                recordViewModel.transDetail.Transaction_Date =
                   getInternationalDateFromAmericanDate(requireView().findViewById<TextView>(R.id.tv_record_date).text.toString() + " " + requireView().findViewById<TextView>(R.id.tv_record_time).text.toString())
            }
        }

        /** reimburse  **/
        binding.tvRecordReimburse.setOnClickListener {
            // change text
            binding.tvRecordReimburse.text = recordViewModel.setReimbursable(it.context)
            // change icon
            setReimburseIcon(recordViewModel.transDetail.Transaction_ReimburseStatus)
        }

        /** person  **/
        binding.tvRecordPerson.setOnClickListener {
            val tList: MutableList<String> = ArrayList()
            for (person in recordViewModel.person){
                tList.add(person.Person_Name)
            }
            popupWindow(requireContext(),getString(R.string.setting_merchant),  tList.toTypedArray(),
                object : SelectItem {
                    override fun clicked(idx: Int) {
                        binding.tvRecordPerson.text = tList[idx]
                    }
                })
        }
        binding.tvRecordPerson.doAfterTextChanged{
            recordViewModel.transDetail.Person_Name = binding.tvRecordPerson.text.toString()
        }

        /** Merchant **/
        binding.tvRecordMerchant.setOnClickListener {
            val tList: MutableList<String> = ArrayList()
            for (merchant in recordViewModel.merchant){
                tList.add(merchant.Merchant_Name)
            }
            popupWindow(requireContext(),getString(R.string.setting_merchant),  tList.toTypedArray(),
                object : SelectItem {
                    override fun clicked(idx: Int) {
                        binding.tvRecordMerchant.text = tList[idx]
                    }
                })
        }
        binding.tvRecordMerchant.doAfterTextChanged{
            recordViewModel.transDetail.Merchant_Name = binding.tvRecordMerchant.text.toString()
        }


        /** Project **/
        binding.tvRecordProject.setOnClickListener {
            val tList: MutableList<String> = ArrayList()
            for (project in recordViewModel.project){
                tList.add(project.Project_Name)
            }
            popupWindow(requireContext(),getString(R.string.setting_merchant),  tList.toTypedArray(),
                object : SelectItem {
                    override fun clicked(idx: Int) {
                        binding.tvRecordProject.text = tList[idx]
                    }
                })
        }
        binding.tvRecordProject.doAfterTextChanged{
            recordViewModel.transDetail.Project_Name = binding.tvRecordProject.text.toString()
        }


        /*** touch feedback ***/
        // other info -- under Memo section
        binding.layoutRecordOtherInfo.forEach {
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


        /** all category  **/
        binding.tvRecordAllCategory.setOnClickListener{
            when (recordViewModel.transDetail.TransactionType_ID){
                TRANSACTION_TYPE_EXPENSE, TRANSACTION_TYPE_INCOME ->
                    switchToCategoryManager(view, this, SELECT_MODE, recordViewModel.transDetail.TransactionType_ID)
                    //openCategoryManager(recordViewModel.transDetail.TransactionType_ID)
            }
        }


        /** category name **/
        binding.tvRecordCategory.setOnClickListener {
            when (recordViewModel.transDetail.TransactionType_ID){
                TRANSACTION_TYPE_EXPENSE, TRANSACTION_TYPE_INCOME ->
                    switchToCategoryManager(view, this, SELECT_MODE,recordViewModel.transDetail.TransactionType_ID)
                //openCategoryManager(recordViewModel.transDetail.TransactionType_ID)
            }
        }
//        tv_record_category.doAfterTextChanged{
//            recordViewModel.transDetail.Category_Name = tv_record_category.text.toString()
//        }


        /** Amount **/
        binding.tvRecordAmount.setOnClickListener {
            binding.tvRecordMemo.clearFocus()
            binding.tvRecordMemo.hideSoftKeyboard()
            callKeyboard(view)
        }
        binding.tvRecordAmount.doAfterTextChanged{
            recordViewModel.transDetail.Transaction_Amount = binding.tvRecordAmount.text.toString().toDouble()
        }


        /** swap **/
        binding.ivRecordSwap.setOnClickListener {
            if (binding.tvRecordAccountPay.text.toString() != getString(R.string.msg_no_account) && binding.tvRecordAccountReceive.text.toString() != getString(R.string.msg_no_account)) {
                // <=>
                binding.tvRecordAccountReceive.text = binding.tvRecordAccountPay.text.apply {
                    binding.tvRecordAccountPay.text = binding.tvRecordAccountReceive.text
                }
                // <=>
                recordViewModel.tempSaveOutAccountName = recordViewModel.tempSaveInAccountName.apply {
                    recordViewModel.tempSaveInAccountName = recordViewModel.tempSaveOutAccountName
                }
            }
        }


        /** account pay **/
        binding.tvRecordAccountPay.setOnClickListener {

            if (binding.tvRecordAccountPay.text.toString() != getString(R.string.msg_no_account)) {
                // open account list
                switchToAccountListFragment(view, this,
                    KEY_ACCOUNT_LIST_MODE_PAY,
                    recordViewModel.transDetail.TransactionType_ID,
                    recordViewModel.transDetail.Category_ID,
                    recordViewModel.getAccountID(binding.tvRecordAccountReceive.text.toString())
                )
            }else{
                // create new account if no account
                createNewAccount(view, recordViewModel.transDetail.Category_Name, true)
            }
        }
//        tv_record_account_pay.doAfterTextChanged{
//            recordViewModel.transDetail.Account_Name = tv_record_account_pay.text.toString()
//            recordViewModel.transDetail.Account_ID = recordViewModel.getAccountID(tv_record_account_pay.text.toString())
//        }


        /** account receive **/
        binding.tvRecordAccountReceive.setOnClickListener {

            if (binding.tvRecordAccountReceive.text.toString() != getString(R.string.msg_no_account)) {
                // open account list
                switchToAccountListFragment(view, this,
                    KEY_ACCOUNT_LIST_MODE_RECEIVE,
                    recordViewModel.transDetail.TransactionType_ID,
                    recordViewModel.transDetail.Category_ID,
                    recordViewModel.getAccountID(binding.tvRecordAccountPay.text.toString())
                )
            }else{
                // create new account if no account
                createNewAccount(view, recordViewModel.transDetail.Category_Name, false)
            }

        }
//        tv_record_account_receive.doAfterTextChanged{
//            recordViewModel.transDetail.AccountRecipient_Name = tv_record_account_receive.text.toString()
//            recordViewModel.transDetail.AccountRecipient_ID = recordViewModel.getAccountID(tv_record_account_receive.text.toString())
//        }


        /** memo **/
        binding.tvRecordMemo.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) Keyboard(view).hide()
        }
        binding.tvRecordMemo.doAfterTextChanged {
            recordViewModel.transDetail.Transaction_Memo = binding.tvRecordMemo.text.toString().trim()
        }
        /**--------------- TOUCH EVENTS ------------------------------------------------------------------------*/
    }




    // called when the fragment is visible and actively running.
    override fun onResume() {
        super.onResume()

        /** template menu (put it here to make sure received Transaction ID from setFragmentResultListener() ) **/
//        if (recordViewModel.transDetail.Transaction_ID == 0L) {
//            binding.toolbarRecord.menu.findItem(R.id.action_template).isVisible = true
//        }

        //recordViewModel.loadDataToRam(requireContext())

        // Set Account
        setAccount(recordViewModel.transDetail.TransactionType_ID, recordViewModel.transDetail.Transaction_ID, )

        // load data to UI textview
        showDataOnUI(recordViewModel.transDetail.TransactionType_ID)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        //(activity as MainActivity).setNavBottomBarVisibility(View.VISIBLE)
        _binding = null
        vpAdapter = null
    }






    /**------------------------------------------  Private Functions -----------------------------------------------**/

    private fun prepareUIData(transType: Long, transID: Long, acctID: Long, templateID: Long){
        /**** prepare the data *****/

        // load Data to Ram
        recordViewModel.loadDataToRam(requireContext())

        // load specified transaction detail
        if (transID > 0L || templateID > 0L ) {
            recordViewModel.loadTransactionDetail(requireContext(), transID, templateID)

            // show delete menu
            binding.toolbarRecord.menu.findItem(R.id.action_delete).isVisible = true
            // show delete button
            binding.tvRecordLeftButton.text = getString(R.string.menu_delete)

        }else{
            // show template menu
            //binding.toolbarRecord.menu.findItem(R.id.action_template).isVisible = true

            // set transaction type
            recordViewModel.setTransactionType(transType)

            // set category
            setCategory(transType)

            // Set Account
            setAccount(transType, transID, acctID)

            // person
            setPerson()

            // merchant
            setMerchant()

            // project
            setProject()

            // reimburse
            //setReimburse()
        }


        /*** show data ***/
        showDataOnUI(transType)

        /** show Keyboard **/
        if (transID == 0L && templateID == 0L) callKeyboard(requireView())
    }

    private fun setProject() {
        if (recordViewModel.transDetail.Project_Name.isEmpty()) {
            recordViewModel.transDetail.Project_Name = recordViewModel.project[0].Project_Name
        }
    }

    private fun setMerchant() {
        if (recordViewModel.transDetail.Merchant_Name.isEmpty()) {
            recordViewModel.transDetail.Merchant_Name = recordViewModel.merchant[0].Merchant_Name
        }
    }

    private fun setPerson() {
        if (recordViewModel.transDetail.Person_Name.isEmpty()) {
            recordViewModel.transDetail.Person_Name = recordViewModel.person[0].Person_Name
        }
    }

    private fun setAccount(transType: Long, transID: Long = 0L, acctID: Long=0L) {
        // !! set category before set account !!
        if (acctID > 0){
            val acctName = recordViewModel.getAccountNameByID(acctID)
            recordViewModel.setAccountName(transType, acctName)
        }else {
            recordViewModel.setAccountName(transType)
        }
    }



    private fun setCategory(transType: Long, cateID: Long = 0L) {
        recordViewModel.setSubCategory(transType, cateID)
    }


    private fun showDataOnUI(transType: Long){
        /*** show data on UI  **/

        // show transaction type color
        showTextViewColor(transType)
        // show common category
        loadCommonCategory(transType)

        // set swap icon, account visible, common category visible
        setViewsVisible(transType)

        // budget

        // set textview data
        setTextViewData()
    }

    private fun setTextViewData(){
        // amount
        binding.tvRecordAmount.text = "%.2f".format(recordViewModel.transDetail.Transaction_Amount)

        // category
        binding.tvRecordCategory.text = recordViewModel.transDetail.Category_Name
        // account
        binding.tvRecordAccountPay.text = recordViewModel.transDetail.Account_Name
        binding.tvRecordAccountReceive.text = recordViewModel.transDetail.AccountRecipient_Name
        // date
        binding.tvRecordDate.text = recordViewModel.transDetail.Transaction_Date.format(DateTimeFormatter.ofPattern("MM/dd/yyyy"))
        binding.tvRecordTime.text = recordViewModel.transDetail.Transaction_Date.format(DateTimeFormatter.ofPattern("HH:mm:ss"))
        // memo
        if (recordViewModel.transDetail.Transaction_Memo.isNotEmpty()){
            binding.tvRecordMemo.setText(recordViewModel.transDetail.Transaction_Memo)
        }
        // person
        binding.tvRecordPerson.text = recordViewModel.transDetail.Person_Name
        // merchant
        binding.tvRecordMerchant.text = recordViewModel.transDetail.Merchant_Name
        // project
        binding.tvRecordProject.text = recordViewModel.transDetail.Project_Name
        // reimburse
        setReimburseIcon(recordViewModel.transDetail.Transaction_ReimburseStatus)
        binding.tvRecordReimburse.text = recordViewModel.getReimbursable(requireContext(), recordViewModel.transDetail.Transaction_ReimburseStatus)
        //todo period,
    }

    private fun selectTransactionType(transType: Long){
        // set transaction type
        recordViewModel.setTransactionType(transType)
        // set category
        setCategory(transType)
        // set account
        setAccount(transType)

        showDataOnUI(transType)
    }



    /** Create New Account **/
    private fun createNewAccount(view: View, categoryName: String, payable: Boolean) {

        when (recordViewModel.getCategoryID(categoryName)) {
            // borrow in | received
            CATEGORY_SUB_BORROW, CATEGORY_SUB_RECEIVE_PAYMENT -> {
                if (payable){
                    // create P/R account
                    switchToAccountAttributePage(view, ACCOUNT_TYPE_RECEIVABLE
                                            ,0,0.0,
                                            NEW_MODE
                    )
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
                        NEW_MODE
                    )

                }else{
                    findNavController().navigate(R.id.navigation_add_account)
                }
            }
            else ->{
                findNavController().navigate(R.id.navigation_add_account)
            }
        }

    }


    /** save record **/
    private fun saveRecord() : Int{

        // If amount is 0, do not save
        if (binding.tvRecordAmount.text.toString().toDouble() == 0.0) {
            Toast.makeText(
                context,
                getString(R.string.msg_cannot_save_with_zero_amount),
                Toast.LENGTH_SHORT
            ).show()
            //Snack.make(requireView(), getString(R.string.msg_cannot_save_with_zero_amount), Snack.LENGTH_SHORT).show()
            return 1
        }else {

            //-- set the values base on transaction type --//
            // reimburse
            if (recordViewModel.transDetail.TransactionType_ID > TRANSACTION_TYPE_INCOME){
                recordViewModel.transDetail.Transaction_ReimburseStatus = 0
            }


            val trans = Trans(
                Transaction_ID = recordViewModel.transDetail.Transaction_ID,
                TransactionType_ID = recordViewModel.transDetail.TransactionType_ID,
                Category_ID = recordViewModel.transDetail.Category_ID,
                Account_ID = recordViewModel.getAccountID(binding.tvRecordAccountPay.text.toString()),
                Transaction_Amount = binding.tvRecordAmount.text.toString().toDouble(),
                Transaction_Amount2 = if (recordViewModel.transDetail.TransactionType_ID > TRANSACTION_TYPE_EXPENSE) binding.tvRecordAmount.text.toString().toDouble() else 0.00 ,
                Transaction_Date = recordViewModel.transDetail.Transaction_Date,
                Transaction_Memo = binding.tvRecordMemo.text.toString().trim(),
                Merchant_ID = recordViewModel.merchant[recordViewModel.merchant.indexOfFirst { it.Merchant_Name == binding.tvRecordMerchant.text.toString() }].Merchant_ID,
                Person_ID = recordViewModel.person[recordViewModel.person.indexOfFirst { it.Person_Name == binding.tvRecordPerson.text.toString() }].Person_ID,
                Project_ID = 1L, //recordViewModel.transDetail.Period_ID,
                Transaction_ReimburseStatus = recordViewModel.transDetail.Transaction_ReimburseStatus
            )

            // AccountRecipient ID
            trans.AccountRecipient_ID = if (recordViewModel.transDetail.TransactionType_ID < TRANSACTION_TYPE_TRANSFER){
                                            trans.Account_ID
                                        }else{
                                            recordViewModel.getAccountID(binding.tvRecordAccountReceive.text.toString())
                                        } //recordViewModel.transDetail.AccountRecipient_ID

            // check Account ID
            if (trans.Account_ID < 1L || trans.AccountRecipient_ID < 1L) {
                Toast.makeText(
                    context,
                    getString(R.string.msg_cannot_save_with_no_account),
                    Toast.LENGTH_SHORT
                ).show()
                return 1
            }


            // save
            if (recordViewModel.transDetail.Transaction_ID > 0) {
                recordViewModel.updateTransaction(requireContext(), trans)
            } else {
                recordViewModel.addTransaction(requireContext(),trans)
            }

            Toast.makeText(context,getString(R.string.msg_saved),Toast.LENGTH_SHORT).show()
            return 0
        }
    }


    /**  Save As Template **/
    private fun saveAsTemplate() : Int{

        //-- set the values base on transaction type --//
        // reimburse
        if (recordViewModel.transDetail.TransactionType_ID > TRANSACTION_TYPE_INCOME){
            recordViewModel.transDetail.Transaction_ReimburseStatus = 0
        }


        val template = Template(
            Template_ID = recordViewModel.TempLateID,
            TransactionType_ID = recordViewModel.transDetail.TransactionType_ID,
            Category_ID = recordViewModel.transDetail.Category_ID,
            Account_ID = recordViewModel.getAccountID(binding.tvRecordAccountPay.text.toString()),
            Transaction_Amount = binding.tvRecordAmount.text.toString().toDouble(),
            Transaction_Memo = binding.tvRecordMemo.text.toString().trim(),
            Merchant_ID = recordViewModel.merchant[recordViewModel.merchant.indexOfFirst { it.Merchant_Name == binding.tvRecordMerchant.text.toString() }].Merchant_ID,
            Person_ID = recordViewModel.person[recordViewModel.person.indexOfFirst { it.Person_Name == binding.tvRecordPerson.text.toString() }].Person_ID,
            Project_ID = 1L, //recordViewModel.transDetail.Period_ID,
            Transaction_ReimburseStatus = recordViewModel.transDetail.Transaction_ReimburseStatus
        )

        // AccountRecipient ID
        template.AccountRecipient_ID = if (recordViewModel.transDetail.TransactionType_ID < TRANSACTION_TYPE_TRANSFER){
                                            template.Account_ID
                                        }else{
                                            recordViewModel.getAccountID(binding.tvRecordAccountReceive.text.toString())
                                        } //recordViewModel.transDetail.AccountRecipient_ID

        // check Account ID
        if (template.Account_ID < 1L || template.AccountRecipient_ID < 1L) {
            Toast.makeText(
                context,
                getString(R.string.msg_cannot_save_with_no_account),
                Toast.LENGTH_SHORT
            ).show()
            return 1
        }


        // save todo need to rewrite, save as template
//        if (recordViewModel.transDetail.Transaction_ID > 0) {
//            recordViewModel.updateTransaction(requireContext(), template)
//        } else {
            recordViewModel.saveTemplate(requireContext(),template)
//        }

        Toast.makeText(context,getString(R.string.msg_saved),Toast.LENGTH_SHORT).show()
        return 0
    }


    /** Pop Up Confirm Msg to save as template  **/
    private fun popUpMsgSaveAsTemplate(activity: FragmentActivity?) {

        val dialogBuilder = AlertDialog.Builder(activity)

        dialogBuilder.setMessage(getString(R.string.msg_content_save_as_template))
            .setCancelable(true)
            .setPositiveButton(getString(R.string.msg_button_confirm)) { _, _ ->

                // save as template
                if (saveAsTemplate() == 0) {
                    // exit
                    NavHostFragment.findNavController(this).navigateUp()
                }
            }
            .setNegativeButton(getString(R.string.msg_button_cancel)) { dialog, _ ->
                // cancel
                dialog.cancel()
            }

        // set Title Style
        val titleView = layoutInflater.inflate(R.layout.popup_title,null)
        // set Title Text
        titleView.findViewById<TextView>(R.id.tv_popup_title_text).text = getString(R.string.msg_Title_prompt)

        val alert = dialogBuilder.create()
        //alert.setIcon(R.drawable.ic_baseline_delete_forever_24)
        alert.setCustomTitle(titleView)
        alert.show()

    }


    /** Delete record or template **/
    private fun deleteRecord(activity: FragmentActivity?, transactionID: Long, templateID: Long) {

        val dialogBuilder = AlertDialog.Builder(activity)

        var strMSG = ""
        if (transactionID > 0L) { strMSG = getString(R.string.msg_content_transaction_delete) }
        if (templateID > 0L) { strMSG = getString(R.string.msg_content_template_delete) }

        dialogBuilder.setMessage(strMSG)
            .setCancelable(true)
            .setPositiveButton(getString(R.string.msg_button_confirm)) { _, _ ->

                if (transactionID > 0L) {
                    // delete record
                    val trans = Trans(Transaction_ID = transactionID)
                    recordViewModel.deleteTrans(requireContext(), trans)
                }

                if (templateID > 0L ){
                    // delete template
                    val template = Template(Template_ID = templateID)
                    recordViewModel.deleteTemplate(requireContext(), template)
                }

                // exit
                //requireActivity().onBackPressed()
                NavHostFragment.findNavController(this).navigateUp()

            }
            .setNegativeButton(getString(R.string.msg_button_cancel)) { dialog, _ ->
                // cancel
                dialog.cancel()
            }

        // set Title Style
        val titleView = layoutInflater.inflate(R.layout.popup_title,null)
        // set Title Text
        titleView.findViewById<TextView>(R.id.tv_popup_title_text).text = getString(R.string.msg_Title_prompt)

        val alert = dialogBuilder.create()
        //alert.setIcon(R.drawable.ic_baseline_delete_forever_24)
        alert.setCustomTitle(titleView)
        alert.show()
    }



    /** reset Textview **/
    @SuppressLint("CutPasteId")
    private fun resetTextViews() {
        // reset amount
        binding.tvRecordAmount.text = "0.00"
        // reset time
        binding.tvRecordTime.text = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"))
        // reset time and save into transaction detail
        recordViewModel.transDetail.Transaction_Date =
            getInternationalDateFromAmericanDate(binding.tvRecordDate.text.toString() + " " + binding.tvRecordTime.text.toString())
        // reset Momo
        binding.tvRecordMemo.text.clear()
        // reset Reimburse
        recordViewModel.transDetail.Transaction_ReimburseStatus = NON_REIMBURSABLE
        setReimburseIcon(NON_REIMBURSABLE)

        // Amount TextView get focus
        binding.tvRecordAmount.callOnClick()

    }

    /** Load Common Category **/
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

                binding.vpRecordCategory.adapter= vpAdapter

            }
        }.start()
    }


    private fun setReimburseIcon(transactionReimburseStatus: Int) {
        when(transactionReimburseStatus){
            NON_REIMBURSABLE -> {
                binding.tvRecordReimburse.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_check_box_outline_blank_24,0,0,0)
            }
            REIMBURSABLE -> {
                binding.tvRecordReimburse.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_indeterminate_check_box_24,0,0,0)
            }
            REIMBURSED -> {
                binding.tvRecordReimburse.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_check_box_24,0,0,0)
            }
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables", "CutPasteId")
    private fun setViewsVisible(transType: Long){

        //
        when (transType) {
            TRANSACTION_TYPE_EXPENSE, TRANSACTION_TYPE_INCOME -> {
                binding.ivRecordSwap.visibility = View.INVISIBLE
                binding.tvRecordAccountReceive.visibility = View.INVISIBLE
                binding.tvRecordCommonCategory.visibility = View.VISIBLE
                binding.tvRecordAllCategory.visibility = View.VISIBLE

                binding.tvRecordPerson.visibility = View.VISIBLE
                binding.tvRecordMerchant.visibility = View.VISIBLE
                binding.tvRecordProject.visibility = View.VISIBLE
                binding.tvRecordReimburse.visibility = View.VISIBLE
            }
            TRANSACTION_TYPE_TRANSFER -> {
                binding.ivRecordSwap.visibility = View.VISIBLE
                binding.ivRecordSwap.isClickable = true
                binding.ivRecordSwap.setImageDrawable(requireContext().getDrawable(R.drawable.ic_baseline_swap_horiz_24))
                binding.tvRecordAccountReceive.visibility = View.VISIBLE
                binding.tvRecordCommonCategory.visibility = View.GONE
                binding.tvRecordAllCategory.visibility = View.GONE

                binding.tvRecordPerson.visibility = View.GONE
                binding.tvRecordMerchant.visibility = View.GONE
                binding.tvRecordProject.visibility = View.GONE
                binding.tvRecordReimburse.visibility = View.GONE
            }
            TRANSACTION_TYPE_DEBIT -> {
                binding.ivRecordSwap.visibility = View.VISIBLE
                binding.ivRecordSwap.isClickable = false
                binding.ivRecordSwap.setImageDrawable(requireContext().getDrawable(R.drawable.ic_baseline_keyboard_arrow_right_24))
                binding.tvRecordAccountReceive.visibility = View.VISIBLE
                binding.tvRecordCommonCategory.visibility = View.GONE
                binding.tvRecordAllCategory.visibility = View.GONE

                binding.tvRecordPerson.visibility = View.GONE
                binding.tvRecordMerchant.visibility = View.GONE
                binding.tvRecordProject.visibility = View.GONE
                binding.tvRecordReimburse.visibility = View.GONE
            }

        }




    }

    private fun showTextViewColor(transType: Long){
        // set transaction type textview color
        binding.tvSectionExpense.setTextColor(ContextCompat.getColor(requireContext(), recordViewModel.textViewTransactionTypeColor[0]))
        binding.tvSectionExpensePointer.visibility = recordViewModel.transactionTypePointerVisible[0]
        binding.tvSectionIncome.setTextColor(ContextCompat.getColor(requireContext(), recordViewModel.textViewTransactionTypeColor[1]))
        binding.tvSectionIncomePointer.visibility = recordViewModel.transactionTypePointerVisible[1]
        binding.tvSectionTransfer.setTextColor(ContextCompat.getColor(requireContext(), recordViewModel.textViewTransactionTypeColor[2]))
        binding.tvSectionTransferPointer.visibility = recordViewModel.transactionTypePointerVisible[2]
        binding.tvSectionDebitCredit.setTextColor(ContextCompat.getColor(requireContext(), recordViewModel.textViewTransactionTypeColor[3]))
        binding.tvSectionDebitCreditPointer.visibility = recordViewModel.transactionTypePointerVisible[3]

        // set amount color
        binding.tvRecordAmount.setTextColor(
            when (transType){
                TRANSACTION_TYPE_EXPENSE -> ContextCompat.getColor( requireContext(), R.color.app_expense_amount)
                TRANSACTION_TYPE_INCOME -> ContextCompat.getColor( requireContext(), R.color.app_income_amount)
                else -> ContextCompat.getColor( requireContext(), R.color.app_amount)
            }
        )

    }

/*    private fun openCategoryManager(transactionID: Long) {
        // hide nav bottom bar
        //(activity as MainActivity).setNavBottomBarVisibility(View.GONE)

        // Put Data Before switch
        setFragmentResult(KEY_CATEGORY_MANAGER, bundleOf(
            KEY_CATEGORY_MANAGER_MODE to SELECT_MODE,
            KEY_CATEGORY_MANAGER_TRANSACTION_TYPE to transactionID))

        // switch to category manage fragment
        findNavController().navigate(R.id.navigation_category_manage)
    }*/

    private fun callKeyboard(view: View){
        if (Keyboard(view).state() != View.VISIBLE) {
            Keyboard(view).initKeys(view.findViewById<TextView>(R.id.tv_record_amount))
            Keyboard(view).show()
        }
    }




}
