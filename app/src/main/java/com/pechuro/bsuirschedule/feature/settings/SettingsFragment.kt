package com.pechuro.bsuirschedule.feature.settings

import android.os.Bundle
import android.view.View
import com.pechuro.bsuirschedule.BuildConfig
import com.pechuro.bsuirschedule.R
import com.pechuro.bsuirschedule.common.base.BaseFragment
import com.pechuro.bsuirschedule.ext.nonNull
import com.pechuro.bsuirschedule.ext.observe
import com.pechuro.bsuirschedule.ext.setSafeClickListener
import com.pechuro.bsuirschedule.ext.setVisibleWithAlpha
import kotlinx.android.synthetic.main.fragment_settings.*

class SettingsFragment : BaseFragment() {

    companion object {

        const val TAG = "SettingsFragment"

        fun newInstance() = SettingsFragment()
    }

    override val layoutId = R.layout.fragment_settings

    private val viewModel by lazy(LazyThreadSafetyMode.NONE) {
        initViewModel(SettingsViewModel::class)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        observeData()
        setViewListeners()
    }

    override fun onBackPressed() = viewModel.setNormalState()

    private fun initView() {
        settingsToolbar.apply {
            setNavigationOnClickListener {
                activity?.onBackPressed()
            }
        }
        settingsThemeButton.text = getString(R.string.settings_action_change_theme, "light")
        settingsVersionText.setMessage(BuildConfig.VERSION_NAME)
    }

    private fun observeData() {
        viewModel.stateData.nonNull().observe(viewLifecycleOwner) {
            updateLayoutState(it)
        }
    }

    private fun updateLayoutState(state: SettingsViewModel.State) {
        when (state) {
            is SettingsViewModel.State.Idle -> {
                settingActionParentView.setVisibleWithAlpha(true)
                settingsInfoLoadLoaderView.setVisibleWithAlpha(false)
                settingsInfoLoadErrorParentView.setVisibleWithAlpha(false)
            }
            is SettingsViewModel.State.Loading -> {
                settingActionParentView.setVisibleWithAlpha(false)
                settingsInfoLoadLoaderView.setVisibleWithAlpha(true)
                settingsInfoLoadErrorParentView.setVisibleWithAlpha(false)
            }
            is SettingsViewModel.State.Error -> {
                settingActionParentView.setVisibleWithAlpha(false)
                settingsInfoLoadLoaderView.setVisibleWithAlpha(false)
                settingsInfoLoadErrorParentView.setVisibleWithAlpha(true)
            }
        }
    }

    private fun setViewListeners() {
        settingsThemeButton.setSafeClickListener {

        }
        settingsUpdateInfoButton.setSafeClickListener {
            viewModel.updateInfo()
        }
        settingsInfoLoadErrorButton.setSafeClickListener {
            viewModel.updateInfo()
        }
        settingsRateAppButton.setSafeClickListener {

        }
        settingsSendFeedbackButton.setSafeClickListener {

        }
        settingsTermOfUseButton.setSafeClickListener {

        }
    }
}