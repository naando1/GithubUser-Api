package com.example.githubuser.ui

import android.content.Context
import android.os.Bundle
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.githubuser.R
import com.example.githubuser.ui.detailuser.Followers
import com.example.githubuser.ui.detailuser.Following

class SectionPagerAdapter(private val mCtx: Context, fm: FragmentManager, data: Bundle) :
    FragmentPagerAdapter(
        fm,
        BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
    ) {

    private var fragmentBundle: Bundle


    init {
        fragmentBundle = data

    }

    override fun getCount(): Int = 2

    @StringRes
    private val TAB_TITLES = intArrayOf(R.string.tab1, R.string.tab2)

    override fun getItem(position: Int): Fragment {
        var fragment: Fragment? = null
        when (position) {
            0 -> fragment = Followers()
            1 -> fragment = Following()
        }
        fragment?.arguments = this.fragmentBundle
        return fragment as Fragment
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return mCtx.resources.getString(TAB_TITLES[position])
    }
}