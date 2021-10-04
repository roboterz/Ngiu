package com.example.ngiu.ui.activity

import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.fragment.app.FragmentManager
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import com.example.ngiu.R
import com.example.ngiu.data.entities.Person
import com.example.ngiu.databinding.FragmentActivityBinding
import com.example.ngiu.functions.AccountListAdapter
import kotlinx.android.synthetic.main.fragment_activity.*


class ActivityFragment : Fragment() {
    private lateinit var activityViewModel: ActivityViewModel
    private var _binding: FragmentActivityBinding? = null
    //val personArr = ArrayList<Person>()

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
            activityViewModel =
                ViewModelProvider(this).get(ActivityViewModel::class.java)

            _binding = FragmentActivityBinding.inflate(inflater, container, false)
            val root: View = binding.root

            //super.onCreate(savedInstanceState)
            //setHasOptionsMenu(true)

            return root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // load custom menu into toolbar
        toolbar_activity.inflateMenu(R.menu.title_menu)
        // choose items to show
        toolbar_activity.menu.findItem(R.id.action_add).isVisible = true

        // menu item clicked
        toolbar_activity.setOnMenuItemClickListener{
            when (it.itemId) {
                R.id.action_add -> {
                    // navigate to add record screen
                    view.findNavController().navigate(R.id.navigation_record)
                    true
                }

                else -> super.onOptionsItemSelected(it)
            }
        }

        // floating Add transaction button
        val fab: View = view.findViewById(R.id.floatingAddTransactionButton)
        fab.setOnClickListener { view ->
            // navigate to add record screen
            view.findNavController().navigate(R.id.navigation_record)
        }


        // call readPerson function on the bottom of this class
        readPerson(view)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    // load data to RecyclerView
    private fun readPerson(view: View) {
        // list of person
        val personArr = ArrayList<Person>()

        Thread {
            val allRecord = activityViewModel.readData(activity) as List<Person>

            this.activity?.runOnUiThread {

                for (i in 0 until allRecord.size) {
                    personArr.add(Person( allRecord.get(i).ID , allRecord.get(i).Name))
                }

                val linearLayoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL,false)

                // specify the layout manager for recycler view
                recyclerView.layoutManager = linearLayoutManager

                // finally, data bind the recycler view with adapter
                recyclerView.adapter = AccountListAdapter(personArr)
            }
        }.start()
    }
}
