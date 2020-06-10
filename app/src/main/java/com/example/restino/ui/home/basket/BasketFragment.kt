package com.example.restino.ui.home.basket

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.restino.R
import com.example.restino.data.model.Product
import com.example.restino.databinding.FragmentBasketBinding


class BasketFragment : Fragment(), BasketAdapter.BasketInteraction {
    //
    private lateinit var binding: FragmentBasketBinding
    private lateinit var basketAdapter :BasketAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRv()

    }
    private fun initRv() {
        binding.rvBasket.apply {
            layoutManager=LinearLayoutManager(requireContext())
            basketAdapter= BasketAdapter(this@BasketFragment)
            adapter=basketAdapter
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_basket, container, false)
        binding.lifecycleOwner = this


        return binding.root

    }

    override fun onDeleteItemSelected(position: Int, item: Product) {


    }


}