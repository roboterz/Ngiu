package com.example.ngiu.ui.activity

import android.app.Application
import android.content.Context
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.ngiu.data.AppDatabase
import com.example.ngiu.data.TransDao
import com.example.ngiu.data.entities.Trans
import com.example.ngiu.data.entities.returntype.TransactionDetail
import com.example.ngiu.functions.TRANSACTION_TYPE_EXPENSE
import com.example.ngiu.functions.TRANSACTION_TYPE_INCOME
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class ActivityViewModel(application: Application) : AndroidViewModel(application) {
//class ActivityViewModel(private val transDao:TransDao) : ViewModel() {


    //var transactionDetail: List<TransactionDetail> = ArrayList()

    private val transDao = AppDatabase.getDatabase(application).trans()

/*    val allTransactionDetail: Flow<PagingData<TransactionDetail>> = Pager(
        PagingConfig(
            // 每页显示的数据的大小
            pageSize = 60,
            // 开启占位符
            enablePlaceholders = true,
            // 预刷新的距离，距离最后一个 item 多远时加载数据
            prefetchDistance = 3,
            //初始化加载数量，默认为 pageSize * 3
            initialLoadSize = 60,
            //一次应在内存中保存的最大数据,这个数字将会触发，滑动加载更多的数据
            maxSize = 200
        )
    ){
        dao.getAllTransDetailPage()
    }.flow*/


    var monthExpense: Double = 0.00
    var monthIncome: Double = 0.00
    var budget: Double = 0.00

    fun getRecordByID(activity: FragmentActivity?, rID: Long): Trans {
        return AppDatabase.getDatabase(activity!!).trans().getRecordByID(rID)
    }

    fun loadDataToRam(context: Context){
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val lDate = LocalDate.now().withDayOfMonth(1)
        val fromDate = lDate.format(formatter)
        val toDate = lDate.plusMonths(1).format(formatter)

        monthExpense = AppDatabase.getDatabase(context).trans().getSumOfDaysByTransactionType(
            TRANSACTION_TYPE_EXPENSE, fromDate, toDate)
        monthIncome = AppDatabase.getDatabase(context).trans().getSumOfDaysByTransactionType(
            TRANSACTION_TYPE_INCOME, fromDate, toDate)

//        AppDatabase.getDatabase(context).trans().getAllTransDetail().observe(, androidx.lifecycle.Observer { it ->
//                transactionDetail = it
//        })


        // todo Budget


    }

    // LiveData
    fun getTransDetail(): LiveData<List<TransactionDetail>> {
        return transDao.getAllTransDetail()
    }
    fun remove(data: Trans) {
        viewModelScope.launch(Dispatchers.IO) {
            transDao.deleteTransaction(data)
        }
    }

}

