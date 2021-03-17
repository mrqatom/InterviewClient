package com.example.interviewclient.repository

import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.util.Log
import com.example.interviewclient.bean.RecommendInfo
import java.lang.Exception

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

    fun getRecommendApp(context: Context): MutableList<RecommendInfo>? {
        val uri: Uri =
            Uri.parse("content://com.example.interviewservice.provider.RecommendAppProvider")
        var recommendCursor: Cursor? = null
        try {
            recommendCursor = context.contentResolver.query(uri, null, null, null, null)
        } catch (e: Exception) {
            Log.e(TAG, "服务器出错或连接服务器失败!")
        }
        recommendCursor?.run {
            val info = mutableListOf<RecommendInfo>()
            while (recommendCursor.moveToNext()) {
                info.add(
                    RecommendInfo(
                        recommendCursor.getString(0),
                        recommendCursor.getString(1),
                        recommendCursor.getString(2),
                        recommendCursor.getString(3)
                    )
                )
            }
            recommendCursor.close()
            return info
        }
        return null
    }

}