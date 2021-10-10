package com.example.ngiu.ui.account.addaccounts

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ngiu.databinding.AddAccountBinding

class AddAccountFragment : Fragment() {

    private var _binding: AddAccountBinding? = null
    private val binding get() = _binding!!

    private val addAccountAdapter = AddAccountAdapter()

    private val viewModel: AddAccountViewModel by lazy {
        ViewModelProvider(this).get(AddAccountViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return AddAccountBinding.inflate(inflater, container, false).run {
            _binding = this
            root
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvAcctType.apply {
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            adapter = addAccountAdapter
        }

        viewModel.state.observe(viewLifecycleOwner) {
            addAccountAdapter.submitList(it)
        }


      // test to go back home with navtool
/*    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home-> {

                *//*   // hide nav bottom bar
                   (activity as MainActivity).setNavBottomBarVisibility(View.GONE)*//*

                // navigate to add record screen
                view?.findNavController()?.popBackStack()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }*/
    }

    override fun onResume() {
        super.onResume()
        viewModel.onAction(AddAccountViewModel.Action.Load(requireContext()))
    }



    override fun onDestroy() {
        super.onDestroy()
        binding.rvAcctType.adapter = null
        _binding = null
    }
}