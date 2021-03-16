package com.example.interviewclient.main.view_model

import android.app.Application
import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.atom.myfirstkotlinproject.utils.extensions.switchAndAutoDispose
import com.example.interviewclient.base.BaseViewModel
import com.example.interviewclient.bean.AppInfo
import com.example.interviewclient.bean.RecommendInfo
import com.example.interviewclient.repository.RemoteRepository
import com.example.interviewclient.util.PackageInfoUtil
import io.reactivex.Observable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * @author qiuyunfei
 * @date 2021/3/15 0015
 * @description
 */
class MainViewModel(application: Application) : BaseViewModel(application) {
    val appInfo: MutableLiveData<MutableList<AppInfo>> = MutableLiveData()
    val appRecommend: MutableLiveData<List<RecommendInfo>> = MutableLiveData()
    val remoteRepository by lazy {
        RemoteRepository()
    }

    fun getAppInfo(context: Context?) {
        viewModelScope.launch(Dispatchers.IO) {
            val info = PackageInfoUtil.getPackages(context)
            withContext(Dispatchers.Main) {
                appInfo.value = info
            }
        }
    }

    fun getRecommendApp(context: Context) {
        remoteRepository.getRecommendApp(context)
    }

    fun uninstallApp(position: Int) {
        appInfo.value?.removeAt(position)
    }

}