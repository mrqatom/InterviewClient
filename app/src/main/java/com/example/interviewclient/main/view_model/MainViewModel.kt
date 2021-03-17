package com.example.interviewclient.main.view_model

import android.app.Application
import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.interviewclient.base.BaseViewModel
import com.example.interviewclient.bean.AppInfo
import com.example.interviewclient.bean.RecommendInfo
import com.example.interviewclient.repository.RemoteRepository
import com.example.interviewclient.util.PackageInfoUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * @author qiuyunfei
 * @date 2021/3/15 0015
 * @description
 */
class MainViewModel(application: Application) : BaseViewModel(application) {
    val appInfo = MutableLiveData<MutableList<AppInfo>>()
    val appRecommend = MutableLiveData<MutableList<RecommendInfo>>()

    /**
     * 去下载的应用在appRecommend的position
     */
    var recommendInstallPosition = 0
    private val remoteRepository by lazy {
        RemoteRepository()
    }

    /**
     * 查询已安装的app
     */
    fun getAppInfo(context: Context?) {
        viewModelScope.launch(Dispatchers.IO) {
            val info = PackageInfoUtil.getPackages(context)
            withContext(Dispatchers.Main) {
                appInfo.value = info
            }
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
            withContext(Dispatchers.Main) {
                appRecommend.value = recommendInfo
            }
        }
    }

    fun checkInstallAndUpdate(context: Context, updateCallback: (Int) -> Unit) {
        if (PackageInfoUtil.isApplicationAvailable(
                context,
                appRecommend.value?.get(recommendInstallPosition)?.packageName ?: ""
            )
        ) {
            appRecommend.value?.removeAt(recommendInstallPosition)
            updateCallback(recommendInstallPosition)
            getAppInfo(context)
        }
    }

    fun uninstallApp(position: Int) {
        appInfo.value?.removeAt(position)
    }

}