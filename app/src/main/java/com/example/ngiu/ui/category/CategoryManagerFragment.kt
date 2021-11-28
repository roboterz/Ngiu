package com.example.ngiu.ui.category

import android.app.AlertDialog
import android.content.DialogInterface
import android.database.sqlite.SQLiteException
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ngiu.MainActivity
import com.example.ngiu.R
import com.example.ngiu.data.AppDatabase
import com.example.ngiu.data.entities.MainCategory
import com.example.ngiu.data.entities.SubCategory
import com.example.ngiu.databinding.FragmentCategoryManageBinding
import kotlinx.android.synthetic.main.fragment_category_manage.*
import kotlinx.android.synthetic.main.fragment_record.toolbar_record
import kotlinx.android.synthetic.main.popup_title.view.*

class CategoryManagerFragment: Fragment() {
    private lateinit var categoryManagerViewModel: CategoryManagerViewModel
    private var _binding: FragmentCategoryManageBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private var mainCategoryAdapter: MainCategoryAdapter? = null
    private var subCategoryAdapter: SubCategoryAdapter? = null

    private var receiveID: Long = 0L

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
        setFragmentResultListener("category_transaction_type") { _, bundle ->

            receiveID = bundle.getLong("transaction_type")

            categoryManagerViewModel.currentTransactionType = receiveID
            categoryManagerViewModel.loadMainCategory(requireContext(), receiveID)

            if ( receiveID > 0) toolbar_record.menu.findItem(R.id.action_edit).isVisible = false
        }



