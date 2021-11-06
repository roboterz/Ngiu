package com.example.ngiu.ui.account.addaccounts

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ngiu.MainActivity
import com.example.ngiu.R
import com.example.ngiu.databinding.FragmentAddAccountBinding
import kotlinx.android.synthetic.main.fragment_activity.*
import kotlinx.android.synthetic.main.fragment_activity.toolbar_activity
import kotlinx.android.synthetic.main.fragment_add_account.*

class AddAccountFragment : Fragment() {

    private var _binding: FragmentAddAccountBinding? = null
    private val binding get() = _binding!!

    lateinit var addAccountAdapter : AddAccountAdapter

    private val viewModel: AddAccountViewModel by lazy {
        ViewModelProvider(this).get(AddAccountViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return FragmentAddAccountBinding.inflate(inflater, container, false).run {
            _binding = this
            root
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        addAccountAdapter = AddAccountAdapter(requireActivity())

        binding.rvAcctType.apply {
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            adapter = addAccountAdapter
        }

        viewModel.state.observe(viewLifecycleOwner) {
            addAccountAdapter.submitList(it)
        }

        toolbar_add_acct.menu.findItem(R.id.action_add).isVisible = true
        // menu item clicked
        toolbar_add_acct.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
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