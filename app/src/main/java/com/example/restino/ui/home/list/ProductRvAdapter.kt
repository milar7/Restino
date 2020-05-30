package com.example.restino.ui.home.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.restino.R
import com.example.restino.util.Constance
import com.example.restino.util.GlideInstence
import com.example.restinoapp.data.remote.ResponceAllProducts.ProductsItem
import kotlinx.android.synthetic.main.item_product.view.*


class ProductRvAdapter(private val interaction: Interaction? = null) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ProductsItem>() {

        override fun areItemsTheSame(oldItem: ProductsItem, newItem: ProductsItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ProductsItem, newItem: ProductsItem): Boolean {
            return oldItem== newItem
        }

    }
    private val differ = AsyncListDiffer(this, DIFF_CALLBACK)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return ProductViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_product,
                parent,
                false
            ),
            interaction
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ProductViewHolder -> {
                holder.bind(differ.currentList.get(position))
            }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    fun submitList(list: List<ProductsItem>) {
        differ.submitList(list)
    }

    class ProductViewHolder
    constructor(
        itemView: View,
        private val interaction: Interaction?
    ) : RecyclerView.ViewHolder(itemView) {

        fun bind(item: ProductsItem) = with(itemView) {
            itemView.btn_product.setOnClickListener {
                interaction?.onItemSelected(adapterPosition, item)
            }

            itemView.apply {
                Glide.with(this).load("${Constance.BASE_URL}${item.image}").apply(GlideInstence.options).into(img_product)
                title_product.text=item.name
                img_price.text=item.price.toString()
            }
        }
    }

    interface Interaction {
        fun onItemSelected(position: Int, item: ProductsItem)
    }
}
