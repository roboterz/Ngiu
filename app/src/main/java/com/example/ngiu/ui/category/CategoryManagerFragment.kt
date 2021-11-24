package com.example.ngiu.ui.category

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ngiu.MainActivity
import com.example.ngiu.R
import com.example.ngiu.databinding.FragmentCategoryManageBinding
import kotlinx.android.synthetic.main.fragment_activity.*
import kotlinx.android.synthetic.main.fragment_category_manage.*
import kotlinx.android.synthetic.main.fragment_record.toolbar_record

class CategoryManagerFragment: Fragment() {
    private lateinit var categoryManagerViewModel: CategoryManagerViewModel
    private var _binding: FragmentCategoryManageBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private var mainCategoryAdapter: MainCategoryAdapter? = null
    private var subCategoryAdapter: SubCategoryAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // hide bottom bar
        (activity as MainActivity).setNavBottomBarVisibility(View.GONE)

        // Main Category Adapter
        Thread {
            this.activity?.runOnUiThread {

                recyclerview_category_main.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL,false)

                mainCategoryAdapter = this.context?.let {
                    MainCategoryAdapter(object: MainCategoryAdapter.OnClickListener {
                        // catch the item click event from adapter
                        override fun onItemClick(rID: Long) {

                            // main category item click
                            mainCategoryAdapter?.setList(categoryManagerViewModel.mainCategory)
                            recyclerview_category_main.adapter = mainCategoryAdapter
                            // show sub category
                            subCategoryAdapter?.setList(categoryManagerViewModel.getSubCategory(requireContext(),rID))
                        }
                    })
                }
                recyclerview_category_main.adapter = mainCategoryAdapter
            }
        }.start()

        // Sub Category Adapter
        Thread {
            this.activity?.runOnUiThread {

                recyclerview_category_sub.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL,false)

                subCategoryAdapter = this.context?.let {
                    SubCategoryAdapter(object: SubCategoryAdapter.OnClickListener {
                        // catch the item click event from adapter
                        override fun onItemClick(transID: Long) {
                            //
                        }

                        override fun onStarClick(transID: Long) {
                            TODO("Not yet implemented")
                        }
                    })
                }
                recyclerview_category_sub.adapter = subCategoryAdapter
            }
        }.start()

    }



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        categoryManagerViewModel = ViewModelProvider(this).get(CategoryManagerViewModel::class.java)
        _binding = FragmentCategoryManageBinding.inflate(inflater, container, false)


        // load data
        categoryManagerViewModel.loadDataToRam(requireContext(),1L)


        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        //---------------------------tool bar--------------------------------
        // choose items to show
        toolbar_record.menu.findItem(R.id.action_done).isVisible = true

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
        //---------------------------tool bar--------------------------------
    }


    override fun onResume() {
        super.onResume()

        // Main Category Adapter
        Thread {
            this.activity?.runOnUiThread {
                mainCategoryAdapter?.setList(categoryManagerViewModel.mainCategory)
            }
        }.start()

        // Sub Category Adapter
        Thread {
            this.activity?.runOnUiThread {
                subCategoryAdapter?.setList(categoryManagerViewModel.getSubCategory(requireContext(),categoryManagerViewModel.currentActiveMainCategory))
            }
        }.start()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        //(activity as MainActivity).setNavBottomBarVisibility(View.VISIBLE)
        _binding = null
    }



    //------------------------------------------Private Function------------------------------------

    private fun showSubCategoryItems(rID: Long) {

        //subCategoryAdapter?.setList(categoryManagerViewModel.getSubCategory(context,rID))
        // todo show sub category
    }
}