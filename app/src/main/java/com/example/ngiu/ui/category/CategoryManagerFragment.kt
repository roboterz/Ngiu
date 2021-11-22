package com.example.ngiu.ui.category

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.ngiu.MainActivity
import com.example.ngiu.R
import com.example.ngiu.databinding.FragmentCategoryManageBinding
import com.example.ngiu.ui.category.CategoryManagerViewModel
import kotlinx.android.synthetic.main.fragment_record.*

class CategoryManagerFragment: Fragment() {
    private lateinit var categoryManagerViewModel: CategoryManagerViewModel
    private var _binding: FragmentCategoryManageBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // hide bottom bar
        (activity as MainActivity).setNavBottomBarVisibility(View.GONE)


    }


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


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        //(activity as MainActivity).setNavBottomBarVisibility(View.GONE)

        // click the navigation Icon in the left side of toolbar
        toolbar_record.setNavigationOnClickListener(View.OnClickListener {

            // call back button event to switch to previous fragment
            requireActivity().onBackPressed()
        })

        // menu item clicked
        toolbar_record.setOnMenuItemClickListener{
            when (it.itemId) {
                // done menu
                R.id.action_done -> {
                    // save record
                    //if (saveRecord(receivedID) == 0) {
                        // call back button event to switch to previous fragment
                        requireActivity().onBackPressed()
                    //}
                    true
                }

                else -> super.onOptionsItemSelected(it)
            }
        }
    }



    override fun onDestroyView() {
        super.onDestroyView()
        //(activity as MainActivity).setNavBottomBarVisibility(View.VISIBLE)
        _binding = null
    }
}