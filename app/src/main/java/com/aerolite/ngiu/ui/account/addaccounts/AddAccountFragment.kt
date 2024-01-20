package com.aerolite.ngiu.ui.account.addaccounts

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aerolite.ngiu.R
import com.aerolite.ngiu.databinding.FragmentAccountAddAccountBinding

//import kotlinx.android.synthetic.main.fragment_account_add_account.*

class AddAccountFragment : Fragment() {

    private lateinit var addAccountViewModel: AddAccountViewModel

    private var _binding: FragmentAccountAddAccountBinding? = null
    private val binding get() = _binding!!

    private var rvAddAccount: RecyclerView? = null
    private var adapter = AddAccountAdapter()



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        addAccountViewModel = ViewModelProvider(this).get(AddAccountViewModel::class.java)

        _binding = FragmentAccountAddAccountBinding.inflate(inflater, container, false)

        rvAddAccount = binding.root.findViewById(R.id.rv_acct_type)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        addAccountViewModel.getAccountType(requireContext())
        rvAddAccount?.layoutManager = LinearLayoutManager(context)
        rvAddAccount?.adapter = adapter

        addAccountViewModel.accountType.observe(viewLifecycleOwner){
            adapter.addItems(it)
        }


        binding.toolbarAddAcct.menu.findItem(R.id.action_add).isVisible = true
        // menu item clicked
        binding.toolbarAddAcct.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
    }

    override fun onResume() {
        super.onResume()

    }



    override fun onDestroy() {
        super.onDestroy()

        _binding = null
    }

}