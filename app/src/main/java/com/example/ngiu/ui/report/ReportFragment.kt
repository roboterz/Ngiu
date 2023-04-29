package com.example.ngiu.ui.report

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.ngiu.MainActivity
import com.example.ngiu.databinding.FragmentReportBinding
import com.example.ngiu.functions.TRANSACTION_TYPE_EXPENSE
import com.example.ngiu.functions.TRANSACTION_TYPE_INCOME
import com.example.ngiu.functions.chart.CategoryAmount
import com.example.ngiu.functions.chart.CircleChartView
import com.example.ngiu.functions.chart.PieData
import kotlinx.android.synthetic.main.fragment_report.*


class ReportFragment : Fragment() {


    private lateinit var reportViewModel: ReportViewModel
    private var _binding: FragmentReportBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!


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


        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // toolbar
        // show add button

        // toolbar menu item clicked



        // ************ Pie Chart  ***********
        val ct = arrayListOf<CategoryAmount>()
        ct.add(CategoryAmount(0,"Breakfast", 800.00))
        ct.add(CategoryAmount(0,"Lunch", 500.00))
        ct.add(CategoryAmount(0,"Dinner", 300.00))
        ct.add(CategoryAmount(0,"Snack", 88.25))
        ct.add(CategoryAmount(0,"Transport", 70.90))
        ct.add(CategoryAmount(0,"Parking", 22.25))
        ct.add(CategoryAmount(0,"Parking", 22.25))
        ct.add(CategoryAmount(0,"Parking", 22.25))
        ct.add(CategoryAmount(0,"Parking", 22.25))
        ct.add(CategoryAmount(0,"Parking", 22.25))
        ct.add(CategoryAmount(0,"Parking", 22.25))
        ct.add(CategoryAmount(0,"Parking", 22.25))
        ct.add(CategoryAmount(0,"Parking", 22.25))
        ct.add(CategoryAmount(0,"Parking", 22.25))
        ct.add(CategoryAmount(0,"Parking", 22.25))
        ct.add(CategoryAmount(0,"Parking", 22.25))
        ct.add(CategoryAmount(0,"Parking", 22.25))

        drawPieChart("Expanse",2026.15, TRANSACTION_TYPE_INCOME, ct)
        // *********** Pie Chart  ***********

    }


    override fun onResume() {
        super.onResume()



    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }



/**
 *  ************* Private Functions *****************
*/

    private fun drawPieChart(typeText: String, totalAmount: Double, transType: Long, dataList: List<CategoryAmount>){
        val list = ArrayList<PieData>()
/*

        list.add(PieData("5%", 5f, "#fb5000", "#00000000", false))
        list.add(PieData("1%", 1f, "#fb0000", "#00000000", false))
*/

        for ( i in dataList.indices){
            list.add(
                PieData(
                    dataList[i].CategoryName,
                    ( dataList[i].Amount / totalAmount * 100 ).toInt().toFloat(),
                    getDecreaseColors(i, transType)
                    //"#%06X".format((initColor.toLong(16) - 5 * i - 400 * i))
                )
            )
        }

        cc_view.setTotalText(typeText)
        cc_view.setTextMoney( "$" + "%.2f".format(totalAmount))
        cc_view.setPieDataList(list)
        cc_view.setOnSpecialTypeClickListener(object : CircleChartView.OnSpecialTypeClickListener {
            override fun onSpecialTypeClick(index: Int, type: String) {
                cc_view.invalidate()
            }
        })

    }

    private fun getDecreaseColors(idx: Int, type: Long): String {
        return when (type){
            TRANSACTION_TYPE_EXPENSE -> {
                when (idx){
                    0 -> "#1B4F72"
                    1,17,33 -> "#21618C"
                    2,18,34 -> "#2874A6"
                    3,19,35 -> "#2E86C1"
                    4,20,36 -> "#2980B9"
                    5,21,37 -> "#3498DB"
                    6,22,38 -> "#5DADE2"
                    7,23,39 -> "#85C1E9"
                    8,24,40 -> "#D6EAF8"
                    9,25,41 -> "#A9CCE3"
                    10,26,42 -> "#7FB3D5"
                    11,27,43 -> "#5499C7"
                    12,28,44 -> "#2980B9"
                    13,29,45 -> "#2471A3"
                    14,30,46 -> "#1F618D"
                    15,31,47 -> "#1A5276"
                    16,32,48 -> "#154360"
                    else ->  "#EEEEEE"
                }
            }
            TRANSACTION_TYPE_INCOME -> {
                when (idx){
                    0 -> "#0B5345"
                    1,17,33 -> "#0E6655"
                    2,18,34 -> "#117A65"
                    3,19,35 -> "#138D75"
                    4,20,36 -> "#16A085"
                    5,21,37 -> "#45B39D"
                    6,22,38 -> "#73C6B6"
                    7,23,39 -> "#A2D9CE"
                    8,24,40 -> "#7DCEA0"
                    9,25,41 -> "#52BE80"
                    10,26,42 -> "#27AE60"
                    11,27,43 -> "#229954"
                    12,28,44 -> "#1E8449"
                    13,29,45 -> "#196F3D"
                    14,30,46 -> "#145A32"
                    15,31,47 -> "#1D8348"
                    16,32,48 -> "#239B56"
                    else -> "#EEEEEE"
                }
            }
            else -> "#EEEEEE"
        }
    }
}

