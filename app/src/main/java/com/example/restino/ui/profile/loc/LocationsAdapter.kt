package com.example.restino.ui.profile.loc

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.restino.R
import com.example.restino.data.remote.responceLocations.ResponceLocationsItem
import com.example.restino.util.NumberEnToFarsi
import kotlinx.android.synthetic.main.item_location.view.*

class LocationsAdapter(private val interaction: Interaction? = null) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ResponceLocationsItem>() {

        override fun areItemsTheSame(
            oldItem: ResponceLocationsItem,
            newItem: ResponceLocationsItem
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: ResponceLocationsItem,
            newItem: ResponceLocationsItem
        ): Boolean {
            return oldItem == newItem
        }

    }
    private val differ = AsyncListDiffer(this, DIFF_CALLBACK)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return LocationsViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_location,
                parent,
                false
            ),
            interaction
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is LocationsViewHolder -> {
                holder.bind(differ.currentList.get(position))
            }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    fun submitList(list: List<ResponceLocationsItem>) {
        differ.submitList(list)
    }

    class LocationsViewHolder
    constructor(
        itemView: View,
        private val interaction: Interaction?
    ) : RecyclerView.ViewHolder(itemView) {

        fun bind(item: ResponceLocationsItem) = with(itemView) {
            itemView.btn_edit_loc.setOnClickListener {
                interaction?.onItemSelected(adapterPosition, item)
            }
            itemView.tv_address.text = " آدرس:${item.address}"
            itemView.tv_code.text = " کد پستی :${item.zip_code.NumberEnToFarsi()}"
            itemView.tv_city.text= "شهر : اصفهان"
            itemView.tv_provinces.text="استان : اصفهان"
        }
    }

    interface Interaction {
        fun onItemSelected(position: Int, item: ResponceLocationsItem)
    }
}
