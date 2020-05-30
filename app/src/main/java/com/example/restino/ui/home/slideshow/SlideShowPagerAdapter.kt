package com.example.restino.ui.home.slideshow

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import com.example.restino.R
import kotlinx.android.synthetic.main.item_slide_show.view.*

class SlideShowPagerAdapter(
    val mcontext: Context,
    val slides: List<Slide>
) : PagerAdapter() {

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val inflater: LayoutInflater =
            mcontext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.item_slide_show, null)
        val img = view.img_slide_show
        img.setImageResource(slides[position].image)
        container.addView(view)
        return view
    }

    override fun getCount(): Int = slides.size

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }


}