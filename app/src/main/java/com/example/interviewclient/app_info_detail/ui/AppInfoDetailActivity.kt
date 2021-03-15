package com.example.interviewclient.app_info_detail.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.view.View
import androidx.lifecycle.Observer
import com.example.interviewclient.R
import com.example.interviewclient.app_info_detail.view_model.AppInfoDetailViewModel
import com.example.interviewclient.base.BaseActivity
import com.example.interviewclient.constant.IntentConstant.INTENT_KEY_APP_INFO
import com.example.interviewclient.util.TimeUtil
import kotlinx.android.synthetic.main.activity_app_info_detail.*


class AppInfoDetailActivity : BaseActivity<AppInfoDetailViewModel>(), View.OnClickListener {
    override val layoutId = R.layout.activity_app_info_detail
    override val mViewModel by lazy { initViewModel<AppInfoDetailViewModel>() }

    override fun initView() {
        app_detail_back.setOnClickListener(this)
        app_detail_uninstall.setOnClickListener(this)
    }

    override fun initData() {
        mViewModel.appInfo.value = intent.getParcelableExtra(INTENT_KEY_APP_INFO)
    }

    @SuppressLint("SetTextI18n")
    override fun initViewObservable() {
        mViewModel.appInfo.observe(this, Observer {
            app_detail_icon.setImageBitmap(it.icon)
            app_detail_name.text = it.appName
            app_detail_version.text = it.version ?: getString(R.string.cant_get)
            app_detail_package.text = it.packageName
            app_detail_install.text = TimeUtil.timeStamp2Date(it.installTime)
            app_detail_update.text = TimeUtil.timeStamp2Date(it.lastUpdateTime)
            mViewModel.getAppSize(this)
        })

        mViewModel.appSize.observe(this, Observer {
            val size:Double = it.toDouble() / 1000000
            app_detail_size.text = "$size M"
        })
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.app_detail_back -> finish()
            R.id.app_detail_uninstall -> uninstallApp()
        }
    }

    /**
     * 卸载app
     */
    private fun uninstallApp() {
        val uri: Uri = Uri.fromParts("package", mViewModel.appInfo.value?.packageName, null)
        val intent = Intent(Intent.ACTION_DELETE, uri)
        startActivity(intent)
    }
}