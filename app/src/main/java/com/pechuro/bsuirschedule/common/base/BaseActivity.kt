package com.pechuro.bsuirschedule.common.base

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import dagger.android.AndroidInjection
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import javax.inject.Inject
import kotlin.reflect.KClass

abstract class BaseActivity : AppCompatActivity(), HasSupportFragmentInjector {

    @Inject
    protected lateinit var fragmentDispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>

    @Inject
    protected lateinit var viewModelFactory: ViewModelProvider.Factory

    @get:LayoutRes
    protected abstract val layoutId: Int

    override fun onCreate(savedInstanceState: Bundle?) {
        performDI()
        super.onCreate(savedInstanceState)
        setContentView(layoutId)
    }

    override fun supportFragmentInjector() = fragmentDispatchingAndroidInjector

    protected fun <T : BaseViewModel> initViewModel(clazz: KClass<T>) =
            ViewModelProviders.of(this, viewModelFactory).get(clazz.java)

    private fun performDI() = AndroidInjection.inject(this)
}