package com.example.ngiu.ui.record


import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.ngiu.R

class RecordViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is notifications Fragment"
    }
    val text: LiveData<String> = _text

    var optionChoice: OptionChoice = OptionChoice()



    fun chooseTransactionType(ttid: Int): OptionChoice{
        val activeText = R.color.app_title_text
        val activePointer = View.VISIBLE

        optionChoice = OptionChoice().clearPreset()

        when (ttid) {
            1 -> {
                optionChoice.expense = activeText
                optionChoice.expensePointer = activePointer
            }
            2 -> {
                optionChoice.income = activeText
                optionChoice.incomePointer = activePointer
            }
            3 -> {
                optionChoice.transfer = activeText
                optionChoice.transferPointer = activePointer
            }

            4 -> {
                optionChoice.debitCredit = activeText
                optionChoice.debitCreditPointer = activePointer
            }
        }
        return optionChoice
    }
}

class OptionChoice{
    var expense:  Int = R.color.app_title_text
    var expensePointer: Int = View.VISIBLE
    var income:  Int = R.color.app_title_text_inactive
    var incomePointer:  Int = View.INVISIBLE
    var transfer:  Int = R.color.app_title_text_inactive
    var transferPointer:  Int = View.INVISIBLE
    var debitCredit:  Int = R.color.app_title_text_inactive
    var debitCreditPointer:  Int = View.INVISIBLE

    fun clearPreset(): OptionChoice{
        val optionChoice = OptionChoice()

        optionChoice.expense = R.color.app_title_text_inactive
        optionChoice.expensePointer = View.INVISIBLE

        return optionChoice
    }
}