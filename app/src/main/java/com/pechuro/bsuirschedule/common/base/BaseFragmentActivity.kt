package com.pechuro.bsuirschedule.common.base

import android.os.Bundle
import androidx.annotation.IdRes
import androidx.lifecycle.Lifecycle
import com.pechuro.bsuirschedule.ext.transaction

abstract class BaseFragmentActivity : BaseActivity() {

    @get:IdRes
    protected abstract val containerId: Int

    private var pendingFragmentTransaction: PendingFragmentTransaction? = null

    protected abstract fun getHomeFragment(): BaseFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) showHomeFragment()
    }

    override fun onPostResume() {
        super.onPostResume()
        pendingFragmentTransaction?.let {
            showFragment(
                    fragment = it.fragment,
                    isAddToBackStack = it.isAddToBackStack,
                    tag = it.tag
            )
            pendingFragmentTransaction = null
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    protected open fun showFragment(
            fragment: BaseFragment,
            isAddToBackStack: Boolean = true,
            tag: String? = null
    ) {
        if (lifecycle.currentState.isAtLeast(Lifecycle.State.RESUMED)) {
            supportFragmentManager.transaction {
                replace(containerId, fragment, tag)
                if (isAddToBackStack) addToBackStack(tag)
            }
        } else {
            pendingFragmentTransaction = PendingFragmentTransaction(
                    fragment = fragment,
                    isAddToBackStack = isAddToBackStack,
                    tag = tag
            )
        }
    }

    protected open fun showDialog(
            fragment: BaseDialog,
            tag: String? = null
    ) {
        if (lifecycle.currentState.isAtLeast(Lifecycle.State.RESUMED)) {
            fragment.show(supportFragmentManager, tag)
        }
    }

    protected fun showPreviousFragment() {
        if (lifecycle.currentState.isAtLeast(Lifecycle.State.RESUMED)) {
            supportFragmentManager.popBackStack()
        }
    }

    private fun showHomeFragment() {
        val fragment = getHomeFragment()
        supportFragmentManager.transaction {
            replace(containerId, fragment)
        }
    }
}

private class PendingFragmentTransaction(
        val fragment: BaseFragment,
        val isAddToBackStack: Boolean,
        val tag: String?
)