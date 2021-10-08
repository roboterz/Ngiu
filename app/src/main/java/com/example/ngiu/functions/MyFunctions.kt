package com.example.ngiu.functions

import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController

class MyFunctions {

    fun switchToFragment(view: View, resID: Int, withBackStack: Boolean){
        // pop out fragment from back stack
        if (withBackStack) view.findNavController().popBackStack()
        // switch to fragment
        view.findNavController().navigate(resID)
    }

}