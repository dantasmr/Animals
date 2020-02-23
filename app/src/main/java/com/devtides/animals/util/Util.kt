package com.devtides.animals.util

import android.content.Context
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.devtides.animals.R

fun getProgressDrawble(context : Context) : CircularProgressDrawable {
    return CircularProgressDrawable(context).apply {
        strokeWidth = 10f
        centerRadius = 50f
        start()
    }
}

fun ImageView.loadImage(uri : String?, progressDrawable: CircularProgressDrawable){
    val options : RequestOptions = RequestOptions()
        .placeholder(progressDrawable)
        .error(R.mipmap.ic_launcher_round)
        Glide.with(context)
        .setDefaultRequestOptions(options)
        .load(uri)
        .into(this)


}

@BindingAdapter("android:ImageUrl")
fun loadImage(view: ImageView, url: String){
    view.loadImage(url, getProgressDrawble(view.context))
}