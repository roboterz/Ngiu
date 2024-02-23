package com.aerolite.ngiu.ui.setting.mppmanage

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.database.sqlite.SQLiteException
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.setPadding
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aerolite.ngiu.MainActivity
import com.aerolite.ngiu.R
import com.aerolite.ngiu.data.AppDatabase
import com.aerolite.ngiu.data.entities.Merchant
import com.aerolite.ngiu.functions.MPP_MERCHANT
import com.aerolite.ngiu.functions.MPP_PERSON
import com.aerolite.ngiu.functions.MPP_PROJECT
import com.aerolite.ngiu.data.entities.Person
import com.aerolite.ngiu.data.entities.Project
import com.aerolite.ngiu.databinding.FragmentMerchantPersonProjectManageBinding

//import kotlinx.android.synthetic.main.fragment_merchant_person_project_manage.*
//import kotlinx.android.synthetic.main.popup_title.view.*

class MPPManagerFragment: Fragment() {
    private lateinit var mppManagerViewModel: MPPManagerViewModel
    private var _binding: FragmentMerchantPersonProjectManageBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private var mppAdapter: MPPAdapter? = null

    private var receiveTypeID: Int = 0
    // Type:
    // 0: Merchant
    // 1: Person
    // 2: Project

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // hide bottom bar
        (activity as MainActivity).setNavBottomBarVisibility(View.GONE)


        // receive data from other fragment
        setFragmentResultListener("mpp_type") { _, bundle ->

            receiveTypeID = bundle.getInt("type_ID")

            mppManagerViewModel.typeID = receiveTypeID
            mppManagerViewModel.loadData(requireContext(), receiveTypeID)

        }



