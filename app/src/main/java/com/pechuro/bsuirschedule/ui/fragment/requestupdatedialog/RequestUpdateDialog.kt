package com.pechuro.bsuirschedule.ui.fragment.requestupdatedialog

import android.os.Bundle
import androidx.databinding.library.baseAdapters.BR
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.pechuro.bsuirschedule.R
import com.pechuro.bsuirschedule.databinding.DialogRequestScheduleUpdateBinding
import com.pechuro.bsuirschedule.ui.base.BaseDialog
import com.pechuro.bsuirschedule.ui.data.ScheduleInformation
import com.pechuro.bsuirschedule.ui.fragment.draweroptions.DrawerOptionEvent

import com.pechuro.bsuirschedule.ui.utils.EventBus

class RequestUpdateDialog : BaseDialog<DialogRequestScheduleUpdateBinding, RequestUpdateDialogViewModel>() {

    override val viewModel: RequestUpdateDialogViewModel
        get() = ViewModelProviders.of(this, viewModelFactory).get(RequestUpdateDialogViewModel::class.java)
    override val layoutId: Int
        get() = R.layout.dialog_request_schedule_update
    override val bindingVariables: Map<Int, Any?>
        get() = mapOf(BR.viewModel to viewModel, BR.info to _scheduleInfo)

    private val _scheduleInfo: ScheduleInformation? by lazy {
        arguments?.getParcelable<ScheduleInformation>(ARG_SCHEDULE_INFO)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setStatusListener()
    }

    private fun setStatusListener() {
        viewModel.status.observe(this, Observer {
            when (it) {
                is Status.OnUpdated -> {
                    EventBus.publish(DrawerOptionEvent.OnScheduleUpdated(it.info))
                    dismiss()
                }
                is Status.OnCancel -> {
                    dismiss()
                }
            }
        })
    }


    companion object {
        const val TAG = "request_update_dialog"
        private const val ARG_SCHEDULE_INFO = "schedule_info"

        fun newInstance(info: ScheduleInformation) =
                RequestUpdateDialog().apply {
                    arguments = Bundle().apply {
                        putParcelable(ARG_SCHEDULE_INFO, info)
                    }
                }
    }
}

