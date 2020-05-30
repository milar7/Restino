package com.example.restino.util

import com.bumptech.glide.request.RequestOptions
import com.example.restino.R

object GlideInstence {

    val options = RequestOptions()
        .placeholder(R.drawable.ic_image_black_24dp)
        .error(R.drawable.ic_error_outline_black_24dp)



    //Glide.with(this).load(image_url).apply(options).into(imageView);
}