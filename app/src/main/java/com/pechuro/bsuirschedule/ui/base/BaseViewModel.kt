package com.pechuro.bsuirschedule.ui.base

import androidx.lifecycle.ViewModel
import com.pechuro.bsuirschedule.ui.activity.infoload.InfoLoadNavigator
import io.reactivex.disposables.CompositeDisposable
import java.lang.ref.WeakReference

abstract class BaseViewModel<N : BaseNavigator> : ViewModel() {

    val compositeDisposable: CompositeDisposable = CompositeDisposable()
    lateinit var navigator: WeakReference<N?>

    fun setNavigator(navigator: N?) {
        this.navigator = WeakReference(navigator)
    }

    fun getNavigator() = navigator.get()

    override fun onCleared() {
        compositeDisposable.dispose()
        super.onCleared()
    }
}
