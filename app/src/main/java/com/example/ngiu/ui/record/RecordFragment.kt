package com.example.ngiu.ui.record


import android.os.Bundle
import android.os.Looper
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import android.widget.EditText
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.ngiu.MainActivity
import com.example.ngiu.R
import com.example.ngiu.data.entities.SubCategory
import com.example.ngiu.data.entities.Trans
import com.example.ngiu.data.entities.returntype.TransactionDetail
import com.example.ngiu.databinding.FragmentRecordBinding
import kotlinx.android.synthetic.main.fragment_activity.*
import kotlinx.android.synthetic.main.fragment_record.*
import java.util.*
import kotlin.collections.ArrayList
import com.example.ngiu.functions.MyFunctions
import com.example.ngiu.functions.addDecimalLimiter
import java.time.Instant.now


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
            }
        }


        Thread {

            activity?.runOnUiThread {

                // pass the value to fragment from adapter when item clicked
                vpAdapter =
                    this.context?.let {
                        RecordCategoryAdapter(object : RecordCategoryAdapter.OnClickListener {

                            // catch the item click event from adapter
                            override fun onItemClick(string: String) {
                                // do something after clicked
                                tv_record_category.text = string

                            }
                        })
                    }
                // load viewpager2 adapter
                vp_record_category.adapter = vpAdapter
            }
        }.start()


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
        val root: View = binding.root

        //



        return root
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

                    // call back button event to switch to previous fragment
                    requireActivity().onBackPressed()

                    true
                }
                R.id.action_delete -> {
                    // todo delete record

                    true
                }
                else -> super.onOptionsItemSelected(it)
            }
        }


        //
    }


    // called when the fragment is visible and actively running.
    override fun onResume() {
        super.onResume()

        if (receivedID > 0) {
            getTransactionRecord(receivedID)

            recordViewModel.currentRowID = receivedID
            receivedID = 0
        }else{
            setStatus( recordViewModel.setTransactionType(1) )
            loadCommonCategory( recordViewModel.currentTransactionType.currentTyID )
        }
    }


    //
    private fun getTransactionRecord( rID: Long) {
        var trans: Trans = Trans(
            0, 0, 0, 0, 0, 0.0,
            Date(), 0, 0, "", 0, 0, 0
        )
        var subCategory: SubCategory = SubCategory(0, 0, "", false)

        var td = TransactionDetail(0,"","","","",0.00, Date(),"","","","",0)

        Thread {
            //Looper.prepare()
            //trans = recordViewModel.getOneTrans(activity, rID)
            //subCategory = recordViewModel.getOneSubCategory(activity, trans.SubCategory_ID)
            td = recordViewModel.getOneTransactionDetail(activity, rID)


            //setStatus(recordViewModel.setTransactionType(trans.TransactionType_ID.toInt()))

            //activity?.runOnUiThread {
                tv_record_category.text = td.SubCategory_Name
                tv_record_amount.setText( td.Transaction_Amount.toString())

                //}
                //Looper.loop()



            //}
        }.start()

        loadCommonCategory(
            recordViewModel.currentTransactionType.currentTyID,
            td.SubCategory_Name
        )
    }


    //
    private fun loadCommonCategory( tyID: Int, categoryString: String = "") {

        val records = ArrayList<String>()

        Thread {

            val commonCateList = recordViewModel.readCommonCategory(activity, tyID)

            activity?.runOnUiThread {
                for (i in commonCateList.indices) {
                    records.add(commonCateList[i].SubCategory_Name)
                }

                vpAdapter?.setList(records)
                vpAdapter?.setCategoryString(categoryString)
                vpAdapter?.setCategoryString(categoryString)

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
    }

    private fun switchPage(){

    }



    override fun onDestroyView() {
        super.onDestroyView()
        (activity as MainActivity).setNavBottomBarVisibility(View.VISIBLE)
        _binding = null
        //vpAdapter = null
    }



}



