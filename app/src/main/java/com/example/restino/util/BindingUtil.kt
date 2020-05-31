package com.example.restino.util

import android.graphics.drawable.Drawable
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.example.restino.data.remote.responceAllProduct.ProductsItem
import kotlinx.android.synthetic.main.item_product.view.*


@BindingAdapter("imageUrl")
fun ImageView.loadImage(url: String?) {
    Glide.with(this.rootView).load("${Constance.BASE_URL}${url}").apply(GlideInstence.options).into(this)
}
@BindingAdapter("textprice")
fun TextView.setCardNumberText(item: ProductsItem?) {
    item?.let {
        text = " ${item.price.toString().NumberEnToFarsi()} ریال"
    }
}