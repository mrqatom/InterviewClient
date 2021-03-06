package com.example.interviewclient.util

import android.app.usage.StorageStats
import android.app.usage.StorageStatsManager
import android.content.Context
import android.content.pm.*
import android.os.Build
import android.os.storage.StorageManager
import android.util.Log
import androidx.annotation.RequiresApi
import java.io.IOException
import java.lang.reflect.Method


object PackageInfoUtil {
    const val ERROR_CODE_NOT_PERMISSION = -1
    const val ERROR_CODE_OTHER = -2

    fun queryAppSize(
        context: Context,
        packageName: String?,
        successCallback: (Long) -> Unit,
        errorCallBack: (Int) -> Unit
    ) {
        packageName?.run {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                getAppSizeO(context, this, successCallback, errorCallBack)
            } else {
                getAppSize(context, this, successCallback, errorCallBack)
            }
        }
    }

    /**
     * 获取应用的大小
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    fun getAppSizeO(
        context: Context,
        packageName: String?,
        successCallback: (Long) -> Unit,
        errorCallBack: (Int) -> Unit
    ) {
        val curTime = System.currentTimeMillis()
        val storageStatsManager =
            context.getSystemService(Context.STORAGE_STATS_SERVICE) as StorageStatsManager
        val uid: Int = getUid(context, packageName)
        //通过包名获取uid
        var storageStats: StorageStats? = null
        try {
            storageStats = storageStatsManager.queryStatsForUid(StorageManager.UUID_DEFAULT, uid)
            Log.e(
                "getAppSizeO",
                "getAppSize:${System.currentTimeMillis() - curTime}"
            )
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: SecurityException) {
            //这里说明没有权限，没有权限只能查询自身应用大小
            errorCallBack(ERROR_CODE_NOT_PERMISSION)
            return
        }
        successCallback(storageStats?.appBytes ?: 0)
        Log.e(
            "getAppSizeO",
            "${packageName}:appBytes:${storageStats?.appBytes},dataBytes${storageStats?.dataBytes}"
        )
    }

    /**
     * 根据应用包名获取对应uid
     */
    private fun getUid(context: Context, pakName: String?): Int {
        try {
            return context.packageManager
                .getApplicationInfo(pakName!!, PackageManager.GET_META_DATA).uid
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        return -1
    }

    /**
     * 获取应用大小8.0以下
     */
    private fun getAppSize(
        context: Context,
        packageName: String?,
        successCallback: (Long) -> Unit,
        errorCallBack: (Int) -> Unit
    ) {
        try {
            val method: Method =
                PackageManager::class.java.getMethod(
                    "getPackageSizeInfo", String::class.java, IPackageStatsObserver::class.java
                )
            // 调用 getPackageSizeInfo 方法，需要两个参数：1、需要检测的应用包名；2、回调
            method.invoke(
                context.packageManager,
                packageName,
                object : IPackageStatsObserver.Stub() {
                    override fun onGetStatsCompleted(
                        pStats: PackageStats,
                        succeeded: Boolean
                    ) {
                        Log.e(
                            "getAppSize",
                            "${packageName}:appBytes:${pStats.dataSize},dataBytes${pStats.codeSize}"
                        )
                        successCallback(pStats.codeSize)
                    }
                })
        } catch (e: Exception) {
            errorCallBack(ERROR_CODE_OTHER)
            e.printStackTrace()
        }
    }

    /**
     * 判断手机是否安装某个应用
     * @param context
     * @param appPackageName  应用包名
     * @return   true：安装，false：未安装
     */
    fun isApplicationAvailable(
        context: Context?,
        appPackageName: String
    ): Boolean {
        context?.run {
            val pinfo: List<PackageInfo> =
                packageManager.getInstalledPackages(0) // 获取所有已安装程序的包信息
            for (i in pinfo.indices) {
                val pn: String = pinfo[i].packageName
                if (appPackageName == pn) {
                    return true
                }
            }
        }
        return false
    }

}