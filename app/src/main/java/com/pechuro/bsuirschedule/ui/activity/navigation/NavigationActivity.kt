package com.pechuro.bsuirschedule.ui.activity.navigation

import android.os.Bundle
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.pechuro.bsuirschedule.BR
import com.pechuro.bsuirschedule.R
import com.pechuro.bsuirschedule.databinding.ActivityNavigationBinding
import com.pechuro.bsuirschedule.ui.base.BaseActivity
import com.pechuro.bsuirschedule.ui.fragment.classes.ScheduleFragment
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import kotlinx.android.synthetic.main.activity_navigation.*
import javax.inject.Inject

class NavigationActivity :
        BaseActivity<ActivityNavigationBinding, NavigationActivityViewModel>(), HasSupportFragmentInjector {

    @Inject
    lateinit var fragmentDispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>

    private lateinit var binding: ActivityNavigationBinding

    override val mViewModel: NavigationActivityViewModel
        get() = ViewModelProviders.of(this, mViewModelFactory).get(NavigationActivityViewModel::class.java)
    override val layoutId: Int
        get() = R.layout.activity_navigation
    override val bindingVariable: Int
        get() = BR.viewModel

    override fun supportFragmentInjector() = fragmentDispatchingAndroidInjector

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = mViewDataBinding
        setup()

        supportFragmentManager.beginTransaction()
                .replace(binding.container.id, ScheduleFragment.newInstance()).commit()
    }

    override fun onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    private fun setup() {
        setSupportActionBar(bar)
        val toggle = ActionBarDrawerToggle(
                this, binding.drawerLayout, binding.bar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
    }
}
