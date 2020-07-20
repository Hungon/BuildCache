package com.trials.sample001.ui.main

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class SectionsPagerAdapter(context: Context, fm: FragmentManager) :
    FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    private val listOfTitles = arrayListOf<String>()

    override fun getItem(position: Int): Fragment {
        return SectionsFragment.newInstance(listOfTitles[position])
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return listOfTitles.takeIf { it.isNotEmpty() }?.get(position)
    }

    override fun getCount(): Int {
        return listOfTitles.size
    }

    fun setTitles(titles: List<String>) {
        listOfTitles.clear()
        listOfTitles.addAll(titles)
        notifyDataSetChanged()
    }
}