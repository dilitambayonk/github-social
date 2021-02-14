package com.sadili.androidapp.githubsocial.adapter

import android.content.Context
import androidx.annotation.Nullable
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.sadili.androidapp.githubsocial.fragments.FollowerFragment
import com.sadili.androidapp.githubsocial.fragments.FollowingFragment
import com.sadili.androidapp.githubsocial.R

class SectionsPagerAdapter(private val mContext: Context, fm: FragmentManager) : FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    var username: String? = null

    @StringRes
    private val TAB_TITLES = intArrayOf(
        R.string.tab_follower,
        R.string.tab_following
    )

    override fun getItem(position: Int): Fragment {
        var fragment: Fragment? = null
        when (position) {
            0 -> fragment =
                FollowerFragment.newInstance(username)
            1 -> fragment =
                FollowingFragment.newInstance(username)
        }
        return fragment as Fragment
    }

    override fun getCount(): Int {
        return 2
    }

    @Nullable
    override fun getPageTitle(position: Int): CharSequence? {
        return mContext.resources.getString(TAB_TITLES[position])
    }
}