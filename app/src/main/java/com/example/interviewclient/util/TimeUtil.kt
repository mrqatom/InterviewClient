package com.example.interviewclient.util

import java.text.SimpleDateFormat
import java.util.*


/**
 * @author qiuyunfei
 * @date 2021/3/15 0015
 * @description
 */
object TimeUtil {

    fun timeStamp2Date(seconds: Long): String {
        val format = "yyyy-MM-dd HH:mm"
        val sdf = SimpleDateFormat.getTimeInstance().format(format)
        return sdf.format(Date(seconds))
    }


}