package com.example.interviewclient.base

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData

/**
 * @author qiuyunfei
 * @description
 */
abstract class BaseViewModel(application: Application) : AndroidViewModel(application) {
    var toastMsg: MutableLiveData<String> = MutableLiveData()
}