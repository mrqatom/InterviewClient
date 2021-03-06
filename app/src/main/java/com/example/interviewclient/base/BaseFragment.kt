package com.example.interviewclient.base

import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.example.interviewclient.R

/**
 * @author qiuyunfei
 * @date 2021/3/15 0015
 * @description
 */
abstract class BaseFragment<VM : BaseViewModel> : Fragment() {
    private val TAG = "BaseFragment"
    protected abstract val mViewModel: VM
    protected abstract fun initView()
    protected abstract fun initData()
    abstract fun onBindLayout(): Int
    protected var isFirstEnter = true

    /**
     * 放置 观察者对象
     */
    abstract fun initViewObservable()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(onBindLayout(), container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewObservable()
        initView()
    }

    override fun onResume() {
        super.onResume()
        if (isFirstEnter) {
            initData()
            isFirstEnter = false
        }
    }

    fun isGranted(permission: String): Boolean {
        val fragmentActivity = activity
            ?: throw IllegalStateException("This fragment must be attached to an activity.")
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            fragmentActivity.checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED
        } else {
            true
        }
    }

}