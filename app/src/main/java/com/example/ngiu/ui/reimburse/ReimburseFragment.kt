package com.example.ngiu.ui.reimburse

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import com.example.ngiu.MainActivity
import com.example.ngiu.R
import com.example.ngiu.databinding.FragmentReimburseBinding
import com.example.ngiu.functions.SelectItem
import com.example.ngiu.functions.popupWindow
import com.example.ngiu.functions.switchToRecordFragment
import kotlinx.android.synthetic.main.fragment_reimburse.btn_reimburse_claim
import kotlinx.android.synthetic.main.fragment_reimburse.cb_reimburse_select_all
import kotlinx.android.synthetic.main.fragment_reimburse.recyclerview_reimburse
import kotlinx.android.synthetic.main.fragment_reimburse.toolbar_reimburse
import kotlinx.android.synthetic.main.popup_claim_dialog.tv_claim_account
import kotlinx.android.synthetic.main.popup_claim_dialog.tv_claim_amount
import kotlinx.android.synthetic.main.popup_claim_dialog.tv_claim_info
import kotlinx.android.synthetic.main.popup_reminder_dialog.button_left
import kotlinx.android.synthetic.main.popup_reminder_dialog.button_right
import java.time.format.DateTimeFormatter


class ReimburseFragment : Fragment() {


    private lateinit var reimburseViewModel: ReimburseViewModel
    private var _binding: FragmentReimburseBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private var reimburseAdapter: ReimburseAdapter? = null
    private var categoryID: Long = 0L
    private var startDate: String =""
    private var endDate: String =""

    private val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-01")
    private val dateMonthFormatter = DateTimeFormatter.ofPattern("yyyy-MM")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        (activity as MainActivity).setNavBottomBarVisibility(View.GONE)


    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ):View? {
        reimburseViewModel =
            ViewModelProvider(this)[ReimburseViewModel::class.java]

        _binding = FragmentReimburseBinding.inflate(inflater, container, false)




        /** Load Data **/
        reimburseViewModel.loadDataToRam(requireContext())

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        /** init Adapter **/
        initAdapter(view, this)

        // toolbar title
        //toolbar_report_detail.setTitle(reportDetailViewModel.getCategoryNameByID(categoryID).)
        // show add button

        // toolbar menu item clicked

        /** click Go Back Icon in the left side of toolbar **/
        toolbar_reimburse.setNavigationOnClickListener {
            // call back button event to switch to previous fragment
            //requireActivity().onBackPressed()

            NavHostFragment.findNavController(this).navigateUp()
        }

        /** Select All  **/
        cb_reimburse_select_all.setOnCheckedChangeListener { buttonView, isChecked ->
            reimburseViewModel.setAllReimburseStatus(isChecked)
            refreshAdapter()
        }

        /** Claim **/
        btn_reimburse_claim.setOnClickListener {
            val num = reimburseViewModel.getCountOfReimbursed()

            if (num >0) {
                showClaimDialog(view.context, num, reimburseViewModel.getSumOfReimbursed())
            }
        }
    }





    override fun onResume() {
        super.onResume()

        refreshAdapter()

    }


    override fun onDestroyView() {
        super.onDestroyView()


        _binding = null
    }



    /**
     *  ************* Private Functions *****************
     */

    private fun initAdapter(view: View, fragment: Fragment){
        /** Report Adapter **/
        Thread {
            this.activity?.runOnUiThread {

                recyclerview_reimburse.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL,false)
                reimburseAdapter = this.context?.let {
                    ReimburseAdapter(object: ReimburseAdapter.OnClickListener {
                        // catch the item click event from adapter
                        override fun onItemClick(transID: Long) {
                            // switch to record fragment (Edit mode)
                            //navigateToRecordFragment(transID)
                            switchToRecordFragment(view, fragment, transID)

                        }

                        override fun onItemSelect(transID: Long, selected: Boolean) {
                            // set reimburse status
                            reimburseViewModel.setReimburseStatus(transID, selected)
                        }
                    })
                }
                recyclerview_reimburse.adapter = reimburseAdapter
            }
        }.start()
    }

    private fun refreshAdapter(){
        /** load Adapter Data **/
        Thread {
            this.activity?.runOnUiThread {
                reimburseAdapter?.setList(reimburseViewModel.getListDetail())
                recyclerview_reimburse.adapter = reimburseAdapter
            }
        }.start()
    }



    /** Claim Window **/
    @SuppressLint("CutPasteId", "SetTextI18n")
    private fun showClaimDialog(context: Context, count: Int, sumOfReimbursed: Double){


        val dialog = MaterialDialog(context)
            .noAutoDismiss()
            .customView(R.layout.popup_claim_dialog, noVerticalPadding = true)

        /** Set info **/
        dialog.tv_claim_info.text =getString(R.string.msg_total) + " " + count.toString() + " " + getString(R.string.msg_transaction) + "."

        /** Set Amount **/
        dialog.tv_claim_amount.text =  "%.2f".format(sumOfReimbursed)

        /** Set Account Name **/
        dialog.tv_claim_account.text = reimburseViewModel.getFirstAccountName()


        /** Select Account **/
        dialog.tv_claim_account.setOnClickListener {
            val nameList: Array<String> = reimburseViewModel.getListOfAccountName()
            popupWindow(requireContext(),getString(R.string.nav_title_account_list),  nameList,
                object : SelectItem {
                    override fun clicked(idx: Int) {
                        dialog.tv_claim_account.text = nameList[idx]
                        reimburseViewModel.setAccountIdx(idx)
                    }
                })
        }


        /** Cancel Button click **/
        dialog.button_left.setOnClickListener(){

            // cancel button
            dialog.dismiss()
        }


        /** Confirm Button click **/
        dialog.button_right.setOnClickListener(){
            // save
            reimburseViewModel.saveListDetail(requireContext(), sumOfReimbursed, dialog.tv_claim_amount.text.toString().toDouble(), count)

            Toast.makeText(
                context,
                getString(R.string.msg_completed),
                Toast.LENGTH_SHORT
            ).show()

            // close window
            dialog.dismiss()

            //refresh
            cb_reimburse_select_all.isChecked = false
            reimburseViewModel.loadDataToRam(requireContext())
            refreshAdapter()

        }


        dialog.show()
    }

}