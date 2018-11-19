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
    lateinit var viewModelFactory: ViewModelProvider.Factory
    abstract val viewModel: V

    lateinit var viewDataBinding: T
    abstract val bindingVariables: Map<Int, Any>?

    @get:LayoutRes
    abstract val layoutId: Int

    override fun onCreate(savedInstanceState: Bundle?) {
        performDI()
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewDataBinding = DataBindingUtil.inflate(inflater, layoutId, container, false)
        return viewDataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindingVariables?.forEach { (variable, obj) -> viewDataBinding.setVariable(variable, obj) }
        viewDataBinding.executePendingBindings()
    }

    private fun performDI() = AndroidSupportInjection.inject(this)
}