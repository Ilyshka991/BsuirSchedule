package com.pechuro.bsuirschedule.common.base

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import com.pechuro.bsuirschedule.common.AppThemeManager
import com.pechuro.bsuirschedule.ext.app
import javax.inject.Inject
import kotlin.reflect.KClass

abstract class BaseActivity : AppCompatActivity() {

    @Inject
    protected lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    protected lateinit var appThemeManager: AppThemeManager

    @get:LayoutRes
    protected abstract val layoutId: Int

    override fun onCreate(savedInstanceState: Bundle?) {
        app.appComponent.inject(this)
        appThemeManager.applyToCurrentTheme(theme)
        super.onCreate(savedInstanceState)
        setContentView(layoutId)
    }

    protected fun <T : BaseViewModel> initViewModel(clazz: KClass<T>): T {
        return initViewModel(clazz, this)
    }

    protected fun <T : BaseViewModel> initViewModel(
        clazz: KClass<T>,
        owner: ViewModelStoreOwner
    ): T {
        return ViewModelProvider(owner, viewModelFactory).get(clazz.java)
    }
}