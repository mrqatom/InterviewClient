package com.example.interviewclient.app_info_detail.view_model

import android.app.Application
import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.interviewclient.base.BaseViewModel
import com.example.interviewclient.bean.AppInfo
import com.example.interviewclient.util.PackageInfoUtil
import com.example.interviewclient.util.PackageInfoUtil.ERROR_CODE_NOT_PERMISSION
import com.example.interviewclient.util.PermissionUtil
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

    /**
     * 是否需要请求APP_SIZE权限，默认不需要
     */
    val needRequestPermission: MutableLiveData<Boolean> = MutableLiveData()

    fun getAppSize(context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            PackageInfoUtil.queryAppSize(context, appInfo.value?.packageName,{
                appSize.postValue(it)
            }) {
                if (it == ERROR_CODE_NOT_PERMISSION) {
                    viewModelScope.launch(Dispatchers.Main) {
                        PermissionUtil.requestPermission(context)
                        needRequestPermission.value = true
                    }
                }
            }
        }
    }
}