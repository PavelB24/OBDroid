package ru.barinov.obdroid.ui.diagnosticRoot

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

class ViewPagerAdapter(
    val fragments: List<Fragment>,
    manager: FragmentManager,
    lifeCycle: Lifecycle
): FragmentStateAdapter(manager, lifeCycle) {

    override fun getItemCount(): Int = fragments.size

    override fun createFragment(position: Int): Fragment = fragments[position]

}