package com.example.interviewclient.bean

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.android.parcel.Parcelize

/**
 * 应用图标、应用名称、版本、应用包名、应用大小、安装时间、更新时间
 */
@Keep
@Parcelize
data class AppInfo(
    var icon: Bitmap,
    var appName: String,
    var version: String?,
    var packageName: String,
    var size: Long = 0,
    var installTime: Long,
    var lastUpdateTime: Long
) : Parcelable