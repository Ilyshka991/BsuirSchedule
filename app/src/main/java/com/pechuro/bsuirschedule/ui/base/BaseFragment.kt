package com.pechuro.bsuirschedule.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

abstract class BaseFragment<T : ViewDataBinding, V : BaseViewModel> : Fragment() {
    @Inject
    lateinit var mViewModelFactory: ViewModelProvider.Factory
    abstract val mViewModel: V

    lateinit var mViewDataBinding: T
    abstract val bindingVariable: Int

    @get:LayoutRes
    abstract val layoutId: Int

    override fun onCreate(savedInstanceState: Bundle?) {
        performDI()
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mViewDataBinding = DataBindingUtil.inflate(inflater, layoutId, container, false)
        return mViewDataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mViewDataBinding.setVariable(bindingVariable, mViewModel)
        mViewDataBinding.executePendingBindings()
    }

    private fun performDI() = AndroidSupportInjection.inject(this)
}