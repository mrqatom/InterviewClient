package com.example.interviewclient.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
import com.example.interviewclient.R
import com.example.interviewclient.app_info_list.ui.AppInfoListFragment
import com.example.interviewclient.app_recommend.ui.AppRecommendFragment
import com.example.interviewclient.main.adapter.MViewPagerAdapter
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private val fragments = ArrayList<Fragment>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initView()
    }

    private fun initView() {
        fragments.add(AppInfoListFragment())
        fragments.add(AppRecommendFragment())

        viewpager.adapter =
            MViewPagerAdapter(
                supportFragmentManager,
                BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT,
                fragments
            )

        tablayout.addTab(tablayout.newTab().setText(R.string.app_list),false)
        tablayout.addTab(tablayout.newTab().setText(R.string.recommend_list),false)
        tablayout.setupWithViewPager(viewpager)
    }


}