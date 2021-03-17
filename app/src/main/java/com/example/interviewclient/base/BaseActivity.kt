package com.example.interviewclient.base

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewStub
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.interviewclient.R
import com.example.interviewclient.util.ToastUtil
import kotlinx.android.synthetic.main.activity_root.*

/**
 * @author qiuyunfei
 * @description
 */
abstract class BaseActivity<VM : BaseViewModel> : AppCompatActivity() {
    private var TAG = "BaseActivity"
    protected abstract val mViewModel: VM
    protected abstract fun initView()
    abstract fun onBindLayout(): Int

    private var mProgressBar: ProgressBar? = null

    inline fun <reified T : VM> initViewModel(): T {
        return ViewModelProvider(this).get(T::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_root)
        TAG = this::class.java.simpleName
        initLiveData()
        initData()
        initCommonView()
    }

    protected fun initCommonView() {
        view_stub_content_layout.layoutResource = onBindLayout()
        view_stub_content_layout.inflate()

        if (enableLoadingView()){
            mProgressBar = view_stub_init_loading.inflate().findViewById(R.id.view_init_loading)
        }

        initView()
    }

    private fun showLoadingBar(show: Boolean) {
        if (mProgressBar == null) {
            Log.e(TAG, "如果需要使用LoadingView,请先重写enableLoadingView" )
        }
        mProgressBar?.visibility = if (show) View.VISIBLE else View.GONE
    }

    private fun initLiveData() {
        mViewModel.toastMsg.observe(this, Observer {
            ToastUtil.showShort(this, it)
        })
        mViewModel.loadingViewVisible.observe(this, Observer {
            showLoadingBar(it)
        })
        initViewObservable()
    }

    protected open fun initData() {}

    /**
     * 放置 观察者对象
     */
    protected open fun initViewObservable() {}

    protected open fun enableLoadingView(): Boolean = false
}