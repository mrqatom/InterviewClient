package com.example.interviewclient.main.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.interviewclient.R
import com.example.interviewclient.app_info_detail.ui.AppInfoDetailActivity
import com.example.interviewclient.base.BaseFragment
import com.example.interviewclient.constant.IntentConstant
import com.example.interviewclient.main.adapter.AppInfoListAdapter
import com.example.interviewclient.main.view_model.MainViewModel
import com.example.interviewclient.util.ToastUtil
import kotlinx.android.synthetic.main.fragment_app_info_list.*

/**
 * 已安装应用列表页
 */
class AppInfoListFragment(override val mViewModel: MainViewModel) : BaseFragment<MainViewModel>() {
    companion object {
        private const val UNINSTALL_REQUEST_CODE = 0x1
    }

    private lateinit var appInfoListAdapter: AppInfoListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_app_info_list, container, false)
    }

    override fun initView() {
        app_list_swipe_refresh.setOnRefreshListener {
            mViewModel.getAppInfo(activity)
            app_list_swipe_refresh.isRefreshing = false
        }
        app_info_list_view.layoutManager = LinearLayoutManager(activity)
        appInfoListAdapter = AppInfoListAdapter(activity) {
            val intent = Intent(activity, AppInfoDetailActivity::class.java)
            intent.putExtra(IntentConstant.INTENT_KEY_APP_INFO, mViewModel.appInfo.value?.get(it))
            intent.putExtra(IntentConstant.INTENT_KEY_APP_INFO_POSITION, it)
            startActivityForResult(intent, UNINSTALL_REQUEST_CODE)
        }
        app_info_list_view.adapter = appInfoListAdapter
    }

    override fun initViewObservable() {
        mViewModel.appInfo.observe(this, Observer {
            if (it == null || it.isEmpty()){
                app_list_no_data.visibility = View.VISIBLE
            }else{
                app_list_no_data.visibility = View.GONE
                appInfoListAdapter.appInfo = it
                appInfoListAdapter.notifyDataSetChanged()
                mViewModel.toastMsg.value = getString(R.string.app_list_update)
            }
        })
    }

    override fun initData() {
        mViewModel.getAppInfo(activity)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            UNINSTALL_REQUEST_CODE ->
                if (resultCode == Activity.RESULT_OK) {
                    data?.run {
                        val position = getIntExtra(IntentConstant.INTENT_KEY_APP_INFO_POSITION, 0)
                        mViewModel.uninstallApp(position)
                        appInfoListAdapter.notifyItemRemoved(position)
                    }
                }
            else -> {
            }
        }
    }
}