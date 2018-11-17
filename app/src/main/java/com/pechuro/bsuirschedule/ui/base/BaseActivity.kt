package com.pechuro.bsuirschedule.ui.base

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProvider
import dagger.android.AndroidInjection
import javax.inject.Inject

abstract class BaseActivity<T : ViewDataBinding, V : BaseViewModel<out BaseNavigator>> : AppCompatActivity() {
    @Inject
    lateinit var mViewModelFactory: ViewModelProvider.Factory

    lateinit var mViewDataBinding: T
    abstract val mViewModel: V

    @get:LayoutRes
    abstract val layoutId: Int
    abstract val bindingVariables: Map<Int, Any>

    override fun onCreate(savedInstanceState: Bundle?) {
        performDI()
        super.onCreate(savedInstanceState)
        performDataBinding()

    }

    private fun performDI() = AndroidInjection.inject(this)

    private fun performDataBinding() {
        mViewDataBinding = DataBindingUtil.setContentView(this, layoutId)
        bindingVariables.forEach { (variable, obj) -> mViewDataBinding.setVariable(variable, obj) }
        mViewDataBinding.executePendingBindings()
    }
}