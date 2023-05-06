package com.example.ngiu.ui.report

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ngiu.MainActivity
import com.example.ngiu.R
import com.example.ngiu.databinding.FragmentReportBinding
import com.example.ngiu.functions.TRANSACTION_TYPE_EXPENSE
import com.example.ngiu.functions.TRANSACTION_TYPE_INCOME
import com.example.ngiu.functions.chart.CategoryAmount
import com.example.ngiu.functions.chart.CircleChartView
import com.example.ngiu.functions.chart.PieData
import com.example.ngiu.ui.calendar.CalendarAdapter
import kotlinx.android.synthetic.main.fragment_calendar.*
import kotlinx.android.synthetic.main.fragment_report.*
import kotlinx.android.synthetic.main.fragment_setting.*
import java.time.LocalDate
import java.time.LocalDateTime
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


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        (activity as MainActivity).setNavBottomBarVisibility(View.GONE)
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ):View? {
        reportViewModel =
            ViewModelProvider(this)[ReportViewModel::class.java]

        _binding = FragmentReportBinding.inflate(inflater, container, false)

        /** Report Adapter **/
        Thread {
            this.activity?.runOnUiThread {

                recyclerview_report.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL,false)
                reportAdapter = this.context?.let {
                    ReportAdapter(object: ReportAdapter.OnClickListener {
                        // catch the item click event from adapter
                        override fun onItemClick(ID: Long) {
                            // Open/switch to account detail
                            // todo report item click
                        }
                    })
                }

                recyclerview_report.adapter = reportAdapter
            }
        }.start()


        /** Load Data **/
        reportViewModel.loadData(requireContext(), TRANSACTION_TYPE_INCOME, LocalDate.now().format(dateFormatter).toString(), LocalDate.now().plusMonths(1).format(dateFormatter).toString() )


        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // toolbar title
        toolbar_report.setTitle(R.string.nav_title_report_income)
        // show add button

        // toolbar menu item clicked

        /** click Go Back Icon in the left side of toolbar **/
        toolbar_report.setNavigationOnClickListener {
            // call back button event to switch to previous fragment
            //requireActivity().onBackPressed()
            NavHostFragment.findNavController(this).navigateUp()
        }


        tv_report_term.text = reportViewModel.getCurrentMonth().dropLast(3)

        /** Previous Month **/
        tv_report_left.setOnClickListener{

            // load data from database
            reportViewModel.loadData(requireContext(), reportViewModel.getTransactionType(), getPreviousMonth(reportViewModel.getCurrentMonth(), 1), reportViewModel.getCurrentMonth() )

            // show term
            tv_report_term.text = reportViewModel.getCurrentMonth().dropLast(3)


            // load data to Adapter
            loadAdapterData()

            // draw chart
            drawPieChart(
                "Income",
                reportViewModel.getTotalAmount(),
                reportViewModel.getTransactionType(),
                reportViewModel.getCategoryAmount()
            )
        }


        /** Next Month **/
        tv_report_right.setOnClickListener{

            // load data from database
            reportViewModel.loadData(requireContext(), reportViewModel.getTransactionType(),  getNextMonth(reportViewModel.getCurrentMonth(), 1), getNextMonth(reportViewModel.getCurrentMonth(), 2))

            // show term
            tv_report_term.text = reportViewModel.getCurrentMonth().dropLast(3)

            // load data to Adapter
            loadAdapterData()

            // draw chart
            drawPieChart(
                getChartTypeText(reportViewModel.getTransactionType()),
                reportViewModel.getTotalAmount(),
                reportViewModel.getTransactionType(),
                reportViewModel.getCategoryAmount()
            )

        }







        /************ Pie Chart  ***********/
        //cateAmountList.add(CategoryAmount(0,"Breakfast", 800.00))
        drawPieChart(
            "Income",
            reportViewModel.getTotalAmount(),
            reportViewModel.getTransactionType(),
            reportViewModel.getCategoryAmount()
        )




    }




    override fun onResume() {
        super.onResume()

        /** load Adapter Data **/
        loadAdapterData()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }



/**
 *  ************* Private Functions *****************
*/


    private fun getChartTypeText(transactionType: Long): String {
        when (transactionType){
            TRANSACTION_TYPE_EXPENSE -> {
                return getString(R.string.category_expense)
            }
            TRANSACTION_TYPE_INCOME -> {
                return getString(R.string.category_income)
            }
            else -> {
                return ""
            }
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


        cc_report_chart.setTotalText(typeText)
        cc_report_chart.setTextMoney( "%.2f".format(totalAmount))
        cc_report_chart.setPieDataList(list)
        cc_report_chart.setOnSpecialTypeClickListener(object : CircleChartView.OnSpecialTypeClickListener {
            override fun onSpecialTypeClick(index: Int, type: String) {
                cc_report_chart.invalidate()
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

}

