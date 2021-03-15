package com.example.interviewclient.app_info_detail.view_model

import android.app.Application
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.atom.myfirstkotlinproject.utils.extensions.switchAndAutoDispose
import com.example.interviewclient.base.BaseViewModel
import com.example.interviewclient.bean.AppInfo
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
class AppInfoDetailViewModel(application: Application) : BaseViewModel(application) {
    val appInfo: MutableLiveData<AppInfo> = MutableLiveData()
    val appSize: MutableLiveData<Long> = MutableLiveData()

    fun getAppSize(context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            PackageInfoUtil.queryAppSize(context, appInfo.value)
            withContext(Dispatchers.Main) {
                appSize.value = appInfo.value?.size
            }
        }
    }
}