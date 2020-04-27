package com.pechuro.bsuirschedule.feature.appwidgetconfiguration

import android.appwidget.AppWidgetManager
import android.content.Intent
import android.os.Bundle
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.SimpleItemAnimator
import com.pechuro.bsuirschedule.R
import com.pechuro.bsuirschedule.common.base.BaseActivity
import com.pechuro.bsuirschedule.domain.entity.SubgroupNumber
import com.pechuro.bsuirschedule.ext.*
import com.pechuro.bsuirschedule.feature.MainActivity
import com.pechuro.bsuirschedule.feature.confirmationdialog.ConfirmationDialog
import com.pechuro.bsuirschedule.feature.confirmationdialog.ConfirmationDialogButtonData
import com.pechuro.bsuirschedule.feature.optiondialog.OptionDialog
import com.pechuro.bsuirschedule.feature.optiondialog.OptionDialogButtonData
import kotlinx.android.synthetic.main.activity_configure_schedule_widget.*

class AppWidgetConfigurationActivity : BaseActivity() {

    override val layoutId: Int = R.layout.activity_configure_schedule_widget

    private val viewModel by lazy {
        initViewModel(AppWidgetConfigurationViewModel::class)
    }

    private val widgetId: Int by lazy(LazyThreadSafetyMode.NONE) {
        intent.extras?.getInt(
                AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID
        ) ?: AppWidgetManager.INVALID_APPWIDGET_ID
    }

    private val scheduleListAdapter = AppWidgetConfigurationAdapter(
            onScheduleClicked = {
                viewModel.dataProvider.setSelectedSchedule(it)
            }
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setResult(RESULT_CANCELED)
        viewModel.init(widgetId)
        initView()
        observeData()
    }

    override fun onBackPressed() {
        if (configureScheduleWidgetDoneButton.isEnabled) {
            showExitDialog()
        } else {
            finish()
        }
    }

    private fun initView() {
        configureScheduleWidgetToolbar.setNavigationOnClickListener {
            onBackPressed()
        }
        configureScheduleWidgetDoneButton.setSafeClickListener {
            onDone()
        }
        configureScheduleWidgetSubgroupNumber.setSafeClickListener {
            selectSubgroupNumber()
        }
        configureScheduleWidgetGoToAppButton.setSafeClickListener {
            startMainApp()
        }
        configureScheduleWidgetRecyclerView.adapter = scheduleListAdapter
        val recyclerItemAnimator = configureScheduleWidgetRecyclerView.itemAnimator as? SimpleItemAnimator
        recyclerItemAnimator?.supportsChangeAnimations = false
    }

    private fun observeData() {
        viewModel.dataProvider.subgroupNumberData.nonNull().observe(this) { number ->
            configureScheduleWidgetSubgroupNumber.setMessage(number.formattedStringRes)
        }
        viewModel.dataProvider.selectedScheduleData.observe(this, Observer {
            updateLayoutState()
        })
        viewModel.scheduleListData.nonNull().observe(this) {
            updateLayoutState()
            scheduleListAdapter.submitList(it)
        }
    }

    private fun updateLayoutState() {
        val isScheduleListEmpty = (viewModel.scheduleListData.value ?: emptyList()).isEmpty()
        val isScheduleSelected = viewModel.dataProvider.selectedScheduleData.value != null
        configureScheduleWidgetEmptySchedulesParentView.isVisible = isScheduleListEmpty
        configureScheduleWidgetRecyclerView.isVisible = !isScheduleListEmpty
        configureScheduleWidgetSubgroupNumber.isVisible = !isScheduleListEmpty
        configureScheduleWidgetSelectScheduleText.isVisible = !isScheduleListEmpty
        configureScheduleWidgetDoneButton.isEnabled = !isScheduleListEmpty && isScheduleSelected
    }

    private fun showExitDialog() {
        ConfirmationDialog
                .Builder()
                .setTitle(getString(R.string.configure_schedule_widget_msg_exit))
                .setPositiveAction(ConfirmationDialogButtonData(
                        text = getString(R.string.action_discard),
                        onClick = { finish() }
                ))
                .setNegativeAction(ConfirmationDialogButtonData(
                        text = getString(R.string.action_cancel)))
                .build()
                .show(supportFragmentManager, ConfirmationDialog.TAG)
    }

    private fun selectSubgroupNumber() {
        val availableNumbers = SubgroupNumber.values()
        val selectedSubgroupNumber = viewModel.dataProvider.subgroupNumberData.requireValue
        val options = availableNumbers.map { number ->
            OptionDialogButtonData(
                    text = getString(number.formattedStringRes),
                    selected = number == selectedSubgroupNumber
            )
        }
        val listener = object : OptionDialog.OptionButtonClickListener {
            override fun onClick(position: Int) {
                viewModel.dataProvider.setSubgroupNumber(availableNumbers[position])
            }
        }
        val title = getString(R.string.modify_schedule_item_title_select_subgroup)
        OptionDialog.Builder()
                .setTitle(title)
                .setActions(options, listener)
                .build()
                .show(supportFragmentManager, OptionDialog.TAG)
    }

    private fun startMainApp() {
        val intent = MainActivity.newIntent(this)
        startActivity(intent)
        finish()
    }

    private fun onDone() {
        viewModel.onDone()
        val resultIntent = Intent().apply {
            putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId)
        }
        setResult(RESULT_OK, resultIntent)
        finish()
    }
}