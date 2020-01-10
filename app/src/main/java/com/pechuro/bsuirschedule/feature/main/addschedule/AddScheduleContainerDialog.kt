package com.pechuro.bsuirschedule.feature.main.addschedule

import android.app.Dialog
import android.os.Bundle
import android.view.ViewGroup
import android.view.WindowManager
import com.google.android.material.tabs.TabLayout
import com.pechuro.bsuirschedule.R
import com.pechuro.bsuirschedule.common.base.BaseDialog
import com.pechuro.bsuirschedule.ext.addOnTabSelectedListener
import com.pechuro.bsuirschedule.ext.observeNonNull
import com.pechuro.bsuirschedule.feature.main.addschedule.AddScheduleViewModel.State
import kotlinx.android.synthetic.main.fragment_viewpager.*
import javax.inject.Inject

class AddScheduleContainerDialog : BaseDialog() {

    companion object {

        const val TAG = "AddScheduleContainerDialog"

        fun newInstance() = AddScheduleContainerDialog()
    }

    override val layoutId: Int = R.layout.fragment_viewpager

    @Inject
    protected lateinit var pagerAdapter: AddScheduleContainerDialogPagerAdapter

    private val viewModel by lazy(LazyThreadSafetyMode.NONE) {
        initViewModel(AddScheduleViewModel::class, owner = this)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState).apply {
            window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initView()
        observeData()
    }

    private fun initView() {
        viewPager.apply {
            adapter = pagerAdapter
            layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
            addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayout))
        }
        tabLayout.apply {
            AddScheduleContainerDialogPagerAdapter.FragmentType.values().forEach {
                val tab = newTab().setText(getString(it.titleRes))
                addTab(tab)
            }
            addOnTabSelectedListener(onTabSelected = {
                viewPager.currentItem = it.position
            })
        }
    }

    private fun observeData() {
        viewModel.state.observeNonNull(viewLifecycleOwner) { state ->
            when (state) {
                is State.Complete -> onComplete()
                is State.Loading -> setActionsEnabled(false)
                is State.Idle -> setActionsEnabled(true)
                is State.Error -> setActionsEnabled(false)
            }
        }
    }

    private fun onComplete() {
        dismiss()
    }

    private fun setActionsEnabled(enabled: Boolean) {
        isCancelable = enabled
        viewPager.isSwipeEnabled = enabled
        tabLayout.setClickEnabled(enabled)
    }
}

