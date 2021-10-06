package com.example.ngiu.ui.record

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.ActionBarContextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import com.example.ngiu.MainActivity
import com.example.ngiu.R
import com.example.ngiu.databinding.FragmentRecordBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_activity.*
import kotlinx.android.synthetic.main.fragment_record.*

class RecordFragment : Fragment() {

    private lateinit var recordViewModel: RecordViewModel
    private var _binding: FragmentRecordBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        recordViewModel =
            ViewModelProvider(this).get(RecordViewModel::class.java)

        _binding = FragmentRecordBinding.inflate(inflater, container, false)
        val root: View = binding.root


        // todo load record data
        //Toast.makeText(context,"open",Toast.LENGTH_SHORT).show()


        return root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)




        // set up toolbar icon and click event

        // choose items to show
        toolbar_record.menu.findItem(R.id.action_done).isVisible = true
        //toolbar_record.title = "sadfdafdfa"

        // menu item clicked
        toolbar_record.setOnMenuItemClickListener{
            when (it.itemId) {
                R.id.action_done -> {

                    // todo save record

                    // navigate to activity screen
                    view.findNavController().navigate(R.id.navigation_activity)
                    true
                }

                else -> super.onOptionsItemSelected(it)
            }
        }


    }


    override fun onDestroyView() {
        super.onDestroyView()
        (activity as MainActivity).setNavBottomBarVisibility(View.VISIBLE)
        _binding = null
    }
}