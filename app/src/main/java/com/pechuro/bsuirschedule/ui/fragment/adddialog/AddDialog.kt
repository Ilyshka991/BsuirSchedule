package com.pechuro.bsuirschedule.ui.fragment.adddialog

import android.content.Context
import android.os.Bundle
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.tabs.TabLayout
import com.pechuro.bsuirschedule.R
import com.pechuro.bsuirschedule.databinding.FragmentViewpagerBinding
import com.pechuro.bsuirschedule.ui.base.BaseDialog
import com.pechuro.bsuirschedule.ui.fragment.adddialog.addfragment.AddFragment
import javax.inject.Inject


class AddDialog : BaseDialog<FragmentViewpagerBinding, AddDialogViewModel>(), AddFragment.AddFragmentCallback {

    @Inject
    lateinit var pagerAdapter: AddDialogPagerAdapter

    private var _navigator: AddDialogCallback? = null

    override val viewModel: AddDialogViewModel
        get() = ViewModelProviders.of(this, mViewModelFactory).get(AddDialogViewModel::class.java)
    override val bindingVariables: Map<Int, Any>?
        get() = null
    override val layoutId: Int
        get() = R.layout.fragment_viewpager

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        _navigator = context as? AddDialogCallback
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupView()
        setListeners()
    }

    override fun onDetach() {
        super.onDetach()
        _navigator = null
    }

    override fun dismiss() {
        super.dismiss()
        _navigator?.onAddDialogDismiss()
    }

    override fun setDialogCancelable(isCancelable: Boolean) {
        setCancelable(isCancelable)
    }

    override fun onDismiss() = dismiss()

    private fun setupView() {
        viewDataBinding.viewPager.adapter = pagerAdapter
        viewDataBinding.viewPager.layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT

        viewDataBinding.tabLayout.addTab(viewDataBinding.tabLayout.newTab().setText(getString(R.string.students)))
        viewDataBinding.tabLayout.addTab(viewDataBinding.tabLayout.newTab().setText(getString(R.string.employees)))
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

    interface AddDialogCallback {
        fun onAddDialogDismiss()
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

