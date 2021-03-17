package com.example.interviewclient.main.ui

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
import com.example.interviewclient.R
import com.example.interviewclient.base.BaseActivity
import com.example.interviewclient.main.adapter.MViewPagerAdapter
import com.example.interviewclient.main.view_model.MainViewModel
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : BaseActivity<MainViewModel>() {

    override val mViewModel by lazy { initViewModel<MainViewModel>() }
    private val fragments = ArrayList<Fragment>()

    override fun onBindLayout(): Int {
        return R.layout.activity_main
    }

    override fun initView() {
        fragments.add(AppInfoListFragment(mViewModel))
        fragments.add(AppRecommendFragment(mViewModel))

        viewpager.adapter =
            MViewPagerAdapter(
                supportFragmentManager,
                BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT,
                fragments
            )

        tablayout.setupWithViewPager(viewpager)
        tablayout.getTabAt(0)?.setText(R.string.app_list)
        tablayout.getTabAt(1)?.setText(R.string.recommend_list)
    }

    override fun enableLoadingView(): Boolean {
        return true
    }
}