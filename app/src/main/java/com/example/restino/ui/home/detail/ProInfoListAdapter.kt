package com.example.restino.ui.home.detail

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.restino.R
import com.example.restino.data.model.ProductInfo
import kotlinx.android.synthetic.main.item_rv_pro_info.view.*

class ProInfoListAdapter(private val interaction: Interaction? = null) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ProductInfo>() {

        override fun areItemsTheSame(oldItem: ProductInfo, newItem: ProductInfo): Boolean {
            return oldItem.id == newItem.id

        }

        override fun areContentsTheSame(oldItem: ProductInfo, newItem: ProductInfo): Boolean {
            return oldItem == newItem
        }

    }
    private val differ = AsyncListDiffer(this, DIFF_CALLBACK)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return ProductInfoViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_rv_pro_info,
                parent,
                false
            ),
            interaction
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ProductInfoViewHolder -> {
                holder.bind(differ.currentList.get(position))
            }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    fun submitList(list: List<ProductInfo>) {
        differ.submitList(list)
    }

    class ProductInfoViewHolder
    constructor(
        itemView: View,
        private val interaction: Interaction?
    ) : RecyclerView.ViewHolder(itemView) {

        fun bind(item: ProductInfo) = with(itemView) {
            itemView.setOnClickListener {
                interaction?.onItemSelected(adapterPosition, item)
            }
            itemView.tv_title.text=item.title
            itemView.tv_value.text=item.value
        }
    }

    interface Interaction {
        fun onItemSelected(position: Int, item: ProductInfo)
    }
}
