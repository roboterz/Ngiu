package com.aerolite.ngiu.ui.report

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aerolite.ngiu.MainActivity
import com.aerolite.ngiu.R
import com.aerolite.ngiu.databinding.FragmentReportBinding
import com.aerolite.ngiu.functions.KEY_REPORT
import com.aerolite.ngiu.functions.KEY_REPORT_DETAIL
import com.aerolite.ngiu.functions.KEY_REPORT_DETAIL_CATEGORY_ID
import com.aerolite.ngiu.functions.KEY_REPORT_DETAIL_END
import com.aerolite.ngiu.functions.KEY_REPORT_DETAIL_START
import com.aerolite.ngiu.functions.KEY_REPORT_END
import com.aerolite.ngiu.functions.KEY_REPORT_START
import com.aerolite.ngiu.functions.KEY_REPORT_TYPE
import com.aerolite.ngiu.functions.TRANSACTION_TYPE_EXPENSE
import com.aerolite.ngiu.functions.TRANSACTION_TYPE_INCOME
import com.aerolite.ngiu.functions.chart.CategoryAmount
import com.aerolite.ngiu.functions.chart.CircleChartView
import com.aerolite.ngiu.functions.chart.PieData
//import kotlinx.android.synthetic.main.fragment_calendar.*
//import kotlinx.android.synthetic.main.fragment_report.*
//import kotlinx.android.synthetic.main.fragment_setting.*
import java.time.LocalDate
import java.time.format.DateTimeFormatter


class ReportFragment : Fragment() {


    private lateinit var reportViewModel: ReportViewModel
    private var _binding: FragmentReportBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private var reportAdapter: ReportAdapter? = null


    private val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-01")
    private val dateMonthFormatter = DateTimeFormatter.ofPattern("yyyy-MM")

    private var startDate: String =""
    private var endDate: String =""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        (activity as MainActivity).setNavBottomBarVisibility(View.GONE)

        /** receive data from other fragment **/
        setFragmentResultListener(KEY_REPORT) { _, bundle ->
            startDate = bundle.getString(KEY_REPORT_START).toString()
            endDate = bundle.getString(KEY_REPORT_END).toString()

            val transType = bundle.getLong(KEY_REPORT_TYPE)
            setReportType(transType)
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ):View? {
        reportViewModel =
            ViewModelProvider(this)[ReportViewModel::class.java]

        _binding = FragmentReportBinding.inflate(inflater, container, false)




        /** Load Data **/
        if (startDate == "") {
            startDate = LocalDate.now().format(dateFormatter).toString()
            endDate = LocalDate.now().plusMonths(1).format(dateFormatter).toString()
        }


        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        /** Report Adapter **/
        Thread {
            this.activity?.runOnUiThread {

                binding.recyclerviewReport.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL,false)
                reportAdapter = this.context?.let {
                    ReportAdapter(object: ReportAdapter.OnClickListener {
                        // catch the item click event from adapter
                        override fun onItemClick(categoryID: Long) {

                            // switch to report detail fragment
                            switchToReportDetailFragment(view, categoryID, reportViewModel.getCurrentMonthStart(), reportViewModel.getCurrentMonthEnd())
                        }
                    })
                }

                binding.recyclerviewReport.adapter = reportAdapter
            }
        }.start()


        // toolbar title
        binding.toolbarReport.setTitle(R.string.nav_title_report)
        // show add button

        // toolbar menu item clicked

        /** click Go Back Icon in the left side of toolbar **/
        binding.toolbarReport.setNavigationOnClickListener {
            // call back button event to switch to previous fragment
            //requireActivity().onBackPressed()
            NavHostFragment.findNavController(this).navigateUp()
        }


        binding.tvReportIncome.setOnClickListener{
            setReportType(TRANSACTION_TYPE_INCOME)
            showData(view.context, reportViewModel.getCurrentMonthStart(), reportViewModel.getCurrentMonthEnd())
        }
        binding.tvReportExpense.setOnClickListener {
            setReportType(TRANSACTION_TYPE_EXPENSE)
            showData(view.context, reportViewModel.getCurrentMonthStart(), reportViewModel.getCurrentMonthEnd())
        }


        binding.tvReportTerm.text = reportViewModel.getCurrentMonthStart().dropLast(3)

        /** Previous Month **/
        binding.tvReportLeft.setOnClickListener{
            showData(view.context, getPreviousMonth(reportViewModel.getCurrentMonthStart(), 1), reportViewModel.getCurrentMonthStart())
        }


        /** Next Month **/
        binding.tvReportRight.setOnClickListener{
            showData(view.context, getNextMonth(reportViewModel.getCurrentMonthStart(), 1), getNextMonth(reportViewModel.getCurrentMonthStart(), 2) )
        }

    }




    override fun onResume() {
        super.onResume()

        showData(requireContext(), startDate, endDate)

    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }



