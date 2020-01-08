package com.pechuro.bsuirschedule.feature.main.addschedule

import android.os.Bundle
import android.view.ViewGroup
import com.google.android.material.tabs.TabLayout
import com.pechuro.bsuirschedule.R
import com.pechuro.bsuirschedule.common.base.BaseDialog
import com.pechuro.bsuirschedule.widget.listeners.TabLayoutListener
import kotlinx.android.synthetic.main.fragment_viewpager.*
import javax.inject.Inject

class AddScheduleDialog : BaseDialog() {

    companion object {

        const val TAG = "AddScheduleDialog"

        fun newInstance() = AddScheduleDialog()
    }

    override val layoutId: Int = R.layout.fragment_viewpager

    @Inject
    protected lateinit var pagerAdapter: AddScheduleDialogPagerAdapter

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initView()
    }

    private fun initView() {
        viewPager.apply {
            adapter = pagerAdapter
            layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
            addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayout))
        }
        tabLayout.apply {
            AddScheduleDialogPagerAdapter.FragmentType.values().forEach {
                val tab = newTab().setText(getString(it.titleRes))
                addTab(tab)
            }
            addOnTabSelectedListener(object : TabLayoutListener {
                override fun onTabSelected(tab: TabLayout.Tab) {
                    viewPager.currentItem = tab.position
                }
            })
        }
    }

}

