package com.aerolite.ngiu.ui.category

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.database.sqlite.SQLiteException
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aerolite.ngiu.functions.ADD_MAIN_CATEGORY
import com.aerolite.ngiu.functions.ADD_SUB_CATEGORY
import com.aerolite.ngiu.functions.EDIT_MAIN_CATEGORY
import com.aerolite.ngiu.functions.EDIT_MODE
import com.aerolite.ngiu.functions.EDIT_SUB_CATEGORY
import com.aerolite.ngiu.functions.KEY_CATEGORY_MANAGER
import com.aerolite.ngiu.functions.KEY_CATEGORY_MANAGER_MODE
import com.aerolite.ngiu.functions.KEY_CATEGORY_MANAGER_TRANSACTION_TYPE
import com.aerolite.ngiu.functions.KEY_RECORD_CATEGORY
import com.aerolite.ngiu.functions.KEY_RECORD_CATEGORY_ID
import com.aerolite.ngiu.functions.MAIN_CATEGORY
import com.aerolite.ngiu.functions.SUB_CATEGORY
import com.aerolite.ngiu.functions.TRANSACTION_TYPE_EXPENSE
import com.aerolite.ngiu.functions.TRANSACTION_TYPE_INCOME
import com.aerolite.ngiu.MainActivity
import com.aerolite.ngiu.R
import com.aerolite.ngiu.data.entities.Category
import com.aerolite.ngiu.databinding.FragmentCategoryManageBinding
import com.aerolite.ngiu.functions.*
//import kotlinx.android.synthetic.main.fragment_category_manage.*
//import kotlinx.android.synthetic.main.popup_title.view.*

class CategoryManagerFragment: Fragment() {
    private lateinit var categoryManagerViewModel: CategoryManagerViewModel
    private var _binding: FragmentCategoryManageBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private var mainCategoryAdapter: MainCategoryAdapter? = null
    private var subCategoryAdapter: SubCategoryAdapter? = null

    private var transactionTypeID: Long = 0L
    private var cateMode: Int = EDIT_MODE

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // hide bottom bar
        (activity as MainActivity).setNavBottomBarVisibility(View.GONE)

        /*
        // do something before backPressed
        activity?.onBackPressedDispatcher?.addCallback(this, object: OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                
                //if (shouldInterceptBackPress()){
                    setFragmentResult("selected_category", bundleOf("subCategory_Name" to receivedString))
                    setFragmentResult("selected_transaction_type", bundleOf("transaction_type" to categoryManagerViewModel.currentTransactionType))
                //}else {
                    isEnabled = false
                    activity?.onBackPressed()
               // }
            }
        })

         */

        // receive data from other fragment
//        cateMode = arguments?.getInt(KEY_CATEGORY_MANAGER_MODE)!!
//        transactionTypeID = arguments?.getLong(KEY_CATEGORY_MANAGER_TRANSACTION_TYPE)!!
//
//        if ( transactionTypeID > 0 ) {
//            categoryManagerViewModel.currentTransactionType = transactionTypeID
//            categoryManagerViewModel.loadMainCategory(requireContext(), transactionTypeID)
//        }
//
//        if (cateMode == EDIT_MODE)
//            toolbar_category.menu.findItem(R.id.action_edit).isVisible = true


        // receive data from other fragment
        setFragmentResultListener(KEY_CATEGORY_MANAGER) { _, bundle ->
            cateMode = bundle.getInt(KEY_CATEGORY_MANAGER_MODE)
            transactionTypeID = bundle.getLong(KEY_CATEGORY_MANAGER_TRANSACTION_TYPE)

            // show the Edit Icon
            if (cateMode == EDIT_MODE)
                binding.toolbarCategory.menu.findItem(R.id.action_edit).isVisible = true

            if (transactionTypeID > 0 ) {
                // set transaction type
                categoryManagerViewModel.currentTransactionType = transactionTypeID
                // load data
                categoryManagerViewModel.loadMainCategory(requireContext(),cateMode, transactionTypeID)
            }
            //Toast.makeText(context, cateMode.toString(), Toast.LENGTH_SHORT).show()
        }




