package com.example.interviewclient.util

import android.app.usage.StorageStats
import android.app.usage.StorageStatsManager
import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.IPackageStatsObserver
import android.content.pm.PackageManager
import android.content.pm.PackageStats
import android.os.Build
import android.os.storage.StorageManager
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.interviewclient.bean.AppInfo
import com.example.interviewclient.util.PermissionUtil.requestPermission
import java.io.IOException
import java.lang.reflect.Method
import kotlin.collections.ArrayList


object PackageInfoUtil {

    fun getPackages(context: Context): ArrayList<AppInfo> {
        val packages = context.packageManager.getInstalledPackages(0)
        val infos = ArrayList<AppInfo>()
        packages.forEachIndexed { index, packageInfo ->
            val curTime = System.currentTimeMillis()
            if ((packageInfo.applicationInfo.flags and ApplicationInfo.FLAG_SYSTEM) == 0) {
                val appInfo = AppInfo(
                    icon = packageInfo.applicationInfo.loadIcon(context.packageManager),
                    appName = packageInfo.applicationInfo.loadLabel(context.packageManager)
                        .toString(),
                    version = packageInfo.versionName,
                    installTime = packageInfo.firstInstallTime,
                    lastUpdateTime = packageInfo.lastUpdateTime,
                    packageName = packageInfo.packageName
                )
                infos.add(appInfo)
//                queryAppSize(context, packageInfo.packageName)
            }
            Log.e(
                "getPackageInfo",
                "$index,getAppSize:${System.currentTimeMillis() - curTime}"
            )
        }
        return infos
    }


    fun queryAppSize(context: Context, packageName: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            getAppSizeO(context, packageName)
        } else {
            getAppsize(context, packageName)
        }
    }

    /**
     * 获取应用的大小
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    fun getAppSizeO(context: Context, packageName: String) {
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
            requestPermission(context)
            return
        }


        Log.e(
            "getAppSizeO",
            "$packageName:appBytes:${storageStats?.appBytes},dataBytes${storageStats?.dataBytes}"
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
    fun getAppsize(context: Context, packageName: String) {
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
                            "getAppSizeO",
                            "$packageName:appBytes:${pStats.dataSize},dataBytes${pStats.codeSize}"
                        )
                    }
                })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}