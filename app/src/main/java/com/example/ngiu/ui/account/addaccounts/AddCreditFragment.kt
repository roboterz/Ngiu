package com.example.ngiu.ui.account.addaccounts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.ngiu.databinding.FragmentAddCreditBinding



class AddCredit : Fragment() {
    private var _binding: FragmentAddCreditBinding? = null
    private val binding get() = _binding!!
    private lateinit var addCreditViewModel: AddCreditViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentAddCreditBinding.inflate(inflater, container, false).run {
            _binding = this
            root
        }
    }




    }