        // Main Category Adapter
        Thread {
            this.activity?.runOnUiThread {

                binding.recyclerviewCategoryMain.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL,false)

                mainCategoryAdapter = this.context?.let {
                    MainCategoryAdapter(object: MainCategoryAdapter.OnClickListener {
                        // catch the item click event from adapter
                        override fun onItemClick(rID: Long, addNew: Boolean) {
                            if (addNew) {
                                manageCategory(ADD_MAIN_CATEGORY)
                            }else {
                                showSubCategoryItems(rID)
                            }
                        }

                        override fun onItemLongClick(rID: Long, mainCategoryName: String, nextRowID: Long) {
                            // edit/delete
                            if (cateMode == EDIT_MODE)
                                manageCategory(EDIT_MAIN_CATEGORY,rID,mainCategoryName, nextRowID)
                        }
                    })
                }
                binding.recyclerviewCategoryMain.adapter = mainCategoryAdapter
            }
        }.start()

        // Sub Category Adapter
        Thread {
            this.activity?.runOnUiThread {

                binding.recyclerviewCategorySub.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL,false)

                subCategoryAdapter = this.context?.let {
                    SubCategoryAdapter(object: SubCategoryAdapter.OnClickListener {
                        // catch the item click event from adapter
                        override fun onItemClick(rID: Long, subCategoryName: String, addNew: Boolean) {
                            if (addNew){
                                manageCategory(ADD_SUB_CATEGORY)
                            }else {
                                // edit mode
                                if (cateMode == EDIT_MODE) {
                                    // edit sub category
                                    manageCategory(EDIT_SUB_CATEGORY, rID, subCategoryName)

                                    // select mode
                                } else {
                                    // pass the string back to record fragment
                                    backToRecordFragment(rID)
                                }
                            }
                        }

                        // long click: delete
                        override fun onItemLongClick(rID: Long) {
                            if (cateMode == EDIT_MODE) {
                                // delete sub category
                                deleteCategory(1, rID)
                            }
                        }

                        // star click: change status
                        override fun onStarClick(rID: Long, commonValue: Boolean) {
                            // save status
                            val subCate = categoryManagerViewModel.subCategory[
                                    categoryManagerViewModel.subCategory.indexOfFirst { it.Category_ID == rID }
                            ]
                            subCate.Category_Common = commonValue

                            categoryManagerViewModel.updateCategory(requireContext(), subCate)
                            // refresh
                            refreshSubCategory()
                        }
                    })
                }
                binding.recyclerviewCategorySub.adapter = subCategoryAdapter
            }
        }.start()

    }



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        categoryManagerViewModel = ViewModelProvider(this)[CategoryManagerViewModel::class.java]
        _binding = FragmentCategoryManageBinding.inflate(inflater, container, false)


        // load data
        //if (transactionTypeID == 0L) categoryManagerViewModel.loadMainCategory(requireContext())


        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        //---------------------------tool bar--------------------------------
        // choose items to show
        //toolbar_category.menu.findItem(R.id.action_edit).isVisible = true

        // click the navigation Icon in the left side of toolbar
        binding.toolbarCategory.setNavigationOnClickListener {

            if (cateMode == EDIT_MODE) {
                // edit mode
                requireActivity().onBackPressedDispatcher.onBackPressed()

                // select mode
            } else {
                // pass the string back to record fragment
                backToRecordFragment(0L)
            }
        }

        // menu item clicked
        binding.toolbarCategory.setOnMenuItemClickListener{
            when (it.itemId) {
                // done menu
                R.id.action_edit -> {
                    // turn on edit mode
                    //mainCategoryAdapter?.setcateMode(true)
                    //recyclerview_category_main.adapter = mainCategoryAdapter
                    true
                }
                R.id.action_done -> {
                    // quite edit mode

                    true
                }

                else -> true
            }
        }
        //---------------------------tool bar--------------------------------



    }



    override fun onResume() {
        super.onResume()

        // Main Category Adapter
        Thread {
            this.activity?.runOnUiThread {
                mainCategoryAdapter?.setList(categoryManagerViewModel.mainCategory)
            }
        }.start()

        // Sub Category Adapter
        Thread {
            this.activity?.runOnUiThread {
                subCategoryAdapter?.setList(categoryManagerViewModel.getSubCategory(requireContext(),categoryManagerViewModel.currentActiveMainCategory,cateMode))
            }
        }.start()


        // show title
        when (transactionTypeID){
            TRANSACTION_TYPE_EXPENSE -> {
                binding.toolbarCategory.setTitle(if (cateMode == EDIT_MODE) R.string.nav_title_category_expense_manage else R.string.nav_title_category_expense)
            }
            TRANSACTION_TYPE_INCOME -> {
                binding.toolbarCategory.setTitle(if (cateMode == EDIT_MODE) R.string.nav_title_category_income_manage else R.string.nav_title_category_income)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()

        //setFragmentResult("selected_category", bundleOf("subCategory_Name" to sendString))

        //(activity as MainActivity).setNavBottomBarVisibility(View.VISIBLE)
        _binding = null
    }





    //------------------------------------------Private Function------------------------------------


    private fun showSubCategoryItems(rID: Long) {

        if (categoryManagerViewModel.currentActiveMainCategory != rID){
            // main category item click
            //mainCategoryAdapter?.setList(categoryManagerViewModel.mainCategory)
            binding.recyclerviewCategoryMain.adapter = mainCategoryAdapter
            // show sub category
            subCategoryAdapter?.setList(categoryManagerViewModel.getSubCategory(requireContext(),rID,cateMode))

            categoryManagerViewModel.currentActiveMainCategory = rID

        }
    }


    // add/edit main and sub category------------------
    // 0: add main category
    // 1: edit main category
    // 2: add sub category
    // 3: edit sub category
    // todo name must be unique
    private fun manageCategory(type: Int, rID: Long = 0L, string: String = "", nextRowID: Long = 0) {
        val alert = AlertDialog.Builder(context)
        val editText = EditText(activity)
        val titleView = View.inflate(context, R.layout.popup_title, null)

        editText.isSingleLine = true
        //editText.imeOptions = EditorInfo.IME_ACTION_DONE

        when (type) {
            EDIT_MAIN_CATEGORY, EDIT_SUB_CATEGORY -> {
                editText.setText(string)
                titleView.findViewById<TextView>(R.id.tv_popup_title_text).text = getString(R.string.msg_edit_category)
            }
            ADD_MAIN_CATEGORY, ADD_SUB_CATEGORY -> {
                titleView.findViewById<TextView>(R.id.tv_popup_title_text).text = getString(R.string.msg_new_category)
            }
        }


        alert.setView(editText)
            .setCustomTitle(titleView)
            .setPositiveButton(R.string.msg_button_confirm
            ) { _, _ -> //What ever you want to do with the value

                when (type) {
                    //add main category
                    ADD_MAIN_CATEGORY -> {
                        val mainCate = Category()
                        mainCate.Category_Name = editText.text.toString().trim()
                        mainCate.TransactionType_ID =
                            categoryManagerViewModel.mainCategory[0].TransactionType_ID
                        categoryManagerViewModel.addCategory(requireContext(), mainCate)
                        // refresh
                        refreshMainCategory()
                    }
                    //edit main category
                    EDIT_MAIN_CATEGORY -> {
                        val mainCate = categoryManagerViewModel.mainCategory[
                                categoryManagerViewModel.mainCategory.indexOfFirst { it.Category_ID == rID }]
                        mainCate.TransactionType_ID =
                            categoryManagerViewModel.mainCategory[0].TransactionType_ID
                        mainCate.Category_Name = editText.text.toString().trim()
                        categoryManagerViewModel.updateCategory(requireContext(), mainCate)
                        // refresh
                        refreshMainCategory()
                    }
                    //add sub category
                    ADD_SUB_CATEGORY -> {
                        val subCate = Category()
                        //subCate.Category_ID = categoryManagerViewModel.currentActiveMainCategory
                        subCate.Category_Name = editText.text.toString().trim()
                        subCate.Category_ParentID = categoryManagerViewModel.currentActiveMainCategory
                        subCate.TransactionType_ID = categoryManagerViewModel.currentTransactionType
                        categoryManagerViewModel.addCategory(requireContext(), subCate)
                        // refresh
                        refreshSubCategory()
                    }
                    //edit sub category
                    EDIT_SUB_CATEGORY -> {
                        val subCate = categoryManagerViewModel.subCategory[
                                categoryManagerViewModel.subCategory.indexOfFirst { it.Category_ID == rID }]
                        subCate.Category_Name = editText.text.toString().trim()
                        categoryManagerViewModel.updateCategory(requireContext(), subCate)
                        // refresh
                        refreshSubCategory()
                    }
                }

            }
            .setNegativeButton(R.string.msg_button_cancel
            ) { dialog, _ ->
                dialog.cancel()
            }

        // main category edit mode with delete button
            if (type == EDIT_MAIN_CATEGORY) {
                alert.setNeutralButton(R.string.msg_button_delete) { _, _ ->
                    deleteCategory(MAIN_CATEGORY, rID, nextRowID)
                }
            }

            alert.show()
    }


    // delete category---------------------
    // 0: mainCategory
    // 1: subCategory
    @SuppressLint("InflateParams")
    private fun deleteCategory(type: Int, rID: Long, nextRowID: Long = 0) {

        val dialogBuilder = AlertDialog.Builder(activity)

        dialogBuilder.setMessage(getText(R.string.msg_content_category_delete))
            .setCancelable(true)
            .setPositiveButton(getText(R.string.msg_button_confirm)) { _, _ ->
                // delete record
                try {
                    categoryManagerViewModel.deleteCategory(requireContext(), Category(rID))

                    when (type){
                        MAIN_CATEGORY -> {
                            mainCategoryAdapter?.setArrowAfterDelete()
                            // refresh
                            refreshMainCategory()
                            // show subcategory
                            if (rID != nextRowID) showSubCategoryItems(nextRowID)
                        }
                        SUB_CATEGORY -> {
                            refreshSubCategory()
                        }
                    }

                } catch (e: SQLiteException) {
                    Toast.makeText(
                        context,
                        getString(R.string.msg_category_delete_error),
                        Toast.LENGTH_SHORT
                    ).show()
                }


            }
            .setNegativeButton(getText(R.string.msg_button_cancel)) { dialog, _ ->
                // cancel
                dialog.cancel()
            }

        // set Title Style
        val titleView = layoutInflater.inflate(R.layout.popup_title,null)
        // set Title Text
        titleView.findViewById<TextView>(R.id.tv_popup_title_text).text = getText(R.string.msg_Title_prompt)

        val alert = dialogBuilder.create()
        //alert.setIcon(R.drawable.ic_baseline_delete_forever_24)
        alert.setCustomTitle(titleView)
        alert.show()
    }

    // refresh subCategory
    private fun refreshSubCategory(){
        subCategoryAdapter?.setList(
            categoryManagerViewModel.getSubCategory(
                requireContext(),
                categoryManagerViewModel.currentActiveMainCategory,
                cateMode
            )
        )
    }
    // refresh MainCategory
    private fun refreshMainCategory(){
        categoryManagerViewModel.loadMainCategory(requireContext(),cateMode, categoryManagerViewModel.currentTransactionType)
        mainCategoryAdapter?.setList(
            categoryManagerViewModel.mainCategory
        )
        //recyclerview_category_main.adapter = mainCategoryAdapter
    }

    private fun backToRecordFragment(cateID: Long){
        // pass the string back to record fragment
        setFragmentResult(
            KEY_RECORD_CATEGORY,
            bundleOf(KEY_RECORD_CATEGORY_ID to cateID)
        )
        requireActivity().onBackPressedDispatcher.onBackPressed()
    }
}