        // Main Category Adapter
        Thread {
            this.activity?.runOnUiThread {

                recyclerview_category_main.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL,false)

                mainCategoryAdapter = this.context?.let {
                    MainCategoryAdapter(object: MainCategoryAdapter.OnClickListener {
                        // catch the item click event from adapter
                        override fun onItemClick(rID: Long) {
                            showSubCategoryItems(rID)
                        }
                    })
                }
                recyclerview_category_main.adapter = mainCategoryAdapter
            }
        }.start()

        // Sub Category Adapter
        Thread {
            this.activity?.runOnUiThread {

                recyclerview_category_sub.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL,false)

                subCategoryAdapter = this.context?.let {
                    SubCategoryAdapter(object: SubCategoryAdapter.OnClickListener {
                        // catch the item click event from adapter
                        override fun onItemClick(rID: Long, subCategoryName: String) {

                            // select mode
                            if (receiveID > 0){
                                // pass the string back to record fragment
                                setFragmentResult("selected_category", bundleOf("subCategory_Name" to subCategoryName))
                                // exit
                                requireActivity().onBackPressed()

                            // edit mode
                            }else {
                                // edit sub category
                                manageCategory(3, rID, subCategoryName)
                            }
                        }

                        // long click: delete
                        override fun onItemLongClick(rID: Long) {
                            if (receiveID > 0) {
                                // delete sub category
                                deleteCategory(1, rID)
                            }
                        }

                        // star click: change status
                        override fun onStarClick(rID: Long, commonValue: Boolean) {
                            // save status
                            val subCate = categoryManagerViewModel.subCategory[
                                    categoryManagerViewModel.subCategory.indexOfFirst { it.SubCategory_ID == rID }
                            ]
                            subCate.SubCategory_Common = commonValue

                            AppDatabase.getDatabase(requireContext()).subcat().updateSubCategory(subCate)
                            // refresh
                            refreshSubCategory()
                        }
                    })
                }
                recyclerview_category_sub.adapter = subCategoryAdapter
            }
        }.start()

    }



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        categoryManagerViewModel = ViewModelProvider(this).get(CategoryManagerViewModel::class.java)
        _binding = FragmentCategoryManageBinding.inflate(inflater, container, false)


        // load data
        categoryManagerViewModel.loadMainCategory(requireContext(),1L)


        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        //---------------------------tool bar--------------------------------
        // choose items to show
        toolbar_record.menu.findItem(R.id.action_edit).isVisible = true

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
                    //setFragmentResult("selected_category", bundleOf("subCategory_Name" to subCategoryName))
                    // call back button event to switch to previous fragment
                    requireActivity().onBackPressed()
                    true
                }

                else -> super.onOptionsItemSelected(it)
            }
        }
        //---------------------------tool bar--------------------------------


        // subCategory add button
        tv_sub_category_add.setOnClickListener {
            manageCategory(2)
        }

        // mainCategory add button
        tv_main_category_add.setOnClickListener {
            manageCategory(0)
        }

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
                subCategoryAdapter?.setList(categoryManagerViewModel.getSubCategory(requireContext(),categoryManagerViewModel.currentActiveMainCategory))
            }
        }.start()
    }

    override fun onDestroyView() {
        super.onDestroyView()

        //setFragmentResult("selected_category", bundleOf("subCategory_Name" to sendString))

        //(activity as MainActivity).setNavBottomBarVisibility(View.VISIBLE)
        _binding = null
    }



    //------------------------------------------Private Function------------------------------------

    private fun showSubCategoryItems(rID: Long) {

        // main category item click
        mainCategoryAdapter?.setList(categoryManagerViewModel.mainCategory)
        recyclerview_category_main.adapter = mainCategoryAdapter
        // show sub category
        subCategoryAdapter?.setList(categoryManagerViewModel.getSubCategory(requireContext(),rID))

        categoryManagerViewModel.currentActiveMainCategory = rID

        tv_sub_category_add.visibility = if (rID == 0L) View.GONE else View.VISIBLE
    }


    // add/edit main and sub category------------------
    // 0: add main category
    // 1: edit main category
    // 2: add sub category
    // 3: edit sub category
    private fun manageCategory(type: Int, rID: Long = 0L, string: String ="") {
        val alert = AlertDialog.Builder(context)
        val editText = EditText(activity)
        val titleView = View.inflate(context, R.layout.popup_title, null)

        editText.isSingleLine = true
        //editText.imeOptions = EditorInfo.IME_ACTION_DONE

        when (type) {
            1, 3 -> {
                editText.setText(string)
                titleView.tv_popup_title_text.text = getString(R.string.msg_edit_category)
            }
            0,2 -> {
                titleView.tv_popup_title_text.text = getString(R.string.msg_new_category)
            }
        }


        alert.setView(editText)
            .setCustomTitle(titleView)
            .setPositiveButton(R.string.msg_button_confirm,
                DialogInterface.OnClickListener { dialog, whichButton -> //What ever you want to do with the value

                    when (type){
                        0 -> {
                            val mainCate = MainCategory()
                            mainCate.MainCategory_Name = editText.text.toString()
                            mainCate.TransactionType_ID = categoryManagerViewModel.mainCategory[0].TransactionType_ID
                            AppDatabase.getDatabase(requireContext()).mainCategory().addMainCategory(mainCate)
                            // refresh
                            refreshMainCategory()
                        }
                        1 -> {
                            val mainCate = categoryManagerViewModel.mainCategory[
                                    categoryManagerViewModel.mainCategory.indexOfFirst { it.MainCategory_ID == rID }]
                            mainCate.TransactionType_ID = categoryManagerViewModel.mainCategory[0].TransactionType_ID
                            mainCate.MainCategory_Name = editText.text.toString()
                            AppDatabase.getDatabase(requireContext()).mainCategory().updateMainCategory(mainCate)
                            // refresh
                            refreshMainCategory()
                        }
                        2 -> {
                            val subCate = SubCategory()
                            subCate.MainCategory_ID = categoryManagerViewModel.currentActiveMainCategory
                            subCate.SubCategory_Name = editText.text.toString()
                            AppDatabase.getDatabase(requireContext()).subcat().addSubCategory(subCate)
                            // refresh
                            refreshSubCategory()
                        }
                        3 -> {
                            val subCate = categoryManagerViewModel.subCategory[
                                    categoryManagerViewModel.subCategory.indexOfFirst { it.SubCategory_ID == rID }]
                            subCate.SubCategory_Name = editText.text.toString()
                            AppDatabase.getDatabase(requireContext()).subcat().updateSubCategory(subCate)
                            // refresh
                            refreshSubCategory()
                        }
                    }

                })
            .setNegativeButton(R.string.msg_button_cancel,
                DialogInterface.OnClickListener { dialog, whichButton ->
                    // do nothing
                })
            .show()
    }


    // delete category---------------------
    // 0: mainCategory
    // 1: subCategory
    private fun deleteCategory(type: Int, rID: Long) {

        val dialogBuilder = AlertDialog.Builder(activity)

        dialogBuilder.setMessage(getText(R.string.msg_content_category_delete))
            .setCancelable(true)
            .setPositiveButton(getText(R.string.msg_button_confirm),DialogInterface.OnClickListener{ _,_->
                // delete record
                when (type){
                    0 -> {
                        try {
                            AppDatabase.getDatabase(requireContext()).mainCategory()
                                .deleteMainCategory(MainCategory(rID))
                        } catch (e: SQLiteException) {
                            Toast.makeText(context, getString(R.string.msg_category_delete_error), Toast.LENGTH_SHORT).show()
                        }
                        refreshMainCategory()
                    }
                    1 -> {
                        try {
                            AppDatabase.getDatabase(requireContext()).subcat().deleteSubCategory(SubCategory(rID))
                        } catch (e: SQLiteException) {
                            Toast.makeText(context, getString(R.string.msg_category_delete_error), Toast.LENGTH_SHORT).show()
                        }
                        // refresh
                        refreshSubCategory()
                    }
                }

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

    // refresh subCategory
    private fun refreshSubCategory(){
        subCategoryAdapter?.setList(
            categoryManagerViewModel.getSubCategory(
                requireContext(),
                categoryManagerViewModel.currentActiveMainCategory
            )
        )
    }
    // refresh MainCategory
    private fun refreshMainCategory(){
        categoryManagerViewModel.loadMainCategory(requireContext(), categoryManagerViewModel.currentTransactionType)
        mainCategoryAdapter?.setList(
            categoryManagerViewModel.mainCategory
        )
    }
}