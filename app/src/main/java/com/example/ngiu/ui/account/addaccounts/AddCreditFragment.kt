package com.example.ngiu.ui.account.addaccounts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.ngiu.databinding.FragmentAddcreditBinding


class AddCredit : Fragment() {
    private var _binding: FragmentAddcreditBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentAddcreditBinding.inflate(inflater, container, false).run {
            _binding = this
            root
        }
    }



}