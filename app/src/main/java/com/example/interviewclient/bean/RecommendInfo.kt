package com.example.interviewclient.bean

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.android.parcel.Parcelize

/**
 * 应用图标、应用名称、版本、应用包名
 */
@Keep
data class RecommendInfo(
    var icon: String,
    var appName: String,
    var version: String?,
    var packageName: String
)