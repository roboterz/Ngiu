package com.example.ngiu.ui.category

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.ngiu.databinding.FragmentCategoryManageBinding
import com.example.ngiu.ui.category.CategoryManagerViewModel

class CategoryManagerFragment: Fragment() {
    private lateinit var categoryManagerViewModel: CategoryManagerViewModel
    private var _binding: FragmentCategoryManageBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        categoryManagerViewModel =
            ViewModelProvider(this).get(CategoryManagerViewModel::class.java)

        _binding = FragmentCategoryManageBinding.inflate(inflater, container, false)


        return binding.root
    }

}