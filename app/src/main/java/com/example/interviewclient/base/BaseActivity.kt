package com.example.interviewclient.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.interviewclient.util.ToastUtil

/**
 * @author qiuyunfei
 * @description
 */
abstract class BaseActivity<VM : BaseViewModel> : AppCompatActivity() {

    protected abstract val layoutId: Int
    protected abstract val mViewModel: VM
    protected abstract fun initView()

    inline fun <reified T : VM> initViewModel(): T {
        return ViewModelProvider(this).get(T::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layoutId)

        initLiveData()
        initData()
        initView()
    }

    private fun initLiveData() {
        mViewModel.toastMsg.observe(this, Observer {
            ToastUtil.showShort(this, it)
        })
        initViewObservable()
    }

    protected open fun initData() {}
    /**
     * 放置 观察者对象
     */
    protected open fun initViewObservable(){}

}