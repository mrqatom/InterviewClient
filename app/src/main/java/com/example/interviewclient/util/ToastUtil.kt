package com.example.interviewclient.util

/**
 * @author qiuyunfei
 * @date 2019/8/9 0009
 * @description
 */

import android.content.Context
import android.widget.Toast


object ToastUtil {

    fun showShort(context: Context?, msg: String) {
        context?.run {
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
        }
    }

    fun showLong(context: Context?, msg: String) {
        context?.run {
            Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
        }
    }
}