        // Main Category Adapter
        Thread {
            this.activity?.runOnUiThread {

                binding.recyclerviewMpp.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL,false)

                mppAdapter = this.context?.let {
                    MPPAdapter(object: MPPAdapter.OnClickListener {
                        // catch the item click event from adapter
                        override fun onItemClick(rID: Long, itemName: String, addNew: Boolean) {
                            //
                            openMSGWindow(requireContext(), rID, itemName,addNew)
                        }
                    })
                }
                binding.recyclerviewMpp.adapter = mppAdapter
            }
        }.start()


    }



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mppManagerViewModel = ViewModelProvider(this).get(MPPManagerViewModel::class.java)
        _binding = FragmentMerchantPersonProjectManageBinding.inflate(inflater, container, false)


        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        //---------------------------tool bar--------------------------------
        // choose items to show
        //toolbar_category.menu.findItem(R.id.action_edit).isVisible = true

        // click the navigation Icon in the left side of toolbar
        binding.toolbarMpp.setNavigationOnClickListener(View.OnClickListener {

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



    }



    override fun onResume() {
        super.onResume()

        // Main Category Adapter
        Thread {
            this.activity?.runOnUiThread {
                mppAdapter?.setList(mppManagerViewModel.itemList)
            }
        }.start()



        // show title
        when (receiveTypeID){
            MPP_MERCHANT -> binding.toolbarMpp.setTitle(R.string.nav_title_merchant_manage)
            MPP_PERSON -> binding.toolbarMpp.setTitle(R.string.nav_title_person_manage)
            MPP_PROJECT -> binding.toolbarMpp.setTitle(R.string.nav_title_project_manage)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()

        //setFragmentResult("selected_category", bundleOf("subCategory_Name" to sendString))

        //(activity as MainActivity).setNavBottomBarVisibility(View.VISIBLE)
        _binding = null
    }



    //------------------------------------------Private Function------------------------------------


    // add/edit item------------------
    private fun openMSGWindow(context: Context, rID: Long = 0L, itemName: String = "", addNew: Boolean = false) {

        val editText = EditText(activity)
        val titleView = View.inflate(context, R.layout.popup_title, null)

        editText.isSingleLine = true
        editText.setPadding(50, 50, 50, 30)
        //editText.imeOptions = EditorInfo.IME_ACTION_DONE

        if (addNew) {
            titleView.findViewById<TextView>(R.id.tv_popup_title_text).text = getString(R.string.msg_new_name)
        }
        else {
            editText.setText(itemName)
            titleView.findViewById<TextView>(R.id.tv_popup_title_text).text = getString(R.string.msg_edit_name)
        }


        val alert = AlertDialog.Builder(context)
            .setView(editText)
            .setCustomTitle(titleView)
            .setPositiveButton(R.string.msg_button_confirm,
                DialogInterface.OnClickListener { dialog, whichButton -> //What ever you want to do with the value
                    // add | edit
                    if (addNew){
                        addItem(editText.text.toString())
                    }else{
                        editItem(rID, editText.text.toString())
                    }
                })
            .setNegativeButton(R.string.msg_button_cancel,
                DialogInterface.OnClickListener { dialog, whichButton ->
                    dialog.cancel()
                })

            // Delete
            if (!addNew) {
                alert.setNeutralButton(R.string.msg_button_delete,DialogInterface.OnClickListener{ dialog, whichButton ->

                    deleteItem(context, rID)
                })
            }

        val dialog = alert.create()
        dialog.show()

        // button text color
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(context, R.color.app_button_text_highlight))
        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(ContextCompat.getColor(context, R.color.app_button_text))
        dialog.getButton(AlertDialog.BUTTON_NEUTRAL).setTextColor(ContextCompat.getColor(context, R.color.app_button_text))
    }

    // edit
    // todo name must be unique
    private fun editItem(rID: Long, itemName: String) {
        try {
            when (mppManagerViewModel.typeID) {
                MPP_MERCHANT -> AppDatabase.getDatabase(requireContext()).merchant().addMerchant( Merchant(rID, itemName) )
                MPP_PERSON -> AppDatabase.getDatabase(requireContext()).person().addPerson( Person(rID,itemName) )
                MPP_PROJECT -> AppDatabase.getDatabase(requireContext()).project().addProject( Project(rID, itemName) )
            }
            refreshList()

        } catch (e: SQLiteException) {
            Toast.makeText(context, getString(R.string.msg_name_edit_error), Toast.LENGTH_SHORT).show()
        }
    }

    // add
    // todo name must be unique
    private fun addItem(itemName: String) {
        try {
            when (mppManagerViewModel.typeID) {
                MPP_MERCHANT -> AppDatabase.getDatabase(requireContext()).merchant().addMerchant( Merchant(0L, itemName) )
                MPP_PERSON -> AppDatabase.getDatabase(requireContext()).person().addPerson( Person(0L,itemName) )
                MPP_PROJECT -> AppDatabase.getDatabase(requireContext()).project().addProject( Project(0L, itemName) )
            }
            refreshList()

        } catch (e: SQLiteException) {
            Toast.makeText(context, getString(R.string.msg_name_add_error), Toast.LENGTH_SHORT).show()
        }
    }


    // delete Item---------------------
    // Type:
    // 0: Merchant
    // 1: Person
    // 2: Project
    private fun deleteItem(context: Context, rID: Long) {

        // set Title Style
        val titleView = layoutInflater.inflate(R.layout.popup_title,null)
        // set Title Text
        titleView.findViewById<TextView>(R.id.tv_popup_title_text).text = getText(R.string.msg_Title_prompt)

        val dialogBuilder = AlertDialog.Builder(activity)
                        .setMessage(getText(R.string.msg_content_name_delete))
                        .setCancelable(true)
                        .setPositiveButton(getText(R.string.msg_button_confirm),DialogInterface.OnClickListener{ _,_->
                            // delete
                            try {
                                when (mppManagerViewModel.typeID) {
                                    MPP_MERCHANT -> AppDatabase.getDatabase(requireContext()).merchant().deleteMerchant( Merchant(rID, "") )
                                    MPP_PERSON -> AppDatabase.getDatabase(requireContext()).person().deletePerson( Person(rID,"") )
                                    MPP_PROJECT -> AppDatabase.getDatabase(requireContext()).project().deleteProject( Project(rID, "") )
                                }
                                refreshList()

                            } catch (e: SQLiteException) {
                                Toast.makeText(context, getString(R.string.msg_name_delete_error), Toast.LENGTH_SHORT).show()
                            }

                        })
                        .setNegativeButton(getText(R.string.msg_button_cancel),DialogInterface.OnClickListener{ dialog, _ ->
                            // cancel
                            dialog.cancel()
                        })
                        .create()


        // title view
        dialogBuilder.setCustomTitle(titleView)
        // show dialog
        dialogBuilder.show()

        // button text color
        dialogBuilder.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(context, R.color.app_button_text_highlight))
        dialogBuilder.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(ContextCompat.getColor(context, R.color.app_button_text))
    }


    // refresh list
    private fun refreshList(){
        mppManagerViewModel.loadData(requireContext(),mppManagerViewModel.typeID)
        mppAdapter?.setList(mppManagerViewModel.itemList)
        //recyclerview_category_main.adapter = mainCategoryAdapter
    }
}