package com.pechuro.bsuirschedule.ui.fragment.itemoptions

import android.os.Bundle
import androidx.databinding.library.baseAdapters.BR
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.pechuro.bsuirschedule.R
import com.pechuro.bsuirschedule.databinding.DialogItemOptionsBinding
import com.pechuro.bsuirschedule.ui.base.BaseDialog
import com.pechuro.bsuirschedule.ui.utils.EventBus

class ItemOptionsDialog : BaseDialog<DialogItemOptionsBinding, ItemOptionsDialogViewModel>() {
    private val _itemId: Int? by lazy {
        arguments?.getInt(ARG_ITEM_ID)
    }

    override val viewModel: ItemOptionsDialogViewModel
        get() = ViewModelProviders.of(this, viewModelFactory).get(ItemOptionsDialogViewModel::class.java)
    override val bindingVariables: Map<Int, Any?>
        get() = mapOf(Pair(BR.viewModel, viewModel), Pair(BR.id, _itemId))
    override val layoutId: Int
        get() = R.layout.dialog_item_options

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setStatusListener()
    }

    private fun setStatusListener() {
        viewModel.status.observe(this, Observer {
            when (it) {
                is Status.OnDeleted -> EventBus.publish(ItemOptionEvent.OnLessonDeleted(it.itemId))
            }
            dismiss()
        })
    }

    companion object {
        const val TAG = "item_options_dialog"
        private const val ARG_ITEM_ID = "item_id"

        fun newInstance(itemId: Int) =
                ItemOptionsDialog().apply {
                    arguments = Bundle().apply {
                        putInt(ARG_ITEM_ID, itemId)
                    }
                }
    }
}

