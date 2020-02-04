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
            supportFragmentManager.commit {
                replace(
                        mainContainer.id,
                        FlowFragment.newInstance(),
                        FlowFragment.thisTag
                )
            }
        }
    }

    override fun onBackPressed() {
        if (supportFragmentManager.currentFragment?.onBackPressed() == true) return
        super.onBackPressed()
    }
}
