package com.example.ngiu.ui.account

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ngiu.MainActivity
import com.example.ngiu.R
import com.example.ngiu.databinding.FragmentAccountRecordsBinding
import kotlinx.android.synthetic.main.fragment_account_records.*


class AccountCreditRecordsFragment : Fragment() {


    private lateinit var  accountRecordsViewModel:AccountRecordsViewModel
    private var _binding: FragmentAccountRecordsBinding? = null


    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private var rvAccount: RecyclerView? = null
    private var adapter = AccountRecordsAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        accountRecordsViewModel =
            ViewModelProvider(this).get(AccountRecordsViewModel::class.java)


        _binding = FragmentAccountRecordsBinding.inflate(inflater, container, false)
        rvAccount = binding.root.findViewById(R.id.recyclerview_account_detail)

        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rvAccount?.layoutManager = LinearLayoutManager(context)
        rvAccount?.adapter = adapter



        // set up toolbar icon and click event
        // choose items to show

        toolbar_account_normal_details.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        toolbar_account_normal_details.menu.findItem(R.id.action_edit).isVisible = true
        toolbar_account_normal_details.menu.findItem(R.id.action_add).isVisible = true

        // menu item clicked
        toolbar_account_normal_details.setOnMenuItemClickListener{
            when (it.itemId) {
                R.id.action_add -> {
                    // hide nav bottom bar
                    (activity as MainActivity).setNavBottomBarVisibility(View.GONE)
                    // navigate to add record screen
                    view.findNavController().navigate(R.id.navigation_record)
                    true
                }
                R.id.action_edit -> {
                    (activity as MainActivity).setNavBottomBarVisibility(View.GONE)
                    // navigate to add record screen
                    view.findNavController().navigate(R.id.navigation_record)
                    true
                }

                else -> super.onOptionsItemSelected(it)
            }
        }


    }

    override fun onResume() {
        super.onResume()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}