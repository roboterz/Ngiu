package com.example.ngiu.ui.record


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainer
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.example.ngiu.MainActivity
import com.example.ngiu.R
import com.example.ngiu.databinding.FragmentRecordBinding
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
        savedInstanceState: Bundle?,
    ): View? {
        recordViewModel =
            ViewModelProvider(this).get(RecordViewModel::class.java)

        _binding = FragmentRecordBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // focus on Expense page when open up
        //setStatus( recordViewModel.chooseTransactionType(1) )


        // todo load record data
        //Toast.makeText(context,"open",Toast.LENGTH_SHORT).show()

        return root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Keep current state when reloading
        setStatus(recordViewModel.optionChoice)

        // touch Expense textView, switch to Expense page
        tvSectionExpense.setOnClickListener {
            setStatus(recordViewModel.chooseTransactionType(1))
            switchPage()
        }
        // touch Income textView, switch to Income page
        tvSectionIncome.setOnClickListener {
            setStatus(recordViewModel.chooseTransactionType(2))
            switchPage()
        }
        // touch Transfer textView, switch to Transfer page
        tvSectionTransfer.setOnClickListener {
            setStatus(recordViewModel.chooseTransactionType(3))
            switchPage()
        }
        // touch DebitCredit textView, switch to DebitCredit page
        tvSectionDebitCredit.setOnClickListener {
            setStatus(recordViewModel.chooseTransactionType(4))
            switchPage()
        }


        // set up toolbar icon and click event

        // choose items to show
        toolbar_record.menu.findItem(R.id.action_done).isVisible = true
        //toolbar_record.title = "sadfdafdfa"

        // click the navigation Icon in the left side of toolbar
        toolbar_record.setNavigationOnClickListener(View.OnClickListener {

            // call back button event to switch to previous fragment
            requireActivity().onBackPressed()

        })

        // menu item clicked
        toolbar_record.setOnMenuItemClickListener{
            when (it.itemId) {
                R.id.action_done -> {

                    // todo save record

                    // call back button event to switch to previous fragment
                    requireActivity().onBackPressed()

                    true
                }

                else -> super.onOptionsItemSelected(it)
            }
        }


        //
        val viewPager2 = view.findViewById<ViewPager2>(R.id.vp_record_category)
        val myAdapter = RecordCategoryAdapter()
        myAdapter.setList(listOf(1,2,3,4,5,6,7,8,9))
        viewPager2.adapter = myAdapter


    }

    private fun setStatus(optionChoice: OptionChoice){
        tvSectionExpense.setTextColor(ContextCompat.getColor(requireContext(),optionChoice.expense))
        tvSectionExpensePointer.visibility = optionChoice.expensePointer
        tvSectionIncome.setTextColor(ContextCompat.getColor(requireContext(),optionChoice.income))
        tvSectionIncomePointer.visibility = optionChoice.incomePointer
        tvSectionTransfer.setTextColor(ContextCompat.getColor(requireContext(),optionChoice.transfer))
        tvSectionTransferPointer.visibility = optionChoice.transferPointer
        tvSectionDebitCredit.setTextColor(ContextCompat.getColor(requireContext(),optionChoice.debitCredit))
        tvSectionDebitCreditPointer.visibility = optionChoice.debitCreditPointer
    }

    private fun switchPage(){

    }



    override fun onDestroyView() {
        super.onDestroyView()
        (activity as MainActivity).setNavBottomBarVisibility(View.VISIBLE)
        _binding = null
    }

    private val data: List<Int>
        get() {
            val list = ArrayList<Int>()
            for (i in 0..3) {
                list.add(i)
            }
            return list
        }

}

