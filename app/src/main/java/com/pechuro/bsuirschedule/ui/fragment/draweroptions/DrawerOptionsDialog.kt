package com.pechuro.bsuirschedule.ui.fragment.draweroptions

import android.os.Bundle
import androidx.databinding.library.baseAdapters.BR
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.pechuro.bsuirschedule.R
import com.pechuro.bsuirschedule.databinding.DialogDrawerOptionsBinding
import com.pechuro.bsuirschedule.ui.base.BaseDialog
import com.pechuro.bsuirschedule.ui.data.ScheduleInformation
import com.pechuro.bsuirschedule.ui.utils.EventBus

class DrawerOptionsDialog : BaseDialog<DialogDrawerOptionsBinding, DrawerOptionsDialogViewModel>() {
    private val _scheduleInfo: ScheduleInformation? by lazy {
        arguments?.getParcelable<ScheduleInformation>(ARG_SCHEDULE_INFO)
    }

    override val viewModel: DrawerOptionsDialogViewModel
        get() = ViewModelProviders.of(this, mViewModelFactory).get(DrawerOptionsDialogViewModel::class.java)
    override val bindingVariables: Map<Int, Any?>
        get() = mapOf(Pair(BR.viewModel, viewModel), Pair(BR.info, _scheduleInfo))
    override val layoutId: Int
        get() = R.layout.dialog_drawer_options

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setEventListener()
    }

    private fun setEventListener() {
        viewModel.command.observe(this, Observer {
            when (it) {
                is OnDeleted -> EventBus.publish(OnScheduleDeletedEvent(it.info))
                is OnUpdated -> EventBus.publish(OnScheduleUpdatedEvent(it.info))
                is OnCancel -> dismiss()
            }
            dismiss()
        })
    }

    companion object {
        private const val ARG_SCHEDULE_INFO = "schedule_info"

        fun newInstance(info: ScheduleInformation) =
                DrawerOptionsDialog().apply {
                    arguments = Bundle().apply {
                        putParcelable(ARG_SCHEDULE_INFO, info)
                    }
                }
    }
}

