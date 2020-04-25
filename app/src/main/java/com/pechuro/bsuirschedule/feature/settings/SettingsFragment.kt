package com.pechuro.bsuirschedule.feature.settings

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
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
            Toast.makeText(requireContext(), "Not implemented", Toast.LENGTH_SHORT).show()
        }
        settingsUpdateInfoButton.setSafeClickListener {
            viewModel.updateInfo()
        }
        settingsInfoLoadErrorButton.setSafeClickListener {
            viewModel.updateInfo()
        }
        settingsRateAppButton.setSafeClickListener {
            rateApp(it.context)
        }
        settingsSendFeedbackButton.setSafeClickListener {
            sendFeedback(it.context)
        }
        settingsPrivacyPoliceButton.setSafeClickListener {
            openPrivacyPolice(it.context)
        }
    }

    private fun rateApp(context: Context) {
        val playStoreIntent = Intent(
                Intent.ACTION_VIEW,
                viewModel.appUriProvider.playStoreAppUri
        )
        playStoreIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)

        val webIntent = Intent(
                Intent.ACTION_VIEW,
                viewModel.appUriProvider.playStoreWebUri
        )

        val resultIntent = when {
            playStoreIntent.resolveActivity(context.packageManager) != null -> playStoreIntent
            webIntent.resolveActivity(context.packageManager) != null -> webIntent
            else -> null
        }

        if (resultIntent != null) {
            startActivity(resultIntent)
        }
    }

    private fun openPrivacyPolice(context: Context) {
        val intent = Intent(Intent.ACTION_VIEW, viewModel.appUriProvider.privacyPoliceUri)
        intent.resolveActivity(context.packageManager)?.let {
            startActivity(intent)
        }
    }

    private fun sendFeedback(context: Context) {
        val intent = Intent(Intent.ACTION_SENDTO, viewModel.appUriProvider.emailFeedbackUri).apply {
            val emailAddress = context.getString(R.string.settings_feedback_email_address)
            val subject = context.getString(R.string.settings_feedback_email_subject)
            putExtra(Intent.EXTRA_EMAIL, arrayOf(emailAddress))
            putExtra(Intent.EXTRA_SUBJECT, subject)
        }
        intent.resolveActivity(context.packageManager)?.let {
            startActivity(intent)
        }
    }
}