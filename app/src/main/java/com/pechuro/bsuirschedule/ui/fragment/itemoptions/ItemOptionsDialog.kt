package com.pechuro.bsuirschedule.ui.fragment.itemoptions

import android.content.Context
import android.os.Bundle
import androidx.databinding.library.baseAdapters.BR
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.pechuro.bsuirschedule.R
import com.pechuro.bsuirschedule.databinding.DialogItemOptionsBinding
import com.pechuro.bsuirschedule.ui.base.BaseDialog

class ItemOptionsDialog : BaseDialog<DialogItemOptionsBinding, ItemOptionsDialogViewModel>() {
    private var _callback: DrawerOptionsCallback? = null
    private val _itemId: Int? by lazy {
        arguments?.getInt(ARG_ITEM_ID)
    }

    override val viewModel: ItemOptionsDialogViewModel
        get() = ViewModelProviders.of(this, mViewModelFactory).get(ItemOptionsDialogViewModel::class.java)
    override val bindingVariables: Map<Int, Any?>
        get() = mapOf(Pair(BR.viewModel, viewModel), Pair(BR.id, _itemId))
    override val layoutId: Int
        get() = R.layout.dialog_item_options

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
                is OnDeleted -> _callback?.onItemDeleted(it.itemId)
            }
            dismiss()
        })
    }

    interface DrawerOptionsCallback {
        fun onItemDeleted(itemId: Int)
    }

    companion object {
        private const val ARG_ITEM_ID = "item_id"

        fun newInstance(itemId: Int) =
                ItemOptionsDialog().apply {
                    arguments = Bundle().apply {
                        putInt(ARG_ITEM_ID, itemId)
                    }
                }
    }
}

