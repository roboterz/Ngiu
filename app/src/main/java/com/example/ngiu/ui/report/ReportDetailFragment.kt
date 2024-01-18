package com.example.ngiu.ui.report

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ngiu.MainActivity
import com.example.ngiu.databinding.FragmentReportDetailBinding
import com.example.ngiu.functions.KEY_REPORT
import com.example.ngiu.functions.KEY_REPORT_DETAIL
import com.example.ngiu.functions.KEY_REPORT_DETAIL_CATEGORY_ID
import com.example.ngiu.functions.KEY_REPORT_DETAIL_END
import com.example.ngiu.functions.KEY_REPORT_DETAIL_START
import com.example.ngiu.functions.KEY_REPORT_END
import com.example.ngiu.functions.KEY_REPORT_START
import com.example.ngiu.functions.KEY_REPORT_TYPE
import com.example.ngiu.functions.switchToRecordFragment
//import kotlinx.android.synthetic.main.fragment_report_detail.recyclerview_report_detail
//import kotlinx.android.synthetic.main.fragment_report_detail.toolbar_report_detail
import java.time.format.DateTimeFormatter


class ReportDetailFragment : Fragment() {


    private lateinit var reportDetailViewModel: ReportDetailViewModel
    private var _binding: FragmentReportDetailBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private var reportDetailAdapter: ReportDetailAdapter? = null
    private var categoryID: Long = 0L
    private var startDate: String =""
    private var endDate: String =""

    private val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-01")
    private val dateMonthFormatter = DateTimeFormatter.ofPattern("yyyy-MM")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        (activity as MainActivity).setNavBottomBarVisibility(View.GONE)

        /** receive data from other fragment **/
        setFragmentResultListener(KEY_REPORT_DETAIL) { _, bundle ->
            categoryID = bundle.getLong(KEY_REPORT_DETAIL_CATEGORY_ID)
            startDate = bundle.getString(KEY_REPORT_DETAIL_START).toString()
            endDate = bundle.getString(KEY_REPORT_DETAIL_END).toString()


            //Toast.makeText(context,"".toString(),Toast.LENGTH_LONG).show()
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ):View? {
        reportDetailViewModel =
            ViewModelProvider(this)[ReportDetailViewModel::class.java]

        _binding = FragmentReportDetailBinding.inflate(inflater, container, false)




        /** Load Data **/
        //reportViewModel.loadData(requireContext(), TRANSACTION_TYPE_INCOME, LocalDate.now().format(dateFormatter).toString(), LocalDate.now().plusMonths(1).format(dateFormatter).toString() )


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
        binding.toolbarReportDetail.setNavigationOnClickListener {
            // call back button event to switch to previous fragment
            //requireActivity().onBackPressed()
            switchToReportFragment(view, startDate, endDate)

            //NavHostFragment.findNavController(this).navigateUp()
        }



    }



    /** Override Back Key Function **/
    override fun onAttach(context: Context) {
        super.onAttach(context)
        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    // Leave empty do disable back press
                    switchToReportFragment(
                        requireView(),
                        startDate,
                        endDate)
                }
            }
        requireActivity().onBackPressedDispatcher.addCallback(
            this,
            callback
        )
    }





    override fun onResume() {
        super.onResume()

        val list = reportDetailViewModel.getTransByCateID(requireContext(),categoryID, startDate, endDate)

        /** load Adapter Data **/
        Thread {
            this.activity?.runOnUiThread {
                reportDetailAdapter?.setList(list)
                binding.recyclerviewReportDetail.adapter = reportDetailAdapter
            }
        }.start()

        // toolbar title
        binding.toolbarReportDetail.title = reportDetailViewModel.getCategoryNameByID(requireContext(), categoryID)
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

                binding.recyclerviewReportDetail.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL,false)
                reportDetailAdapter = this.context?.let {
                    ReportDetailAdapter(object: ReportDetailAdapter.OnClickListener {
                        // catch the item click event from adapter
                        override fun onItemClick(transID: Long) {
                            // switch to record fragment (Edit mode)
                            //navigateToRecordFragment(transID)
                            switchToRecordFragment(view, fragment, transID)

                        }
                    })
                }
                binding.recyclerviewReportDetail.adapter = reportDetailAdapter
            }
        }.start()
    }

    fun switchToReportFragment(view: View,  startDate: String, endDate: String) {

        // Put Data Before switch
        setFragmentResult(
            KEY_REPORT, bundleOf(
                KEY_REPORT_TYPE to reportDetailViewModel.getReportTypeByCategoryID(requireContext(), categoryID),
                KEY_REPORT_START to startDate,
                KEY_REPORT_END to endDate
            )
        )

        // switch to category manage fragment
        //view.findNavController().navigate(R.id.navigation_report)
        NavHostFragment.findNavController(this).navigateUp()
    }

}

