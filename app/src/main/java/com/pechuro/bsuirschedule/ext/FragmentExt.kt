package com.pechuro.bsuirschedule.ext

import android.os.Parcelable
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commitNow
import com.pechuro.bsuirschedule.domain.common.Logger
import com.pechuro.bsuirschedule.feature.display.DisplayScheduleFragmentContainer
import java.io.Serializable

fun FragmentManager.removeAllFragmentsImmediate() = commitNow {
    popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
}

internal val FragmentManager.currentFragment: Fragment?
    get() = if (backStackEntryCount > 0) {
        val topFragmentTag = getBackStackEntryAt(backStackEntryCount - 1).name
        findFragmentByTag(topFragmentTag)
    } else null

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

inline fun <reified T : Parcelable> Fragment.parcelableOrException(key: String): T =
        requireArguments().parcelableOrException(key)

inline fun <reified T : Serializable> Fragment.serializableOrException(key: String): T =
        requireArguments().serializableOrException(key)

inline fun <reified T : Parcelable> Fragment.parcelableArrayOrException(key: String): Array<T> =
        requireArguments().parcelableArrayOrException(key)

val FragmentManager.displayScheduleFragment: DisplayScheduleFragmentContainer?
    get() = findFragmentBy(DisplayScheduleFragmentContainer.TAG, DisplayScheduleFragmentContainer::class.java)