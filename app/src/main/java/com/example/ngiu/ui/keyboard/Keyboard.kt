package com.example.ngiu.ui.keyboard

import android.annotation.SuppressLint
import android.view.MotionEvent
import android.view.View
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.forEach
import com.example.ngiu.R


const val NO_DOT = 0
const val NO_DECIMAL = 1
const val ONE_DECIMAL = 2


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
    private var dot: Int = 3  // the places of Decimal point




    @SuppressLint("SetTextI18n")
    fun initKeys(echoView: TextView) {
        if (echoView.text.isEmpty()) echoView.text = "0.00"
        dot = echoView.text.length - echoView.text.lastIndexOf('.')
        if (dot >0) {
            if (echoView.text.last() == '0') {
                echoView.text = echoView.text.dropLast(1)
                dot--
            }
            if (echoView.text.last() == '0') {
                echoView.text = echoView.text.dropLast(1)
                dot--
            }
            if (echoView.text.last() == '.') {
                echoView.text = echoView.text.dropLast(1)
                dot--
            }
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
                        if (echoView.text.toString() == "0"){
                            echoView.text = it.tag.toString()
                        }else {
                            if (dot < 3) {
                                echoView.append(it.tag.toString())
                                if (dot > 0) dot++
                            }
                        }
                    }
                }
            }
        }

        // decimal point
        keyDot.setOnClickListener {
            if (dot == 0){
                echoView.append(keyDot.text)
                dot++
            }
        }
        keyCalc.setOnClickListener {

            //binding.priceInput.visibility = View.GONE
        }
        keyClear.setOnClickListener {
            echoView.text = "0"
            dot = 0
        }
        keyBack.setOnClickListener {
            if (echoView.length() > 1){
                echoView.text = echoView.text.dropLast(1)
                if (dot > 0) dot--
            }else{
                echoView.text = "0"
            }
        }

        keyEnter.setOnClickListener {
            when (dot){
                NO_DOT -> echoView.text = echoView.text.toString() + ".00"
                NO_DECIMAL -> echoView.text = echoView.text.toString() + "00"
                ONE_DECIMAL -> echoView.text = echoView.text.toString() + "0"
                //else -> echoView.text = echoView.text
            }
            hide()
        }
         
    }

    fun show(){
        keyboardPanel.visibility = View.VISIBLE
    }

    fun hide(){
        keyboardPanel.visibility = View.GONE
    }




}