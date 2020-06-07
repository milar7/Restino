package com.example.restino.util

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.example.restino.data.remote.responceAllProduct.ProductsItem
import com.example.restino.data.remote.responseProfile.ProfileResponse


@BindingAdapter("imageUrl")
fun ImageView.loadImage(url: String?) {
    Glide.with(this.rootView).load("${Constance.BASE_URL}${url}").apply(GlideInstence.options).into(this)
}

@BindingAdapter("textFullName")
fun TextView.setfullname(item: ProfileResponse) {
    item?.let {
        text = "${it.first_name} ${it.last_name}"
    }
}

@BindingAdapter("textcategories")
fun TextView.setCategories(item: ProductsItem?) {
    item?.let {
        var txt= "رستینو"
        item.categories.forEach {
            txt="$txt / ${it.name}"
        }
        text=txt
    }
}


@BindingAdapter("textprice")
fun TextView.setCardNumberText(item: ProductsItem?) {
    item?.let {
        text = " ${item.price.toString().NumberEnToFarsi()} ریال"
    }
}