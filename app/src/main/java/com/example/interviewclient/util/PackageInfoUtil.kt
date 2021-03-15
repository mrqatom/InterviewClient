package com.example.interviewclient.util

import android.app.usage.StorageStats
import android.app.usage.StorageStatsManager
import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.IPackageStatsObserver
import android.content.pm.PackageManager
import android.content.pm.PackageStats
import android.graphics.drawable.BitmapDrawable
import android.os.Build
import android.os.storage.StorageManager
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.graphics.drawable.toBitmap
import com.example.interviewclient.bean.AppInfo
import com.example.interviewclient.util.PermissionUtil.requestPermission
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException
import java.lang.reflect.Method
import kotlin.collections.ArrayList


object PackageInfoUtil {
    const val ERROR_CODE_NOT_PERMISSION = -1
    const val ERROR_CODE_OTHER = -2

    fun getPackages(context: Context?): ArrayList<AppInfo> {
        val infos = ArrayList<AppInfo>()
        context?.run {
            val packages = packageManager.getInstalledPackages(0)
            packages.forEachIndexed { index, packageInfo ->
                val curTime = System.currentTimeMillis()
                if ((packageInfo.applicationInfo.flags and ApplicationInfo.FLAG_SYSTEM) == 0) {
                    val appInfo = AppInfo(
                        icon = packageInfo.applicationInfo.loadIcon(packageManager).toBitmap(),
                        appName = packageInfo.applicationInfo.loadLabel(packageManager)
                            .toString(),
                        version = packageInfo.versionName,
                        installTime = packageInfo.firstInstallTime,
                        lastUpdateTime = packageInfo.lastUpdateTime,
                        packageName = packageInfo.packageName
                    )
                    infos.add(appInfo)
                }
                Log.e(
                    "getPackageInfo",
                    "$index,getAppSize:${System.currentTimeMillis() - curTime}"
                )
            }
        }

        return infos
    }


    fun queryAppSize(context: Context, appInfo: AppInfo?,errorCallBack:(Int) ->Unit) {
        appInfo?.run {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                getAppSizeO(context, this,errorCallBack)
            } else {
                getAppsize(context, this,errorCallBack)
            }
        }
    }

    /**
     * 获取应用的大小
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    fun getAppSizeO(context: Context, appInfo: AppInfo,errorCallBack:(Int) ->Unit) {
        val curTime = System.currentTimeMillis()
        val storageStatsManager =
            context.getSystemService(Context.STORAGE_STATS_SERVICE) as StorageStatsManager
        val uid: Int = getUid(context, appInfo.packageName)
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
        appInfo.size = storageStats?.appBytes ?: 0
        Log.e(
            "getAppSizeO",
            "${appInfo.packageName}:appBytes:${storageStats?.appBytes},dataBytes${storageStats?.dataBytes}"
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
    fun getAppsize(context: Context, appInfo: AppInfo,errorCallBack:(Int) ->Unit) {
        try {
            val method: Method =
                PackageManager::class.java.getMethod(
                    "getPackageSizeInfo", String::class.java, IPackageStatsObserver::class.java
                )
            // 调用 getPackageSizeInfo 方法，需要两个参数：1、需要检测的应用包名；2、回调
            method.invoke(
                context.packageManager,
                appInfo.packageName,
                object : IPackageStatsObserver.Stub() {
                    override fun onGetStatsCompleted(
                        pStats: PackageStats,
                        succeeded: Boolean
                    ) {
                        appInfo.size = pStats.dataSize
                        Log.e(
                            "getAppSizeO",
                            "${appInfo.packageName}:appBytes:${pStats.dataSize},dataBytes${pStats.codeSize}"
                        )
                    }
                })
        } catch (e: Exception) {
            errorCallBack(ERROR_CODE_OTHER)
            e.printStackTrace()
        }
    }

}