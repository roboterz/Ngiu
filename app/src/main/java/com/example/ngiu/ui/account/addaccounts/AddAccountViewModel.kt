package com.example.ngiu.ui.account.addaccounts

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.ngiu.data.AppDatabase
import com.example.ngiu.ui.account.model.AccountTypeUIModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class AddAccountViewModel : ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    private val _state = MutableLiveData<List<AccountTypeUIModel>>()
    val state: LiveData<List<AccountTypeUIModel>> = _state

    fun onAction(action: Action) {
        when (action) {
            is Action.Load -> {
                readData(action.context)
            }
        }
    }

    //fixed threading issue by using rxjava
    private fun readData(context: Context) {
        val appDatabase = AppDatabase.getDatabase(context)
        appDatabase.accounttype().getAllAccountType()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe({ accountTypes ->
                accountTypes.map {
                    AccountTypeUIModel(it.AccountType_Name, it.AccountType_Memo)
                }.run {
                    _state.postValue(this)
                }
            }, {
                Log.e("Error", "Reading data failed with ${it.message}")
            }).run {
                compositeDisposable.add(this)
            }
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }

    sealed class Action {
        data class Load(val context: Context) : Action()
    }
}