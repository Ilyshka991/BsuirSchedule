package com.pechuro.bsuirschedule.ui.fragment.adddialog

import android.os.Bundle
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.tabs.TabLayout
import com.pechuro.bsuirschedule.R
import com.pechuro.bsuirschedule.databinding.FragmentViewpagerBinding
import com.pechuro.bsuirschedule.ui.base.BaseDialog
import com.pechuro.bsuirschedule.ui.utils.EventBus
import javax.inject.Inject

class AddDialog : BaseDialog<FragmentViewpagerBinding, AddDialogViewModel>() {

    @Inject
    lateinit var pagerAdapter: AddDialogPagerAdapter

    override val viewModel: AddDialogViewModel
        get() = ViewModelProviders.of(this, mViewModelFactory).get(AddDialogViewModel::class.java)
    override val bindingVariables: Map<Int, Any>?
        get() = null
    override val layoutId: Int
        get() = R.layout.fragment_viewpager

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupView()
        setListeners()
        setEventListeners()
    }

    private fun setupView() {
        viewDataBinding.viewPager.adapter = pagerAdapter
        viewDataBinding.viewPager.layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT

        viewDataBinding.tabLayout.addTab(viewDataBinding.tabLayout.newTab().setText(getString(R.string.students)))
        viewDataBinding.tabLayout.addTab(viewDataBinding.tabLayout.newTab().setText(getString(R.string.employees)))
    }

    private fun setEventListeners() {
        compositeDisposable.addAll(
                EventBus.listen(SetDialogCancelable::class.java).subscribe {
                    isCancelable = it.isCancelable
                },
                EventBus.listen(OnAddDialogDismissEvent::class.java).subscribe {
                    dismiss()
                })
    }

    private fun setListeners() {
        viewDataBinding.viewPager
                .addOnPageChangeListener(
                        TabLayout.TabLayoutOnPageChangeListener(
                                viewDataBinding.tabLayout))

        viewDataBinding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab) {

            }

            override fun onTabSelected(tab: TabLayout.Tab) {
                viewDataBinding.viewPager.currentItem = tab.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {

            }
        })
    }

    companion object {
        fun newInstance(): AddDialog {
            val fragment = AddDialog()
            val bundle = Bundle()
            fragment.arguments = bundle
            return fragment
        }
    }
}

