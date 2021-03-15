package com.example.interviewclient.util

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.*


/**
 * @author qiuyunfei
 * @date 2021/3/15 0015
 * @description
 */
object TimeUtil {

    fun formatData(timeStamp: Long): String {
        if (timeStamp == 0L) {
            return ""
        }
        var result = ""
        @SuppressLint("SimpleDateFormat") val format =
            SimpleDateFormat("dd-MM-yyyy HH:mm:ss")
        result = format.format(Date(timeStamp))
        return result
    }
}