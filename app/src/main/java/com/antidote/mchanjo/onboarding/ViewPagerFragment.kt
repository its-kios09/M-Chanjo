package com.antidote.mchanjo.onboarding

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.widget.ViewPager2
import com.antidote.mchanjo.R
import com.antidote.mchanjo.onboarding.screens.FirstScreen
import com.antidote.mchanjo.onboarding.screens.SecondScreen
import com.antidote.mchanjo.onboarding.screens.ThirdScreen

class ViewPagerFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
//        This will return the view of the fragments
        val view = inflater.inflate(R.layout.fragment_view_pager, container, false)

        val fragmentList = arrayListOf<Fragment>(
            FirstScreen(),
            SecondScreen(),
            ThirdScreen()
        )
        val adapter = ViewPagerAdapter(fragmentList,requireActivity().supportFragmentManager, lifecycle)
        view.findViewById<ViewPager2>(R.id.viewPager).adapter = adapter
        return view
    }

}