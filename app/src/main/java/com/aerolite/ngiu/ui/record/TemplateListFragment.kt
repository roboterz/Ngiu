package com.aerolite.ngiu.ui.record

import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
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
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aerolite.ngiu.MainActivity
import com.aerolite.ngiu.R
import com.aerolite.ngiu.data.AppDatabase
import com.aerolite.ngiu.data.entities.Merchant
import com.aerolite.ngiu.data.entities.Person
import com.aerolite.ngiu.data.entities.Project
import com.aerolite.ngiu.databinding.FragmentTemplateListBinding
import com.aerolite.ngiu.functions.KEY_RECORD
import com.aerolite.ngiu.functions.KEY_RECORD_ACCOUNT_ID
import com.aerolite.ngiu.functions.KEY_RECORD_CATEGORY
import com.aerolite.ngiu.functions.KEY_RECORD_CATEGORY_ID
import com.aerolite.ngiu.functions.KEY_RECORD_TRANSACTION_ID
import com.aerolite.ngiu.functions.KEY_RECORD_TRANSACTION_TYPE_ID
import com.aerolite.ngiu.functions.KEY_TEMPLATE
import com.aerolite.ngiu.functions.KEY_TEMPLATE_ID


class TemplateListFragment: Fragment() {
    private lateinit var templateListViewModel: TemplateListViewModel
    private var _binding: FragmentTemplateListBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private var templateListAdapter: TemplateListAdapter? = null

    private var receiveTypeID: Int = 0
    // Type:
    // 0: Merchant
    // 1: Person
    // 2: Project

    // todo rewrite class

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // hide bottom bar
        (activity as MainActivity).setNavBottomBarVisibility(View.GONE)


//        // receive data from other fragment
//        setFragmentResultListener("mpp_type") { _, bundle ->
//
//            receiveTypeID = bundle.getInt("type_ID")
//
//            //templateListViewModel.typeID = receiveTypeID
//            //templateListViewModel.loadData(requireContext(), receiveTypeID)
//
//        }





    }



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        templateListViewModel = ViewModelProvider(this)[TemplateListViewModel::class.java]
        _binding = FragmentTemplateListBinding.inflate(inflater, container, false)


        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        //---------------------------tool bar--------------------------------
        // choose items to show
        //toolbar_category.menu.findItem(R.id.action_edit).isVisible = true

        // click the navigation Icon in the left side of toolbar
        binding.toolbarTemplate.title = getString(R.string.nav_title_template_list)

        binding.toolbarTemplate.setNavigationOnClickListener(View.OnClickListener {

            // call back button event to switch to previous fragment
            //requireActivity().onBackPressed()
            NavHostFragment.findNavController(this).navigateUp()
        })

        /*
        // menu item clicked
        toolbar_category.setOnMenuItemClickListener{
            when (it.itemId) {
                // done menu
                R.id.action_edit -> {
                    // turn on edit mode
                    //mainCategoryAdapter?.setEditMode(true)
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

         */
        //---------------------------tool bar--------------------------------


        // Template list Adapter
//        Thread {
//            this.activity?.runOnUiThread {

        binding.recyclerviewTemplate.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL,false)

        templateListAdapter = this.context?.let {
            TemplateListAdapter(object: TemplateListAdapter.OnClickListener {
                // catch the item click event from adapter
                override fun onItemClick(rID: Long) {
                    //
                    // todo switch to record fragment
                    navigateToRecordFragment(rID)
                }
            })
        }
        binding.recyclerviewTemplate.adapter = templateListAdapter
