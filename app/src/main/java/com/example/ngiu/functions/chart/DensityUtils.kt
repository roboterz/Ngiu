package com.example.ngiu.functions.chart

// Android开发常用类, 用于dp/px切换
// https://zhuanlan.zhihu.com/p/570155777

import android.content.Context
import android.util.DisplayMetrics
import android.view.WindowManager

class DensityUtils {
    companion object {
        @JvmStatic
        fun dp2px(context: Context, scale: Float): Int {
            return (scale * context.resources.displayMetrics.density + 0.5f).toInt()
        }

        @JvmStatic
        fun px2dp(context: Context, scale: Float): Int {
            return (scale / context.resources.displayMetrics.density + 0.5f).toInt()
        }

        @JvmStatic
        fun getDensityDpi(context: Context): Int {
            val displayMetrics = DisplayMetrics()

            (context.getSystemService(Context.WINDOW_SERVICE) as WindowManager).defaultDisplay.getMetrics(
                displayMetrics
            )
            return displayMetrics.densityDpi
        }

        @JvmStatic
        fun getScaledDensity(context: Context): Float {
            val displayMetrics = DisplayMetrics()
            (context.getSystemService(Context.WINDOW_SERVICE) as WindowManager).defaultDisplay.getMetrics(
                displayMetrics
            )
            return displayMetrics.scaledDensity
        }
    }
}