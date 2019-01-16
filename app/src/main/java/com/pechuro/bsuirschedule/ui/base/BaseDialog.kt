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
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.AndroidSupportInjection
import dagger.android.support.HasSupportFragmentInjector
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject


abstract class BaseDialog<T : ViewDataBinding, V : BaseViewModel> : DialogFragment(),
        HasSupportFragmentInjector {
    @Inject
    lateinit var fragmentDispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    abstract val viewModel: V?

    @get:LayoutRes
    abstract val layoutId: Int
    lateinit var rootView: View

    lateinit var viewDataBinding: T
    open val bindingVariables: Map<Int, Any?>? = null

    @Inject
    lateinit var compositeDisposable: CompositeDisposable

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

    override fun supportFragmentInjector() = fragmentDispatchingAndroidInjector

    private fun performDI() = AndroidSupportInjection.inject(this)
}