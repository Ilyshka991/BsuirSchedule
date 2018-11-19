package com.pechuro.bsuirschedule.ui.fragment.draweroptions

import android.content.Context
import android.os.Bundle
import androidx.databinding.library.baseAdapters.BR
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.pechuro.bsuirschedule.R
import com.pechuro.bsuirschedule.databinding.DialogDrawerOptionsBinding
import com.pechuro.bsuirschedule.ui.base.BaseDialog
import com.pechuro.bsuirschedule.ui.data.ScheduleInformation

class DrawerOptionsDialog : BaseDialog<DialogDrawerOptionsBinding, DrawerOptionsDialogViewModel>() {
    private var _callback: DrawerOptionsCallback? = null
    private val _scheduleInfo: ScheduleInformation? by lazy {
        arguments?.getParcelable<ScheduleInformation>(ARG_SCHEDULE_INFO)
    }

    override val viewModel: DrawerOptionsDialogViewModel
        get() = ViewModelProviders.of(this, mViewModelFactory).get(DrawerOptionsDialogViewModel::class.java)
    override val bindingVariables: Map<Int, Any?>
        get() = mapOf(Pair(BR.viewModel, viewModel), Pair(BR.info, _scheduleInfo))
    override val layoutId: Int
        get() = R.layout.dialog_drawer_options

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        _callback = context as? DrawerOptionsCallback
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setEventListener()
    }

    private fun setEventListener() {
        viewModel.command.observe(this, Observer {
            when (it) {
                is OnDeleted -> _callback?.onItemDeleted(it.info)
                is OnUpdated -> _callback?.onItemUpdated(it.info)
                is OnCancel -> dismiss()
            }
            dismiss()
        })
    }

    interface DrawerOptionsCallback {
        fun onItemUpdated(info: ScheduleInformation)

        fun onItemDeleted(info: ScheduleInformation)
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

