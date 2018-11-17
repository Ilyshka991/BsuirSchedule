package com.pechuro.bsuirschedule.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject


abstract class BaseDialog<T : ViewDataBinding, V : BaseViewModel<out BaseNavigator>> : DialogFragment() {
    @Inject
    lateinit var mViewModelFactory: ViewModelProvider.Factory

    lateinit var mViewDataBinding: T
    abstract val mViewModel: V?
    abstract val bindingVariable: Int
    lateinit var mRootView: View
    @get:LayoutRes
    abstract val layoutId: Int

    override fun onCreate(savedInstanceState: Bundle?) {
        performDI()
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mViewDataBinding = DataBindingUtil
                .inflate(LayoutInflater.from(context), layoutId, null, false)
        mRootView = mViewDataBinding.root
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        return mRootView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mViewDataBinding.setVariable(bindingVariable, mViewModel)
        mViewDataBinding.executePendingBindings()
    }

    private fun performDI() = AndroidSupportInjection.inject(this)
}