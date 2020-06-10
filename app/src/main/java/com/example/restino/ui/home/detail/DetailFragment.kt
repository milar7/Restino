package com.example.restino.ui.home.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.afollestad.materialdialogs.MaterialDialog
import com.bumptech.glide.Glide
import com.example.restino.R
import com.example.restino.data.model.ProductInfo
import com.example.restino.data.remote.responceAllProduct.ProductsItem
import com.example.restino.databinding.FragmentDetailBinding
import com.example.restino.util.Constance
import com.example.restino.util.CurrentFragment
import com.example.restino.util.GlideInstence
import com.example.restino.util.InjectorUtil

class DetailFragment : Fragment() {


    private lateinit var viewModel: DetailViewModel
    private lateinit var binding: FragmentDetailBinding
    private lateinit var proInfoListAdapter: ProInfoListAdapter
    private var proCount: Int = 0
    private lateinit var product: ProductsItem
    val args: DetailFragmentArgs by navArgs()

    private var proInfos = mutableListOf<ProductInfo>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        CurrentFragment.curr = Constance.Detail

        (activity as AppCompatActivity).supportActionBar?.show()
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_detail, container, false)
        binding.lifecycleOwner = this
        product = args.product
        binding.fbBack.setOnClickListener {
            findNavController().navigateUp()
        }



        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val viewModelProviderFactory = InjectorUtil.DetailViewModelProviderFactory(product)
        viewModel =
            ViewModelProvider(this, viewModelProviderFactory).get(DetailViewModel::class.java)
        binding.viewModel = viewModel

        binding.imgProduct.apply {
            Glide.with(this).load("${Constance.BASE_URL}${product.image}")
                .apply(GlideInstence.options).into(binding.imgProduct)
        }

        if (proInfos.isEmpty()) {
            args.product.properties.forEach {
                proInfos.add(ProductInfo(title = it.name, value = it.value))
            }
        }

        binding.btnAddToCard.setOnClickListener {
            //TODO check for signin
            
//            if (true) {
//                goForSignIn("برای اضافه کردن به سبد خرید باید وارد شوید",
//                object :GoForSignInCallback{
//                    override fun proceed() {
//                        findNavController().navigate(
//                            DetailFragmentDirections.actionDetailFragmentToLoginFragment()
//                        )
//                    }
//
//                    override fun cancel() {
//                        //Toast.makeText(context, "cancelled", Toast.LENGTH_SHORT).show()
//                    }
//
//                })
//            }
        }

        setUpCount()
        setupRecyclerView()
    }

    fun goForSignIn(message: String, callback: GoForSignInCallback) {
        MaterialDialog(this!!.requireContext())
            .show {
                title(text = "ورود")
                message(text = message)
                negativeButton(text = "خیر") {
                    callback.cancel()
                }
                positiveButton(text = "ورود") {

                    callback.proceed()
                }
            }
    }

    interface GoForSignInCallback {
        fun proceed()
        fun cancel()
    }


    private fun setupRecyclerView() {
        binding.rvInfo.apply {
            layoutManager = LinearLayoutManager(activity)
            proInfoListAdapter = ProInfoListAdapter()
            adapter = proInfoListAdapter

        }
        proInfoListAdapter.submitList(proInfos)
    }


    //TODO should be in view model
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