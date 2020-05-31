package com.example.restino.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.restino.R
import com.example.restino.data.Result
import com.example.restino.data.remote.responceAllProduct.ProductsItem
import com.example.restino.data.repository.RestinoRepository
import com.example.restino.databinding.FragmentHomeBinding
import com.example.restino.ui.home.list.ProductRvAdapter
import com.example.restino.ui.home.slideshow.Slide
import com.example.restino.ui.home.slideshow.SlideShowPagerAdapter
import com.example.restino.util.InjectorUtil
import com.example.restino.util.NumberEnToFarsi
import com.example.restino.util.hide
import com.example.restino.util.show
import java.util.*
private const val TAG = "HomeFragment"

class HomeFragment : Fragment(), ProductRvAdapter.Interaction {

    private lateinit var viewModel: HomeViewModel
    private lateinit var binding: FragmentHomeBinding
    private lateinit var slideAdapter: SlideShowPagerAdapter
    private lateinit var productRvAdapter: ProductRvAdapter

    companion object {
        val slides = mutableListOf<Slide>()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        binding.lifecycleOwner = this
        (activity as AppCompatActivity).supportActionBar?.show()
        val restinoRepository = RestinoRepository()
        val viewModelProviderFactory = InjectorUtil.HomeViewModelProviderFactory(restinoRepository)
        viewModel = ViewModelProvider(this, viewModelProviderFactory).get(HomeViewModel::class.java)

        subscribeUi()
        setupSlideshow()
        initRecyclerView()
        setupSwipeRefresh()
        return binding.root
    }

    private fun initRecyclerView() {
        binding.recyclerViewHome.apply {

            //TODO enter animation
            //TODO place holder loading
            layoutManager = GridLayoutManager(context, 2)
            productRvAdapter = ProductRvAdapter(this@HomeFragment)
            adapter = productRvAdapter
        }

    }

    private fun setupSwipeRefresh() {
        binding.swipeView.setSwipeableChildren(binding.recyclerViewHome.id)

        binding.swipeView.setOnRefreshListener(object : SwipeRefreshLayout.OnRefreshListener {
            override fun onRefresh() {
                viewModel.getNewProducts()
                binding.swipeView.isRefreshing = false
            }

        })
    }

    private fun subscribeUi() {
        viewModel.newProducts.observe(viewLifecycleOwner, androidx.lifecycle.Observer { response ->
            when (response) {
                is Result.Success -> {
                    binding.pbHome.hide()
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
        val timer = Timer()
        timer.scheduleAtFixedRate(SliderTimer(), 4000, 6000)
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

    override fun onItemSelected(position: Int, item: ProductsItem) {
        findNavController().navigate(
            HomeFragmentDirections.actionHomeFragmentToDetailFragment(item)
        )

    }


}