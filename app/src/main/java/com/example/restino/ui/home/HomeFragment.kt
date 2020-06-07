package com.example.restino.ui.home

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.restino.R
import com.example.restino.data.Result
import com.example.restino.data.remote.responceAllProduct.ProductsItem
import com.example.restino.databinding.FragmentHomeBinding
import com.example.restino.ui.MainActivity
import com.example.restino.ui.home.list.ProductRvAdapter
import com.example.restino.ui.home.slideshow.Slide
import com.example.restino.ui.home.slideshow.SlideShowPagerAdapter
import com.example.restino.util.*
import java.lang.Runnable
import java.util.*

private const val TAG = "HomeFragment"

class HomeFragment : Fragment(), ProductRvAdapter.Interaction {

    private lateinit var viewModel: HomeViewModel
    private lateinit var binding: FragmentHomeBinding
    private lateinit var slideAdapter: SlideShowPagerAdapter
    private lateinit var productRvAdapter: ProductRvAdapter
    private var glayoutManager: GridLayoutManager? = null
    companion object {
        val slides = mutableListOf<Slide>()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        CurrentFragment.curr=Constance.HOME
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        binding.lifecycleOwner = this
        (activity as AppCompatActivity).supportActionBar?.hide()

        viewModel=(activity as MainActivity).viewModel


        subscribeUi()
        setupSlideshow()
        initRecyclerView()
        setupSwipeRefresh()
        setUpGridList()


        return binding.root

    }


    private fun initRecyclerView() {
        glayoutManager = GridLayoutManager(context, 2)
        binding.recyclerViewHome.apply {

            //TODO enter animation
            //TODO place holder loading
            layoutManager = glayoutManager
            productRvAdapter = ProductRvAdapter(glayoutManager,this@HomeFragment)
            adapter = productRvAdapter
        }
        Log.d(TAG, "initRecyclerView: ")
        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE) ?: return
        val itemPosition = sharedPref.getInt("itemPosition", 0)
        binding.nestedScroll.scrollTo(0,itemPosition*500)
        Log.d(TAG, "setupSwipeRefresh: $itemPosition")
       // ViewCompat.setNestedScrollingEnabled(  binding.recyclerViewHome, false)

    }
    override fun onItemSelected(position: Int, item: ProductsItem) {

        Log.d(TAG, "onItemSelected: $position")

        val itemPosition= if(position %2 ==0)position else position-1

        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE) ?: return
        with (sharedPref.edit()) {
            putInt("itemPosition", itemPosition)
            commit()
        }

        findNavController().navigate(
            HomeFragmentDirections.actionHomeFragmentToDetailFragment(item)
        )

    }

    private fun setupSwipeRefresh() {
        binding.swipeView.setSwipeableChildren(binding.recyclerViewHome.id)
        binding.swipeView.setOnRefreshListener {


           viewModel.getNewProducts()


            binding.swipeView.isRefreshing = false
        }
    }

    private fun subscribeUi() {



        viewModel.newProducts.observe(viewLifecycleOwner, androidx.lifecycle.Observer { response ->
            when (response) {
                is Result.Success -> {
                    binding.pbHome.hide()
                    binding.swipeView.fadeShow()
                    binding.imgLogo.fadeHide()
                    (activity as AppCompatActivity).supportActionBar?.show()
                    response.data?.let {
                        productRvAdapter.submitList(it)

                    }
                }
                is Result.Error -> {
                    binding.pbHome.hide()
                    response.message?.let {
                        Log.e("HOMEFRAGMENT", "An error occured: $it")
                    }
                }
                is Result.Loading ->
                    binding.pbHome.show()


            }
        })


    }

    // TODO bug : moving so fast
    @SuppressLint("ClickableViewAccessibility")
    private fun setupSlideshow() {
        if (slides.isEmpty()) {
            slides.add(Slide(R.drawable.slide1))
            slides.add(Slide(R.drawable.slide2))
            slides.add(Slide(R.drawable.slide3))
        }
        slideAdapter = SlideShowPagerAdapter(this!!.requireContext(), slides)
        binding.vPager.adapter = slideAdapter
        binding.vPager.currentItem = 0


        binding.tabIndicator.setupWithViewPager(binding.vPager, true)
//        var job :Job?=null
//        job = MainScope().launch {
//            delay(500L)
//            withContext(Dispatchers.Main){
//
//            }
//
//        }

        val timer = Timer()
        timer.scheduleAtFixedRate(SliderTimer(), 4000, 12000)
//        binding.vPager.setOnTouchListener { v, event ->
//
//            job?.cancel()
//            job = MainScope().launch {
//                delay(500L)
//                withContext(Dispatchers.Main){
//                    val timer = Timer()
//                    timer.scheduleAtFixedRate(SliderTimer(), 4000, 12000)
//                }
//
//            }
//            true
//        }



    }

    inner class SliderTimer : TimerTask() {
        override fun run() {

            activity?.runOnUiThread(object : Runnable {
                override fun run() {
                    if (binding.vPager.currentItem < HomeFragment.slides.size - 1) {
                        binding.vPager.currentItem = binding.vPager.currentItem + 1
                    } else {
                        binding.vPager.currentItem = 0
                    }
                }

            })
        }


    }

    override fun onDestroy() {
        super.onDestroy()
        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE) ?: return
        with (sharedPref.edit()) {
            putInt("itemPosition", 0)
            commit()
        }
    }


    private fun setUpGridList() {

//        binding.btnGrid.setOnClickListener {
//            if (glayoutManager?.spanCount==2) {
//                glayoutManager?.spanCount = 1
//                binding.btnGrid.setImageDrawable(resources.getDrawable(R.drawable.ic_baseline_grid_on_24))
//              //  Glide.with(requireContext()).load().into(binding.btnGrid)
//
//            }else{
//                glayoutManager?.spanCount = 2
//                //Glide.with(requireContext()).load(R.drawable.ic_baseline_format_align_justify_24).into(binding.btnGrid)
//                binding.btnGrid.setImageDrawable(resources.getDrawable(R.drawable.ic_baseline_format_align_justify_24))
//
//            }
//
//
//            productRvAdapter.notifyItemRangeChanged(0,productRvAdapter?.itemCount?:0)
//            // productRvAdapter.se
//        }
    }

}