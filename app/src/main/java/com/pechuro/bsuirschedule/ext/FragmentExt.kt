package com.pechuro.bsuirschedule.ext

import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.bumptech.glide.manager.SupportRequestManagerFragment
import com.pechuro.bsuirschedule.common.base.BaseFragment

inline fun FragmentManager.commit(
        allowStateLoss: Boolean = false,
        body: FragmentTransaction.() -> Unit
) {
    val transaction = beginTransaction()
    transaction.body()
    if (allowStateLoss) {
        transaction.commitAllowingStateLoss()
    } else {
        transaction.commit()
    }
}

inline fun FragmentManager.commitNow(
        allowStateLoss: Boolean = false,
        body: FragmentTransaction.() -> Unit
) {
    val transaction = beginTransaction()
    transaction.body()
    if (allowStateLoss) {
        transaction.commitNowAllowingStateLoss()
    } else {
        transaction.commitNow()
    }
}

fun FragmentManager.popFragmentByTag(tag: String) =
        popBackStackImmediate(
                tag,
                FragmentManager.POP_BACK_STACK_INCLUSIVE
        )

val FragmentManager.currentFragment: BaseFragment?
    get() = run {
        val topFragmentTag = fragments.lastOrNull { fragment ->
            fragment !is SupportRequestManagerFragment
        }?.tag ?: ""
        return findFragmentByTag(topFragmentTag) as? BaseFragment
    }