/**
 *  ************* Private Functions *****************
*/

    private fun showData(context: Context, start: String, end: String ){

        /** load data to ram **/
        reportViewModel.loadData(context, start, end)

        // show term
        binding.tvReportTerm.text = reportViewModel.getCurrentMonthStart().dropLast(3)


        /** load Adapter Data **/
        loadAdapterData()

        /************ Pie Chart  ***********/
        // draw chart
        drawPieChart(
            getChartTypeText(reportViewModel.getTransactionType()),
            reportViewModel.getTotalAmount(),
            reportViewModel.getTransactionType(),
            reportViewModel.getCategoryAmount()
        )
    }

    private fun setReportType(transType: Long){
        when (transType){
            TRANSACTION_TYPE_INCOME -> {
                binding.tvReportIncome.setTextColor(ContextCompat.getColor(requireContext(), R.color.app_title_text))
                binding.tvReportExpense.setTextColor(ContextCompat.getColor(requireContext(), R.color.app_title_text_inactive))
            }
            TRANSACTION_TYPE_EXPENSE -> {
                binding.tvReportIncome.setTextColor(ContextCompat.getColor(requireContext(), R.color.app_title_text_inactive))
                binding.tvReportExpense.setTextColor(ContextCompat.getColor(requireContext(), R.color.app_title_text))
            }
        }
        reportViewModel.setTransactionType(transType)
    }

    private fun getChartTypeText(transactionType: Long): String {
        return when (transactionType){
            TRANSACTION_TYPE_EXPENSE -> getString(R.string.category_expense)
            TRANSACTION_TYPE_INCOME -> getString(R.string.category_income)
            else -> ""
        }
    }

    private fun getPreviousMonth(currentDataMonth: String, months: Long): String{
        /** Get Previous or Next Month**/
        val lDate: LocalDate =  LocalDate.parse(currentDataMonth, DateTimeFormatter.ofPattern("yyyy-MM-dd"))
        return lDate.minusMonths(months).format(DateTimeFormatter.ofPattern("yyyy-MM-01")).toString()
    }


    private fun getNextMonth(currentDataMonth: String, months: Long): String{
        /** Get Previous or Next Month**/
        val lDate: LocalDate =  LocalDate.parse(currentDataMonth, DateTimeFormatter.ofPattern("yyyy-MM-dd"))
        return lDate.plusMonths(months).format(DateTimeFormatter.ofPattern("yyyy-MM-01")).toString()
    }


    private fun loadAdapterData(){
        /** load Adapter Data **/
        Thread {
            activity?.runOnUiThread {
                reportAdapter?.setList(reportViewModel.getCategoryAmount(), reportViewModel.getTotalAmount())
            }
        }.start()
    }


    private fun drawPieChart(typeText: String, totalAmount: Double, transType: Long, dataList: List<CategoryAmount>){
        val list = ArrayList<PieData>()
/*

        list.add(PieData("5%", 5f, "#fb5000", "#00000000", false))
        list.add(PieData("1%", 1f, "#fb0000", "#00000000", false))
*/

        for ( i in dataList.indices){
            list.add(
                PieData(
                    dataList[i].Category_Name + "\n" + "%.2f".format(dataList[i].Amount),
                    ( dataList[i].Amount / totalAmount * 100 ).toInt().toFloat(),
                    getDecreaseColors(i, transType)
                    //"#%06X".format((initColor.toLong(16) - 5 * i - 400 * i))
                )
            )
        }

        // if no data, add one record
        if (dataList.isEmpty()){
            list.add(PieData("0.00", (10).toFloat(), getDecreaseColors(0, transType)))
        }


        binding.ccReportChart.setTotalText(typeText)
        binding.ccReportChart.setTextMoney( "%.2f".format(totalAmount))
        binding.ccReportChart.setPieDataList(list)
        binding.ccReportChart.setOnSpecialTypeClickListener(object : CircleChartView.OnSpecialTypeClickListener {
            override fun onSpecialTypeClick(index: Int, type: String) {
                binding.ccReportChart.invalidate()
            }
        })

    }



    @SuppressLint("ResourceType")
    private fun getDecreaseColors(idx: Int, type: Long): String {
        return when (type){
            TRANSACTION_TYPE_EXPENSE -> {
                when (idx % 10){
                    0 -> getString(R.color.chart_expense_color_0)
                    1 -> getString(R.color.chart_expense_color_1)
                    2 -> getString(R.color.chart_expense_color_2)
                    3 -> getString(R.color.chart_expense_color_3)
                    4 -> getString(R.color.chart_expense_color_4)
                    5 -> getString(R.color.chart_expense_color_5)
                    6 -> getString(R.color.chart_expense_color_6)
                    7 -> getString(R.color.chart_expense_color_7)
                    8 -> getString(R.color.chart_expense_color_8)
                    9 -> getString(R.color.chart_expense_color_9)
                    else ->  "#EEEEEE"
                }
            }
            TRANSACTION_TYPE_INCOME -> {
                when (idx % 10){
                    0 -> getString(R.color.chart_income_color_0)
                    1 -> getString(R.color.chart_income_color_1)
                    2 -> getString(R.color.chart_income_color_2)
                    3 -> getString(R.color.chart_income_color_3)
                    4 -> getString(R.color.chart_income_color_4)
                    5 -> getString(R.color.chart_income_color_5)
                    6 -> getString(R.color.chart_income_color_6)
                    7 -> getString(R.color.chart_income_color_7)
                    8 -> getString(R.color.chart_income_color_8)
                    9 -> getString(R.color.chart_income_color_9)
                    else -> "#EEEEEE"
                }
            }
            else -> "#EEEEEE"
        }
    }

    fun switchToReportDetailFragment(view: View, cateID: Long, startDate: String, endDate: String) {

        // Put Data Before switch
        setFragmentResult(
            KEY_REPORT_DETAIL, bundleOf(
                KEY_REPORT_DETAIL_CATEGORY_ID to cateID,
                KEY_REPORT_DETAIL_START to startDate,
                KEY_REPORT_DETAIL_END to endDate
            )
        )

        // switch to category manage fragment
        view.findNavController().navigate(R.id.navigation_report_detail)
    }
}

