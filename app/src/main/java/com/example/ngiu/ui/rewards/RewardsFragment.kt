package com.example.ngiu.ui.rewards

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ngiu.MainActivity
import com.example.ngiu.R
import com.example.ngiu.databinding.FragmentRewardsBinding
import com.example.ngiu.ui.rewards.RewardsAdapter
import com.example.ngiu.ui.rewards.RewardsViewModel
import kotlinx.android.synthetic.main.fragment_activity.*
import kotlinx.android.synthetic.main.fragment_rewards.*

class RewardsFragment : Fragment() {

    private lateinit var rewardsViewModel: RewardsViewModel
    private var _binding: FragmentRewardsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rewardsViewModel =
            ViewModelProvider(this).get(RewardsViewModel::class.java)

        _binding = FragmentRewardsBinding.inflate(inflater, container, false)

        //



        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        toolbar_rewards.menu.findItem(R.id.action_add).isVisible = true

        // menu item clicked
        toolbar_rewards.setOnMenuItemClickListener{
            when (it.itemId) {
                R.id.action_add -> {
                    // switch to record fragment
                    //navigateToRecordFragment()
                    true
                }
                else -> super.onOptionsItemSelected(it)
            }
        }
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}
