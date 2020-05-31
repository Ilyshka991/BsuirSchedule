package com.pechuro.bsuirschedule.ext

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commitNow
import com.pechuro.bsuirschedule.domain.common.Logger
import com.pechuro.bsuirschedule.feature.display.DisplayScheduleFragmentContainer
import com.pechuro.bsuirschedule.feature.modifyitem.ModifyScheduleItemFragment

fun FragmentManager.removeAllFragmentsImmediate() = commitNow {
    popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
}

internal val FragmentManager.currentFragment: Fragment?
    get() = if (backStackEntryCount > 0) {
        val topFragmentTag = getBackStackEntryAt(backStackEntryCount - 1).name
        findFragmentByTag(topFragmentTag)
    } else primaryNavigationFragment

inline fun <reified T : Fragment> FragmentManager.fragmentByTag(tag: String) = findFragmentBy(tag, T::class.java)

fun <T : Fragment> FragmentManager.findFragmentBy(tag: String, clazz: Class<T>): T? {
    val fragment = findFragmentByTag(tag)
    return fragment?.let { clazz.cast(it) }
}

inline fun <reified T : Any> Fragment.getCallbackOrNull(): T? = when {
    parentFragment is T -> parentFragment as T
    context is T -> context as T
    context == null -> {
        Logger.wtf(this::class.java.simpleName, "Fragment not attached to host")
        null
    }
    else -> {
        Logger.wtf(this::class.java.simpleName, "Can not get ${T::class.java.simpleName} callback")
        null
    }
}

inline fun <reified T> Fragment.args(key: String): Lazy<T> = lazy(LazyThreadSafetyMode.NONE) {
    requireArguments().get(key) as T
}

val FragmentManager.displayScheduleFragment: DisplayScheduleFragmentContainer?
    get() = findFragmentBy(DisplayScheduleFragmentContainer.TAG, DisplayScheduleFragmentContainer::class.java)

val FragmentManager.modifyItemFragment: ModifyScheduleItemFragment?
    get() = findFragmentBy(ModifyScheduleItemFragment.TAG, ModifyScheduleItemFragment::class.java)