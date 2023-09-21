package com.antidote.mchanjo.onboarding

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

class ViewPagerAdapter(list: ArrayList<Fragment>,fm: FragmentManager, lifecycle: Lifecycle): FragmentStateAdapter(fm, lifecycle) {
//   Creating the Onboarding Screen Fragments
    private val fragmentList:ArrayList<Fragment> = list
    override fun getItemCount(): Int {
//        Here it will return the number of onboarding screen
        return fragmentList.size
    }

    override fun createFragment(position: Int): Fragment {
//        We are going to return the onboarding according to the position
        return fragmentList[position]
    }
}