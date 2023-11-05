package com.example.ngiu.ui.rewards

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ngiu.R
import com.example.ngiu.data.entities.returntype.AccountIcon
import com.example.ngiu.databinding.FragmentRewardsBinding
import com.example.ngiu.ui.report.ReportAdapter
import kotlinx.android.synthetic.main.fragment_activity.*
import kotlinx.android.synthetic.main.fragment_report.recyclerview_report
import kotlinx.android.synthetic.main.fragment_rewards.*

class RewardsFragment : Fragment() {

    private lateinit var rewardsViewModel: RewardsViewModel
    private var _binding: FragmentRewardsBinding? = null

    private var cardAdapter: CardsAdapter? = null
    private var rewardsAdapter: RewardsAdapter? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        rewardsViewModel =
            ViewModelProvider(this)[RewardsViewModel::class.java]

        _binding = FragmentRewardsBinding.inflate(inflater, container, false)


        // load account icons
        rewardsViewModel.loadAccountIcons(requireContext())


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
                else -> true
            }
        }

        // todo
        //initRecyclerViewAdapter()

    }

    override fun onResume() {
        super.onResume()

        //rewards_iv.setImageBitmap( rewardsViewModel.getIcon(requireContext()).Icon_Image)

        loadCardAdapter(requireContext(), rewardsViewModel.accountIcons)


    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }



    private fun loadCardAdapter(context: Context, icons: List<AccountIcon>){
        /** Cards Adapter **/
        Thread {
            activity?.runOnUiThread {
                // pass the value to fragment from adapter when item clicked
                cardAdapter =
                    this.context?.let {
                        CardsAdapter(object : CardsAdapter.OnClickListener {

                            // catch the item click event from adapter
                            override fun onItemClick(accountID: Long) {
                                // *** do something after clicked

                                // todo
                                //showRewards(context, accountID)
                            }
                        })
                    }

                cardAdapter?.setList(icons)


                // load viewpager2 adapter
                vp_rewards_cards.offscreenPageLimit = 3

                // set cards style
                vp_rewards_cards.setPageTransformer { page, position ->
                    //
                    val minScale = 0.85f
                    val defaultCenter = 0.5f
                    val pageWidth = page.width.toFloat()
                    val pageHeight = page.height.toFloat()

                    page.pivotX = pageWidth / 2
                    page.pivotY = pageHeight / 2

                    if (position < -1){
                        // [-Infinity, -1)
                        // off-screen to the left
                        page.scaleX = minScale
                        page.scaleY = minScale
                        page.pivotX = pageWidth
                    }else if (position <=1) {
                        // [-1, 1]
                        // shrink the page
                        if (position < 0) {
                            // [0, -1] [-1, 0]
                            val scaleFactor = (1 + position) * (1 - minScale) + minScale
                            page.scaleX = scaleFactor
                            page.scaleY = scaleFactor
                            page.pivotX =
                                pageWidth * (defaultCenter + (defaultCenter * -position))
                        } else {
                            // [1, 0] [0, 1]
                            val scaleFactor = (1 - position) * (1 - minScale) + minScale
                            page.scaleX = scaleFactor
                            page.scaleY = scaleFactor
                            page.pivotX = pageWidth * ((1 - position) * defaultCenter)
                        }
                    } else {
                        // (1, +Infinity]
                        page.pivotX = 0f
                        page.scaleX = minScale
                        page.scaleY = minScale
                    }
                }

                vp_rewards_cards.adapter = cardAdapter
            }
        }.start()

    }

    private fun initRecyclerViewAdapter(){

        /** Rewards Adapter **/
        Thread {
            this.activity?.runOnUiThread {

                recyclerView_rewards.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL,false)
                rewardsAdapter = this.context?.let {
                    RewardsAdapter(object: RewardsAdapter.OnClickListener {
                        // catch the item click event from adapter
                        override fun onItemClick(rewardsID: Long) {

                            // switch to report detail fragment
                        }
                    })
                }

                recyclerView_rewards.adapter = rewardsAdapter
            }
        }.start()
    }
    
    private fun showRewards(context: Context, accountID: Long) {

        rewardsViewModel.setRewardListByAccountID(context, accountID)

        //Thread {
            // todo
        //    activity?.runOnUiThread {



                //rewardsAdapter?.setList(rewardsViewModel.rewardList)
                //recyclerView_rewards.adapter = rewardsAdapter
         //   }
        //}.start()
    }
}
