package com.pechuro.bsuirschedule.ui.fragment.optiondialog

import android.content.Context
import android.os.Bundle
import androidx.databinding.library.baseAdapters.BR
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.pechuro.bsuirschedule.R
import com.pechuro.bsuirschedule.databinding.DialogDrawerOptionsBinding
import com.pechuro.bsuirschedule.ui.activity.navigation.transactioninfo.ScheduleInformation
import com.pechuro.bsuirschedule.ui.base.BaseDialog

class DrawerOptionsDialog : BaseDialog<DialogDrawerOptionsBinding, DrawerOptionsDialogViewModel>() {
    private var _callback: DrawerOptionsCallback? = null

    override val viewModel: DrawerOptionsDialogViewModel
        get() = ViewModelProviders.of(this, mViewModelFactory).get(DrawerOptionsDialogViewModel::class.java)
    override val bindingVariable: Int
        get() = BR._all
    override val layoutId: Int
        get() = R.layout.dialog_drawer_options

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        _callback = context as? DrawerOptionsCallback
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setListeners()
        setEventListener()
    }

    private fun setEventListener() {
        viewModel.command.observe(this, Observer {
            when (it) {
                is OnDelete -> _callback?.onItemDeleted(it.info)
                is OnUpdate -> _callback?.onItemUpdated(it.info)
            }
            dismiss()
        })
    }

    private fun setListeners() {
        viewDataBinding.buttonUpdate.setOnClickListener {
            val info: ScheduleInformation? = arguments?.getParcelable(ARG_SCHEDULE_INFO)
            info?.let { viewModel.update(it) }
        }
        viewDataBinding.buttonDelete.setOnClickListener {
            val info: ScheduleInformation? = arguments?.getParcelable(ARG_SCHEDULE_INFO)
            info?.let { viewModel.delete(it) }
        }
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

