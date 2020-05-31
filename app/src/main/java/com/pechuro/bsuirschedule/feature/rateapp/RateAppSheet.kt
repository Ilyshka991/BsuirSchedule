package com.pechuro.bsuirschedule.feature.rateapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.pechuro.bsuirschedule.R
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
        rateAppRateButton.setSafeClickListener {
            rateApp(it.context)
            dismiss()
        }
        rateAppNoButton.setSafeClickListener {
            viewModel.onDismissRateApp()
            dismiss()
        }
        rateAppLaterButton.setSafeClickListener {
            viewModel.onRateAppLater()
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