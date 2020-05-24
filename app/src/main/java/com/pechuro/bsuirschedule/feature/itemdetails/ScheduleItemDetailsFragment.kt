package com.pechuro.bsuirschedule.feature.itemdetails

import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.OvalShape
import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.lifecycle.observe
import androidx.recyclerview.widget.SimpleItemAnimator
import com.pechuro.bsuirschedule.R
import com.pechuro.bsuirschedule.common.base.BaseFragment
import com.pechuro.bsuirschedule.domain.entity.LessonPriority
import com.pechuro.bsuirschedule.ext.*
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
                onNoteChanged = { viewModel.updateNote(it) }
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
    }

    private fun observeData() {
        viewModel.detailsData.nonNull().observe(viewLifecycleOwner) { (scheduleItem, details) ->
            val title = getString(R.string.item_details_title, scheduleItem.subject, scheduleItem.lessonType)
            scheduleItemDetailsTitle.text = title
            adapter.submitList(details)
        }
    }

    private fun selectPriority(selectedPriority: LessonPriority) {
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
}
