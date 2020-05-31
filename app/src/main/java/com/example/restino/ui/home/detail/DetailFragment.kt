package com.example.restino.ui.home.detail

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.restino.R
import com.example.restino.data.model.ProductInfo
import com.example.restino.databinding.FragmentDetailBinding

class DetailFragment : Fragment() {



    private lateinit var viewModel: DetailViewModel
    private lateinit var binding: FragmentDetailBinding
    private lateinit var proInfoListAdapter: ProInfoListAdapter
    private var proCount: Int = 0


    private var proInfos = mutableListOf<ProductInfo>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        (activity as AppCompatActivity).supportActionBar?.show()
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_detail, container, false)
        binding.lifecycleOwner = this


        return inflater.inflate(R.layout.fragment_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        if (proInfos.isEmpty()) {

            proInfos.add(ProductInfo(title = "حجم", value = "33 سی سی"))
            proInfos.add(ProductInfo(title = "عصاره", value = "دارد"))
            proInfos.add(ProductInfo(title = "حاوی نرم کننده", value = "بله"))
            proInfos.add(ProductInfo(title = "حجم", value = "33 سی سی"))
            proInfos.add(ProductInfo(title = "حجم", value = "33 سی سی"))
            proInfos.add(ProductInfo(title = "حجم", value = "33 سی سی"))
        }

        setUpCount()
        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        binding.rvInfo.apply {
            layoutManager= LinearLayoutManager(activity)
            proInfoListAdapter= ProInfoListAdapter()
            adapter=proInfoListAdapter

        }
        proInfoListAdapter.submitList(proInfos)
    }

    private fun setUpCount() {
        binding.btnPlus.setOnClickListener {
            proCount++
            binding.tvCount.text = proCount.toString()
        }
        binding.btnMines.setOnClickListener {
            if (proCount != 0) {
                proCount--
                binding.tvCount.text = proCount.toString()
            }

        }

    }
}