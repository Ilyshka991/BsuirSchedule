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
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject


abstract class BaseDialog<T : ViewDataBinding, V : BaseViewModel> : DialogFragment() {
    @Inject
    lateinit var mViewModelFactory: ViewModelProvider.Factory

    lateinit var viewDataBinding: T
    abstract val viewModel: V?
    abstract val bindingVariables: Map<Int, Any?>?
    lateinit var rootView: View
    @get:LayoutRes
    abstract val layoutId: Int

    val compositeDisposable: CompositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        performDI()
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewDataBinding = DataBindingUtil
                .inflate(LayoutInflater.from(context), layoutId, null, false)
        rootView = viewDataBinding.root
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        return rootView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        bindingVariables?.forEach { (variable, obj) -> viewDataBinding.setVariable(variable, obj) }
        viewDataBinding.executePendingBindings()
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.dispose()
    }

    private fun performDI() = AndroidSupportInjection.inject(this)
}