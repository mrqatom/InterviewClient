package com.example.interviewclient.main.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class MViewPagerAdapter(
    fm: FragmentManager?,
    behavior: Int,
    private val fragments: List<Fragment>
) :
    FragmentPagerAdapter(fm!!, behavior) {
    override fun getItem(position: Int): Fragment {
        return fragments[position]
    }

    override fun getCount(): Int {
        return fragments.size
    }

}