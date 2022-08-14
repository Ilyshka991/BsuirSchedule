package com.pechuro.bsuirschedule.feature.itemdetails

import android.content.Intent
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.OvalShape
import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.lifecycle.observe
import androidx.recyclerview.widget.SimpleItemAnimator
import com.bsuir.pechuro.bsuirschedule.R
import com.pechuro.bsuirschedule.common.AppAnalytics
import com.pechuro.bsuirschedule.common.AppAnalyticsEvent
import com.pechuro.bsuirschedule.common.base.BaseFragment
import com.pechuro.bsuirschedule.domain.entity.Building
import com.pechuro.bsuirschedule.domain.entity.LessonPriority
import com.pechuro.bsuirschedule.domain.entity.coordinates
import com.pechuro.bsuirschedule.ext.args
import com.pechuro.bsuirschedule.ext.color
import com.pechuro.bsuirschedule.ext.formattedColorRes
import com.pechuro.bsuirschedule.ext.formattedStringRes
import com.pechuro.bsuirschedule.ext.nonNull
import com.pechuro.bsuirschedule.feature.optiondialog.OptionDialog
import com.pechuro.bsuirschedule.feature.optiondialog.OptionDialogButtonData
import kotlinx.android.synthetic.main.fragment_schedule_item_details.*


class ScheduleItemDetailsFragment : BaseFragment() {

    companion object {

        private const val BUNDLE_ARGS = "BUNDLE_ARGS"

        const val TAG = "LessonDetailsFragment"

        fun newInstance(args: ScheduleItemDetailsArgs) = ScheduleItemDetailsFragment().apply {
            arguments = bundleOf(BUNDLE_ARGS to args)
        }
    }

    private val viewModel by lazy(LazyThreadSafetyMode.NONE) {
        initViewModel(ScheduleItemDetailsViewModel::class)
    }

    private val adapter by lazy(LazyThreadSafetyMode.NONE) {
        ScheduleItemDetailsAdapter(
            onPrioritySelected = ::selectPriority,
            onNoteChanged = { viewModel.updateNote(it) },
            onAuditoryClicked = ::openMap
        )
    }

    override val layoutId: Int = R.layout.fragment_schedule_item_details

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val args: ScheduleItemDetailsArgs by args(BUNDLE_ARGS)
        viewModel.init(args.schedule, args.itemId)
        initView()
        observeData()
    }

    private fun initView() {
        scheduleItemDetailsToolbar.setNavigationOnClickListener {
            activity?.onBackPressed()
        }
        scheduleItemDetailsRootRecycler.adapter = adapter
        val animator = scheduleItemDetailsRootRecycler.itemAnimator as? SimpleItemAnimator
        animator?.apply {
            addDuration = 0L
            changeDuration = 0L
        }
    }

    private fun observeData() {
        viewModel.detailsData.nonNull().observe(viewLifecycleOwner) { (scheduleItem, details) ->
            val title = getString(
                R.string.item_details_title,
                scheduleItem.subject,
                scheduleItem.lessonType
            )
            scheduleItemDetailsTitle.text = title
            adapter.submitList(details)
        }
    }

    private fun selectPriority(selectedPriority: LessonPriority) {
        AppAnalytics.report(AppAnalyticsEvent.Details.PriorityOpened)
        val availablePriorities = LessonPriority.values()
        val options = availablePriorities.map { prioriry ->
            val drawable = ShapeDrawable(OvalShape()).apply {
                paint.color = requireContext().color(prioriry.formattedColorRes)
            }
            OptionDialogButtonData(
                text = getString(prioriry.formattedStringRes),
                icon = drawable,
                selected = prioriry == selectedPriority
            )
        }
        val listener = object : OptionDialog.OptionButtonClickListener {
            override fun onClick(position: Int) {
                viewModel.updatePriority(availablePriorities[position])
            }
        }
        val title = getString(R.string.modify_schedule_item_title_select_priority)
        OptionDialog.Builder()
            .setTitle(title)
            .setActions(options, listener)
            .build()
            .show(childFragmentManager, OptionDialog.TAG)
    }

    private fun openMap(building: Building) {
        AppAnalytics.report(AppAnalyticsEvent.Details.LocationClicked(building))
        val context = this.context ?: return
        val latLng = building.coordinates ?: return
        val uri = viewModel.appUriProvider.provideGeoUri(latLng)
        val intent = Intent(Intent.ACTION_VIEW).apply {
            data = uri
        }
        intent.resolveActivity(context.packageManager)?.let {
            startActivity(intent)
        }
    }
}
