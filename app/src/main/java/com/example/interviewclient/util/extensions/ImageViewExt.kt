package com.example.interviewclient.util.extensions

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.example.interviewclient.R


/**
 * 加载图片 By Url
 */
fun ImageView.loadImage(url: String) =
    Glide.with(this)
        .load(url)
        .placeholder(R.mipmap.ic_launcher_round)
        .error(R.mipmap.no_pic)
        .into(this)