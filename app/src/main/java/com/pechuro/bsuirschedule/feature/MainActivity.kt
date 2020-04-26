package com.pechuro.bsuirschedule.feature

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.annotation.StyleRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import com.pechuro.bsuirschedule.R
import com.pechuro.bsuirschedule.common.BackPressedHandler
import com.pechuro.bsuirschedule.domain.common.BaseInteractor
import com.pechuro.bsuirschedule.domain.common.getOrDefault
import com.pechuro.bsuirschedule.domain.entity.AppTheme
import com.pechuro.bsuirschedule.domain.interactor.GetAppTheme
import com.pechuro.bsuirschedule.ext.app
import com.pechuro.bsuirschedule.feature.flow.FlowFragment
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class MainActivity : AppCompatActivity(), FlowFragment.ActionCallback {

    companion object {

        fun newIntent(context: Context) = Intent(context, MainActivity::class.java)
    }

    @Inject
    protected lateinit var getAppTheme: GetAppTheme

    override fun onCreate(savedInstanceState: Bundle?) {
        app.appComponent.inject(this)
        val themeStyleRes = getThemeStyleRes()
        theme.applyStyle(themeStyleRes, true)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (savedInstanceState == null) {
            showFlowFragment()
        }
    }

    override fun onBackPressed() {
        val flowFragment = supportFragmentManager.fragments.firstOrNull() as? BackPressedHandler
        if (flowFragment?.onBackPressed() == true) return
        super.onBackPressed()
    }

    override fun onFlowRecreate() {
        finish()
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        startActivity(intent)
    }

    private fun showFlowFragment() {
        supportFragmentManager.commit {
            replace(activityHostFragment.id, FlowFragment.newInstance())
        }
    }

    @StyleRes
    private fun getThemeStyleRes() = runBlocking {
        when (getAppTheme.execute(BaseInteractor.NoParams).getOrDefault(AppTheme.DEFAULT)) {
            AppTheme.LIGHT -> R.style.LightStyleTheme
            AppTheme.DARK -> R.style.DarkStyleTheme
        }
    }
}
