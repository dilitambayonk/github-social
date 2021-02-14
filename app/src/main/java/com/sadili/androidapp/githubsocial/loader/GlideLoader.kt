package com.sadili.androidapp.githubsocial.loader

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

fun ImageView.loadImage(url: String?) {
    Glide.with(this.context)
        .load(url)
        .apply(RequestOptions().override(55, 55))
        .into(this)
}