package com.example.restino.ui.home.basket

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.restino.R
import com.example.restino.data.model.Product
import com.example.restino.util.NumberEnToFarsi
import kotlinx.android.synthetic.main.item_basket.view.*

class BasketAdapter(private val basketInteraction: BasketInteraction? = null) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Product>() {

        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem == newItem
        }

    }
    private val differ = AsyncListDiffer(this, DIFF_CALLBACK)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return BasketViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_basket,
                parent,
                false
            ),
            basketInteraction
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is BasketViewHolder -> {
                holder.bind(differ.currentList.get(position))
            }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    fun submitList(list: List<Product>) {
        differ.submitList(list)
    }

    class BasketViewHolder
    constructor(
        itemView: View,
        private val basketInteraction: BasketInteraction?
    ) : RecyclerView.ViewHolder(itemView) {

        fun bind(item: Product) = with(itemView) {
            itemView.btn_delete_product.setOnClickListener {
                basketInteraction?.onDeleteItemSelected(adapterPosition, item)
            }
                itemView.tv_product_name.text=item.name
                itemView.tv_product_brans.text=item.brand
                itemView.tv_product_price.text="${item.price.toString().NumberEnToFarsi()}ریال"
                itemView.tv_count.text=item.count
        }
    }

    interface BasketInteraction {
        fun onDeleteItemSelected(position: Int, item: Product)
    }
}
