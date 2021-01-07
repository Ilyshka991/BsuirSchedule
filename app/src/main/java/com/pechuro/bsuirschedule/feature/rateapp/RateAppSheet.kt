package com.pechuro.bsuirschedule.feature.rateapp

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.bsuir.pechuro.bsuirschedule.R
import com.pechuro.bsuirschedule.common.AppAnalytics
import com.pechuro.bsuirschedule.common.AppAnalyticsEvent
import com.pechuro.bsuirschedule.common.base.BaseBottomSheetDialog
import com.pechuro.bsuirschedule.ext.setSafeClickListener
import kotlinx.android.synthetic.main.sheet_rate_app.*

class RateAppSheet : BaseBottomSheetDialog() {

    companion object {

        const val TAG = "RateAppSheet"

        fun newInstance() = RateAppSheet()
    }

    override val layoutId: Int = R.layout.sheet_rate_app

    private val viewModel by lazy(LazyThreadSafetyMode.NONE) {
        initViewModel(RateAppViewModel::class)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        AppAnalytics.report(AppAnalyticsEvent.RateApp.Opened)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    override fun onCancel(dialog: DialogInterface) {
        viewModel.onRateAppAskLater()
    }

    private fun initView() {
        rateAppRateButton.setSafeClickListener {
            AppAnalytics.report(AppAnalyticsEvent.RateApp.RateClicked)
            viewModel.onRateAppAskNever()
            rateApp(it.context)
            dismiss()
        }
        rateAppNoButton.setSafeClickListener {
            AppAnalytics.report(AppAnalyticsEvent.RateApp.NotRemindClicked)
            viewModel.onRateAppAskNever()
            dismiss()
        }
        rateAppLaterButton.setSafeClickListener {
            AppAnalytics.report(AppAnalyticsEvent.RateApp.LaterClicked)
            viewModel.onRateAppAskLater()
            dismiss()
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
}