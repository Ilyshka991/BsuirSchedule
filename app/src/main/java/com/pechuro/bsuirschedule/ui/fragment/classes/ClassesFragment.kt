package com.pechuro.bsuirschedule.ui.fragment.classes

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.tabs.TabLayout
import com.pechuro.bsuirschedule.BR
import com.pechuro.bsuirschedule.R
import com.pechuro.bsuirschedule.databinding.FragmentViewpagerBinding
import com.pechuro.bsuirschedule.ui.base.BaseFragment
import com.pechuro.bsuirschedule.ui.fragment.transactioninfo.BaseInformation
import com.pechuro.bsuirschedule.ui.fragment.transactioninfo.impl.ClassesDayInformation
import com.pechuro.bsuirschedule.ui.fragment.transactioninfo.impl.ScheduleInformation
import javax.inject.Inject


class ClassesFragment : BaseFragment<FragmentViewpagerBinding, ClassesFragmentViewModel>() {
    companion object {
        const val NUMBER_OF_TABS = 40
        const val ARG_INFO = "arg_information"

        fun newInstance(info: ScheduleInformation): ClassesFragment {
            val args = Bundle()
            args.putParcelable(ARG_INFO, info)

            val fragment = ClassesFragment()
            fragment.arguments = args
            return fragment
        }
    }

    @Inject
    lateinit var mPagerAdapter: ClassesPagerAdapter

    private val scheduleName by lazy {
        (arguments?.getParcelable(ARG_INFO) as? BaseInformation)?.name ?: ""
    }
    private val scheduleType by lazy {
        (arguments?.getParcelable(ARG_INFO) as? BaseInformation)?.type ?: -1
    }

    override val mViewModel: ClassesFragmentViewModel
        get() = ViewModelProviders.of(this, mViewModelFactory).get(ClassesFragmentViewModel::class.java)
    override val bindingVariable: Int
        get() = BR._all
    override val layoutId: Int
        get() = R.layout.fragment_viewpager

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
        inflateLayout()
    }

    private fun inflateLayout() {
        val info = mutableListOf<ClassesDayInformation>()

        for (i in 0 until NUMBER_OF_TABS) {
            val (day, week, dayRu) = mViewModel.getTabDate(i)
            info.add(ClassesDayInformation(scheduleName, scheduleType, dayRu, week, 0))
            mViewDataBinding.tabLayout.addTab(mViewDataBinding.tabLayout.newTab()
                    .setText(getString(R.string.schedule_tab_text, day, week)))
        }
        mPagerAdapter.fragmentsInfo = info
    }


    private fun setupView() {
        mViewDataBinding.viewPager.adapter = mPagerAdapter

        mViewDataBinding.viewPager
                .addOnPageChangeListener(
                        TabLayout.TabLayoutOnPageChangeListener(
                                mViewDataBinding.tabLayout))

        mViewDataBinding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab) {

            }

            override fun onTabSelected(tab: TabLayout.Tab) {
                mViewDataBinding.viewPager.currentItem = tab.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {

            }
        })
    }
}