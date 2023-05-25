package com.example.ngiu.ui.keyboard

import android.annotation.SuppressLint
import android.view.MotionEvent
import android.view.View
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.forEach
import com.example.ngiu.R



const val TEXTVIEW_MAX_NUMBER: Int = 16

@SuppressLint("ClickableViewAccessibility")
class Keyboard(view: View){
    //private val key0: TextView = view.findViewById(R.id.tv_price_0)
    private val keyBack: TextView = view.findViewById(R.id.tv_price_back)
    private val keyDot: TextView = view.findViewById(R.id.tv_price_dot)
    private val keyEnter: TextView = view.findViewById(R.id.tv_price_enter)
    private val keyCalc: TextView = view.findViewById(R.id.tv_price_calc)
    private val keyClear: TextView = view.findViewById(R.id.tv_price_clear)
    private val keyboardPanel: ConstraintLayout = view.findViewById(R.id.layout_Keyboard)
    private val keys: ConstraintLayout = view.findViewById(R.id.price_input_keys)


    @SuppressLint("SetTextI18n")
    fun initKeys(echoView: TextView) {
        if (echoView.text.isEmpty()) {
            echoView.text = "0.00"
        }else{
            echoView.text = "%.2f".format(echoView.text.toString().toDouble())
        }


        keys.forEach{
            // Press feedback
            it.setOnTouchListener { view, motionEvent ->
                when (motionEvent.actionMasked){
                    MotionEvent.ACTION_DOWN -> {it.setBackgroundColor(ContextCompat.getColor(view.context, R.color.keyboard_frame))}
                    MotionEvent.ACTION_UP -> {it.setBackgroundResource(R.drawable.keyboard_border)}
                }
                false
            }

            // number button
            when (it.tag){
                "1","2","3","4","5","6","7","8","9","0" -> {
                    it.setOnClickListener{ _ ->
                        if (echoView.text.length < TEXTVIEW_MAX_NUMBER) {
                            if (echoView.text.toString().toDouble() == 0.0) {
                                echoView.text = "%.2f".format(it.tag.toString().toDouble() / 100.0)
                            } else {
                                echoView.text = "%.2f".format(
                                    echoView.text.toString().toDouble() * 10.0
                                            + it.tag.toString().toDouble() / 100.0
                                )
                            }
                        }
                    }
                }
            }
        }

        // decimal point
        keyDot.setOnClickListener {
            // todo need change
        }
        //
        keyCalc.setOnClickListener {
            // todo
            //binding.priceInput.visibility = View.GONE
        }
        // clear
        keyClear.setOnClickListener {
            echoView.text = "0.00"
        }
        // back
        keyBack.setOnClickListener {
            echoView.text = deleteLastDigit(echoView.text.toString())
        }
        // enter
        keyEnter.setOnClickListener {
            echoView.text = "%.2f".format(echoView.text.toString().toDouble())
            hide()
        }
         
    }

    fun show(){
        keyboardPanel.visibility = View.VISIBLE
    }

    fun hide(){
        keyboardPanel.visibility = View.GONE
    }

    fun state(): Int{
        return keyboardPanel.visibility
    }


    private fun deleteLastDigit(str: String): String {
        var newStr = (str.toDouble() * 100).toLong().toString()

        if (newStr.length > 1) {
            newStr = newStr.substring(0, newStr.length - 1)

            return "%.2f".format(newStr.toDouble() / 100.0)
        }else{
            return "0.00"
        }

    }

}