package com.example.ngiu.ui.home

import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.ngiu.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var recordId: Long = 0

    private lateinit var homeViewModel: HomeViewModel
    private var _binding: FragmentHomeBinding? = null
    private val TAG = "HomeFragment"

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textHome
        homeViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //get data fom edittext where user enter something text.
        val strName = binding.etxtName.text
        val strDate = binding.etxtDate.text
        val strMemo = binding.etxtMemo.text
        binding.btnAdd.setOnClickListener {
            Log.e(
                TAG,
                "onViewCreated: " + "Name: " + strName + " Date: " + strDate + " Memo: " + strMemo
            )

            performAddingTask(strName, strDate, strMemo)
        }


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    private fun performAddingTask(
        strName: Editable,
        strDate: Editable,
        strMemo: Editable
    ) {
        Thread {
            recordId = homeViewModel.insertData(activity, strName, strDate, strMemo)

            activity?.runOnUiThread {
                if (recordId > 0) {
                    Toast.makeText(activity, "Record Saved", Toast.LENGTH_SHORT).show()
                    strName.clear()
                    strDate.clear()
                    strMemo.clear()
                }
            }
        }.start()
    }
}