//            }
//        }.start()

        templateListViewModel.getAllRecords().observe(viewLifecycleOwner, Observer { it ->
            templateListAdapter?.setList(it)
        })


    }



    override fun onResume() {
        super.onResume()

        // Main Category Adapter
//        Thread {
//            this.activity?.runOnUiThread {
//                templateListAdapter?.setList(templateListViewModel.getAllRecords())
//            }
//        }.start()




    }

    override fun onDestroyView() {
        super.onDestroyView()

        //setFragmentResult("selected_category", bundleOf("subCategory_Name" to sendString))

        //(activity as MainActivity).setNavBottomBarVisibility(View.VISIBLE)
        _binding = null
    }



    //------------------------------------------Private Function------------------------------------


    // add/edit item------------------
    private fun openMSGWindow( rID: Long = 0L, itemName: String = "", addNew: Boolean = false) {
        val alert = AlertDialog.Builder(context)
        val editText = EditText(activity)
        val titleView = View.inflate(context, R.layout.popup_title, null)

        editText.isSingleLine = true
        //editText.imeOptions = EditorInfo.IME_ACTION_DONE

        if (addNew) {
            titleView.findViewById<TextView>(R.id.tv_popup_title_text).text = getString(R.string.msg_new_name)
        }
        else {
            editText.setText(itemName)
            titleView.findViewById<TextView>(R.id.tv_popup_title_text).text = getString(R.string.msg_edit_name)
        }


        alert.setView(editText)
            .setCustomTitle(titleView)
            .setPositiveButton(
                R.string.msg_button_confirm,
                DialogInterface.OnClickListener { dialog, whichButton -> //What ever you want to do with the value
                    // add | edit
                    if (addNew){
                        addItem(editText.text.toString())
                    }else{
                        editItem(rID, editText.text.toString())
                    }
                })
            .setNegativeButton(
                R.string.msg_button_cancel,
                DialogInterface.OnClickListener { dialog, whichButton ->
                    dialog.cancel()
                })

        // Delete
        if (!addNew) {
            alert.setNeutralButton(
                R.string.msg_button_delete,
                DialogInterface.OnClickListener{ dialog, whichButton ->
                deleteItem(rID)
            })
        }

        alert.show()
    }

    // edit
    // todo name must be unique
    private fun editItem(rID: Long, itemName: String) {
        try {
//            when (templateListViewModel.typeID) {
//                MPP_MERCHANT -> AppDatabase.getDatabase(requireContext()).merchant().addMerchant( Merchant(rID, itemName) )
//                MPP_PERSON -> AppDatabase.getDatabase(requireContext()).person().addPerson( Person(rID,itemName) )
//                MPP_PROJECT -> AppDatabase.getDatabase(requireContext()).project().addProject( Project(rID, itemName) )
//            }
            refreshList()

        } catch (e: SQLiteException) {
            Toast.makeText(context, getString(R.string.msg_name_edit_error), Toast.LENGTH_SHORT).show()
        }
    }

    // add
    // todo name must be unique
    private fun addItem(itemName: String) {
        try {
//            when (templateListViewModel.typeID) {
//                MPP_MERCHANT -> AppDatabase.getDatabase(requireContext()).merchant().addMerchant( Merchant(0L, itemName) )
//                MPP_PERSON -> AppDatabase.getDatabase(requireContext()).person().addPerson( Person(0L,itemName) )
//                MPP_PROJECT -> AppDatabase.getDatabase(requireContext()).project().addProject( Project(0L, itemName) )
//            }
            refreshList()

        } catch (e: SQLiteException) {
            Toast.makeText(context, getString(R.string.msg_name_add_error), Toast.LENGTH_SHORT).show()
        }
    }


    // delete Item---------------------

    private fun deleteItem(rID: Long) {

        val dialogBuilder = AlertDialog.Builder(activity)

        dialogBuilder.setMessage(getText(R.string.msg_content_name_delete))
            .setCancelable(true)
            .setPositiveButton(getText(R.string.msg_button_confirm),
                DialogInterface.OnClickListener{ _, _->
                // delete
                try {
//                    when (templateListViewModel.typeID) {
//                        MPP_MERCHANT -> AppDatabase.getDatabase(requireContext()).merchant().deleteMerchant( Merchant(rID, "") )
//                        MPP_PERSON -> AppDatabase.getDatabase(requireContext()).person().deletePerson( Person(rID,"") )
//                        MPP_PROJECT -> AppDatabase.getDatabase(requireContext()).project().deleteProject( Project(rID, "") )
//                    }
                    refreshList()

                } catch (e: SQLiteException) {
                    Toast.makeText(context, getString(R.string.msg_name_delete_error), Toast.LENGTH_SHORT).show()
                }

            })
            .setNegativeButton(getText(R.string.msg_button_cancel),
                DialogInterface.OnClickListener{ dialog, _ ->
                // cancel
                dialog.cancel()
            })

        // set Title Style
        val titleView = layoutInflater.inflate(R.layout.popup_title,null)
        // set Title Text
        titleView.findViewById<TextView>(R.id.tv_popup_title_text).text = getText(R.string.msg_Title_prompt)

        val alert = dialogBuilder.create()
        //alert.setIcon(R.drawable.ic_baseline_delete_forever_24)
        alert.setCustomTitle(titleView)
        alert.show()
    }


    // refresh list
    private fun refreshList(){
        //templateListViewModel.loadData(requireContext(),templateListViewModel.typeID)
        //templateListAdapter?.setList(templateListViewModel.itemList)
        //recyclerview_category_main.adapter = mainCategoryAdapter
    }

    private fun navigateToRecordFragment(templateID: Long) {
        setFragmentResult(
            KEY_TEMPLATE,
            bundleOf(KEY_TEMPLATE_ID to templateID)
        )
        //findNavController().popBackStack()
        findNavController().navigate(R.id.navigation_record)
    }
}