package com.pechuro.bsuirschedule.common.base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import com.pechuro.bsuirschedule.common.BaseEvent
import com.pechuro.bsuirschedule.common.EventBus
import com.pechuro.bsuirschedule.ext.app
import javax.inject.Inject
import kotlin.reflect.KClass

abstract class BaseFragment : Fragment() {

    @Inject
    protected lateinit var viewModelFactory: ViewModelProvider.Factory

    @get:LayoutRes
    protected abstract val layoutId: Int

    open fun onBackPressed(): Boolean = false

    override fun onAttach(context: Context) {
        context.app.appComponent.inject(this)
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? = inflater.inflate(layoutId, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        EventBus.send(OnViewCreated)
        super.onViewCreated(view, savedInstanceState)
    }

    protected fun <T : BaseViewModel> initViewModel(clazz: KClass<T>): T {
        return initViewModel(clazz, this)
    }

    protected fun <T : BaseViewModel> initViewModel(clazz: KClass<T>, owner: ViewModelStoreOwner): T {
        return ViewModelProvider(owner, viewModelFactory).get(clazz.java)
    }

    object OnViewCreated : BaseEvent()
}