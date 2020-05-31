package com.pechuro.bsuirschedule.feature.settings

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.pechuro.bsuirschedule.BuildConfig
import com.pechuro.bsuirschedule.R
import com.pechuro.bsuirschedule.common.base.BaseFragment
import com.pechuro.bsuirschedule.domain.entity.AppTheme
import com.pechuro.bsuirschedule.ext.*
import com.pechuro.bsuirschedule.feature.optiondialog.OptionDialog
import com.pechuro.bsuirschedule.feature.optiondialog.OptionDialogButtonData
import kotlinx.android.synthetic.main.fragment_settings.*
import java.util.*

class SettingsFragment : BaseFragment() {

    interface ActionCallback {

        fun onSettingsThemeChanged()
    }

    companion object {

        const val TAG = "SettingsFragment"

        fun newInstance() = SettingsFragment()
    }

    override val layoutId = R.layout.fragment_settings

    private val viewModel by lazy(LazyThreadSafetyMode.NONE) {
        initViewModel(SettingsViewModel::class)
    }

    private var actionCallback: ActionCallback? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        actionCallback = getCallbackOrNull()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        observeData()
        setViewListeners()
    }

    override fun onDetach() {
        super.onDetach()
        actionCallback = null
    }

    override fun handleBackPressed() = viewModel.setNormalState()

    private fun initView() {
        settingsToolbar.apply {
            setNavigationOnClickListener {
                activity?.onBackPressed()
            }
        }
        val currentTheme = getString(viewModel.getCurrentAppTheme().formattedStringRes).toLowerCase(Locale.getDefault())
        settingsThemeButton.text = getString(R.string.settings_action_change_theme, currentTheme)
        settingsVersionText.setMessage(BuildConfig.VERSION_NAME)
    }

    private fun observeData() {
        viewModel.stateData.nonNull().observe(viewLifecycleOwner) {
            updateLayoutState(it)
        }
        viewModel.themeChangedEvent.nonNull().observe(viewLifecycleOwner) {
            actionCallback?.onSettingsThemeChanged()
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
            selectTheme()
        }
        settingsUpdateInfoButton.setSafeClickListener {
            viewModel.updateInfo()
        }
        settingsInfoLoadErrorButton.setSafeClickListener {
            viewModel.updateInfo()
        }
        settingsRateAppButton.setSafeClickListener {
            viewModel.onRateApp()
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

    private fun selectTheme() {
        val availableThemes = AppTheme.getAvailable()
        val selectedTheme = viewModel.getCurrentAppTheme()
        val options = availableThemes.map { theme ->
            OptionDialogButtonData(
                    text = getString(theme.formattedStringRes),
                    selected = theme == selectedTheme
            )
        }
        val listener = object : OptionDialog.OptionButtonClickListener {
            override fun onClick(position: Int) {
                val newTheme = availableThemes[position]
                if (newTheme != selectedTheme) {
                    viewModel.setAppTheme(newTheme)
                }
            }
        }
        val title = getString(R.string.settings_title_select_theme)
        OptionDialog.Builder()
                .setTitle(title)
                .setActions(options, listener)
                .build()
                .show(childFragmentManager, OptionDialog.TAG)
    }
}