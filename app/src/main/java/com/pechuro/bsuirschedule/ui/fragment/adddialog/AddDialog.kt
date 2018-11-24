package com.pechuro.bsuirschedule.ui.fragment.adddialog

import android.content.Context
import android.os.Bundle
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
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
        get() = ViewModelProviders.of(this, viewModelFactory).get(AddDialogViewModel::class.java)
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

        viewDataBinding.tabLayout.addTab(viewDataBinding.tabLayout.newTab().setText(getString(R.string.add_dialog_tab_item_students)))
        viewDataBinding.tabLayout.addTab(viewDataBinding.tabLayout.newTab().setText(getString(R.string.add_dialog_tab_item_employees)))
    }

    private fun setEventListeners() {
        compositeDisposable.addAll(
                EventBus.listen(AddDialogEvent::class.java).subscribe {
                    when (it) {
                        is AddDialogEvent.OnSuccess -> {
                            dismiss()
                            EventBus.publish(AddDialogEvent.OnScheduleAdded)
                        }
                    }
                },
                EventBus.listenWithReplay(AddDialogEvent.OnLoading::class.java).subscribe {
                    isCancelable = it.isEnabled
                    viewDataBinding.viewPager.isSwipeEnabled = it.isEnabled
                    viewDataBinding.tabLayout.setClickEnabled(it.isEnabled)
                    hideKeyboard()
                })
    }

    private fun hideKeyboard() {
        val view = dialog.currentFocus
        if (view != null) {
            val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            imm?.hideSoftInputFromWindow(view.windowToken, 0)
        }
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
        const val TAG = "add_dialog"

        fun newInstance(): AddDialog {
            val fragment = AddDialog()
            val bundle = Bundle()
            fragment.arguments = bundle
            return fragment
        }
    }
}

