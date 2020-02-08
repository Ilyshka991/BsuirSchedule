package com.pechuro.bsuirschedule.feature

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.pechuro.bsuirschedule.R
import com.pechuro.bsuirschedule.ext.commit
import com.pechuro.bsuirschedule.ext.currentFragment
import com.pechuro.bsuirschedule.ext.thisTag
import com.pechuro.bsuirschedule.feature.flow.FlowFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme_NoActionBar)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (savedInstanceState == null) {
            showNavigationFragment()
        }
    }

    override fun onBackPressed() {
        if (supportFragmentManager.currentFragment?.onBackPressed() == true) return
        super.onBackPressed()
    }

    private fun showNavigationFragment() {
        supportFragmentManager.commit {
            val fragment = FlowFragment()
            replace(activityHostFragment.id, fragment, fragment.thisTag)
        }
    }
}
