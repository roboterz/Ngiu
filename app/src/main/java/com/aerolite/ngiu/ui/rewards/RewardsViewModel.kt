package com.aerolite.ngiu.ui.rewards

import android.content.Context
import androidx.lifecycle.ViewModel
import com.aerolite.ngiu.data.AppDatabase
import com.aerolite.ngiu.data.entities.Reward
import com.aerolite.ngiu.data.entities.returntype.AccountIcon
import com.aerolite.ngiu.data.entities.returntype.RewardsDetail
import kotlin.collections.ArrayList

class RewardsViewModel : ViewModel(){

    var accountIcons: List<AccountIcon> = ArrayList()

    var rewardList: List<Reward> = ArrayList()

    var rewardDetailList: List<RewardsDetail> = ArrayList()

    //var icons: List<Account> = ArrayList()
    //var icon: Icon = Icon()

    fun loadAccountIcons(context: Context){
        val database = AppDatabase.getDatabase(context)
        accountIcons = database.account().getAccountIcons()

        // todo got error
        //rewardList = database.reward().getAllReward()

    //icons = database.account().getAccountByType(ACCOUNT_TYPE_CREDIT)

    }

//    fun getRewardListByAccountID(context: Context, acctID: Long): List<RewardsDetail>{
//        val database = AppDatabase.getDatabase(context)
//        rewardList = database.reward().getRewardsDetailByAccountID(acctID)
//
//        return rewardList
//    }

    fun setRewardListByAccountID(context: Context, acctID: Long){
        val database = AppDatabase.getDatabase(context)
        //rewardList = database.reward().getRewardsDetailByAccountID(acctID)

        rewardList = database.reward().getAllReward()

    }

//    fun getIcon(context: Context): Icon{
//        return AppDatabase.getDatabase(context).iconDao().getOneIcon()
//    }

//    fun getAccountIcons(context: Context): List<AccountIcon>{
//        return AppDatabase.getDatabase(context).account().getAccountIcons()
//    }

}