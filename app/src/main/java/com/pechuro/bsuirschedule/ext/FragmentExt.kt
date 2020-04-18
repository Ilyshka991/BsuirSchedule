package com.pechuro.bsuirschedule.ext

import android.os.Parcelable
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.bumptech.glide.manager.SupportRequestManagerFragment
import java.io.Serializable

fun FragmentManager.popFragmentByTag(tag: String) =
        popBackStackImmediate(
                tag,
                FragmentManager.POP_BACK_STACK_INCLUSIVE
        )

val FragmentManager.currentFragment: Fragment?
    get() = run {
        val topFragmentTag = fragments.lastOrNull { fragment ->
            fragment !is SupportRequestManagerFragment
        }?.tag ?: ""
        return findFragmentByTag(topFragmentTag)
    }

inline fun <reified T : Parcelable> Fragment.parcelableOrException(key: String): T =
        requireArguments().parcelableOrException(key)

inline fun <reified T : Serializable> Fragment.serializableOrException(key: String): T =
        requireArguments().serializableOrException(key)

inline fun <reified T : Parcelable> Fragment.parcelableArrayOrException(key: String): Array<T> =
        requireArguments().parcelableArrayOrException(key)