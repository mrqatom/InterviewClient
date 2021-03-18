package com.example.interviewclient.main.view_model

import android.app.Application
import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.util.Log
import androidx.core.graphics.drawable.toBitmap
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.interviewclient.base.BaseViewModel
import com.example.interviewclient.bean.AppInfo
import com.example.interviewclient.bean.RecommendInfo
import com.example.interviewclient.repository.RemoteRepository
import com.example.interviewclient.util.PackageInfoUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * @author qiuyunfei
 * @date 2021/3/15 0015
 * @description
 */
class MainViewModel(application: Application) : BaseViewModel(application) {
    val appInfo = MutableLiveData<MutableList<AppInfo>>()
    val appRecommend = MutableLiveData<MutableList<RecommendInfo>>()

    /**
     * 下载了推荐列表中的应用位置
     */
    val installRecommendPosition = MutableLiveData<Int>()

    /**
     * 卸载了应用列表中的应用位置
     */
    val uninstallPosition = MutableLiveData<Int>()

    private val remoteRepository by lazy {
        RemoteRepository()
    }

    /**
     * 查询已安装的app
     */
    fun getAppInfo(context: Context?) {
        viewModelScope.launch(Dispatchers.IO) {
            val info = getPackages(context)
            appInfo.postValue(info)
        }
    }

    /**
     * 请求推荐app
     */
    fun getRecommendApp(context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            val recommendInfo = remoteRepository.getRecommendApp(context)?.filterNot { recommend ->
                var isInstall = false
                appInfo.value?.forEach {
                    if (it.packageName == recommend.packageName) {
                        isInstall = true
                    }
                }
                isInstall
            }?.toMutableList()
            appRecommend.postValue(recommendInfo)
        }
    }

    fun uninstallApp(packageName: String) {
        var uninstallIndex = -1
        appInfo.value?.forEachIndexed { index, appInfo ->
            if (appInfo.packageName == packageName) {
                uninstallIndex = index
                return@forEachIndexed
            }
        }
        if (uninstallIndex >= 0) {
            appInfo.value?.removeAt(uninstallIndex)
            uninstallPosition.value = uninstallIndex
        }
    }

    fun installApp(packageInfo: PackageInfo, context: Context) {
        appInfo.value?.add(0, makeAppInfo(packageInfo, context))
        var installIndex = -1
        appRecommend.value?.forEachIndexed { index, recommendInfo ->
            if (recommendInfo.packageName == packageInfo.packageName) {
                installIndex = index
                return@forEachIndexed
            }
        }
        if (installIndex >= 0) {
            appRecommend.value?.removeAt(installIndex)
            installRecommendPosition.value = installIndex
        }
    }

    /**
     * 获取已安装应用
     */
    private fun getPackages(context: Context?): ArrayList<AppInfo> {
        val ifs = ArrayList<AppInfo>()
        context?.let {
            val packages = it.packageManager.getInstalledPackages(0)
            packages.forEachIndexed { index, packageInfo ->
                val curTime = System.currentTimeMillis()
                if ((packageInfo.applicationInfo.flags and ApplicationInfo.FLAG_SYSTEM) == 0) {
                    ifs.add(makeAppInfo(packageInfo, it))
                }
                Log.e(
                    "getPackageInfo",
                    "$index,getAppSize:${System.currentTimeMillis() - curTime}"
                )
            }
        }
        return ifs
    }

    /**
     * 根据packageInfo返回一个appInfo
     */
    private fun makeAppInfo(packageInfo: PackageInfo, context: Context): AppInfo {
        return AppInfo(
            icon = packageInfo.applicationInfo.loadIcon(context.packageManager).toBitmap(),
            appName = packageInfo.applicationInfo.loadLabel(context.packageManager)
                .toString(),
            version = packageInfo.versionName,
            installTime = packageInfo.firstInstallTime,
            lastUpdateTime = packageInfo.lastUpdateTime,
            packageName = packageInfo.packageName
        )
    }
}