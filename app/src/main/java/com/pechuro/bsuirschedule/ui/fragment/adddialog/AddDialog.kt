package com.pechuro.bsuirschedule.ui.fragment.adddialog

import android.content.Context
import android.os.Bundle
import android.view.ViewGroup
import androidx.databinding.library.baseAdapters.BR
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.tabs.TabLayout
import com.pechuro.bsuirschedule.R
import com.pechuro.bsuirschedule.databinding.FragmentViewpagerBinding
import com.pechuro.bsuirschedule.ui.base.BaseDialog
import com.pechuro.bsuirschedule.ui.fragment.adddialog.addfragment.AddFragment
import javax.inject.Inject


class AddDialog : BaseDialog<FragmentViewpagerBinding, AddDialogViewModel>(), AddFragment.AddFragmentCallback {
    companion object {
        fun newInstance(): AddDialog {
            val fragment = AddDialog()
            val bundle = Bundle()
            fragment.arguments = bundle
            return fragment
        }
    }

    @Inject
    lateinit var mPagerAdapter: AddDialogPagerAdapter

    private var mNavigator: AddDialogCallback? = null

    override val mViewModel: AddDialogViewModel
        get() = ViewModelProviders.of(this, mViewModelFactory).get(AddDialogViewModel::class.java)
    override val bindingVariable: Int
        get() = BR._all
    override val layoutId: Int
        get() = R.layout.fragment_viewpager

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        mNavigator = context as? AddDialogCallback
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupView()
        setListeners()
    }

    override fun dismiss() {
        super.dismiss()
        mNavigator?.onAddDialogDismiss()
    }

    override fun setDialogCancelable(isCancelable: Boolean) {
        setCancelable(isCancelable)
    }

    override fun onDismiss() = dismiss()

    private fun setupView() {
        mViewDataBinding.viewPager.adapter = mPagerAdapter
        mViewDataBinding.viewPager.layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT

        mViewDataBinding.tabLayout.addTab(mViewDataBinding.tabLayout.newTab().setText(getString(R.string.students)))
        mViewDataBinding.tabLayout.addTab(mViewDataBinding.tabLayout.newTab().setText(getString(R.string.employees)))
    }

    private fun setListeners() {
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

    interface AddDialogCallback {
        fun onAddDialogDismiss()
    }
}

