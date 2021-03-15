package com.example.interviewclient.bean

import android.graphics.drawable.Drawable
import androidx.annotation.Keep

/**
 * 应用图标、应用名称、版本、应用包名、应用大小、安装时间、更新时间
 */
@Keep
data class AppInfo(
    var icon: Drawable,
    var appName: String,
    var version: String,
    var packageName: String,
    var size: Int = 0,
    var installTime: Long,
    var lastUpdateTime: Long
)