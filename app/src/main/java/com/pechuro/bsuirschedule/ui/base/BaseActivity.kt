package com.pechuro.bsuirschedule.ui.base

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import dagger.android.AndroidInjection
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

abstract class BaseActivity<T : ViewDataBinding, V : BaseViewModel> : AppCompatActivity(),
        HasSupportFragmentInjector {
    @Inject
    lateinit var fragmentDispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    abstract val viewModel: V

    @get:LayoutRes
    abstract val layoutId: Int
    lateinit var viewDataBinding: T
    open val bindingVariables: Map<Int, Any>? = null

    @Inject
    lateinit var compositeDisposable: CompositeDisposable

    override fun onCreate(savedInstanceState: Bundle?) {
        performDI()
        super.onCreate(savedInstanceState)
        performDataBinding()
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.dispose()
    }

    override fun supportFragmentInjector() = fragmentDispatchingAndroidInjector

    private fun performDI() = AndroidInjection.inject(this)

    private fun performDataBinding() {
        viewDataBinding = DataBindingUtil.setContentView(this, layoutId)
        bindingVariables?.forEach { (variable, obj) -> viewDataBinding.setVariable(variable, obj) }
        viewDataBinding.executePendingBindings()
    }
}