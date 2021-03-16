package com.example.interviewclient.repository

import android.content.Context
import android.net.Uri
import android.util.Log
import com.example.interviewclient.bean.RecommendInfo

/**
 * @author qiuyunfei
 * @date 2021/3/16 0016
 * @description 远程数据获取
 * 实际项目中应该是网路请求或者数据库
 */
class RemoteRepository {
    companion object {
        private const val TAG = "RemoteRepository"
    }

    fun getRecommendApp(context: Context): List<RecommendInfo>? {
        val uri: Uri =
            Uri.parse("content://com.example.interviewservice.provider.RecommendAppProvider")
        val recommendCursor = context.contentResolver.query(uri, null, null, null, null)
        recommendCursor?.run {
            val info = listOf<RecommendInfo>()
            while (recommendCursor.moveToNext()) {
                Log.d(TAG, "1: " + recommendCursor.getString(0))
                Log.d(TAG, "2: " + recommendCursor.getString(1))
                Log.d(TAG, "3: " + recommendCursor.getString(2))
                Log.d(TAG, "4: " + recommendCursor.getString(3))
            }
            recommendCursor.close()
            return info
        }
        return null
    }

}