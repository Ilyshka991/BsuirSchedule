package com.pechuro.bsuirschedule.ui.fragment.adddialog

import android.os.Bundle
import android.widget.Toast
import androidx.databinding.library.baseAdapters.BR
import androidx.lifecycle.ViewModelProviders
import com.pechuro.bsuirschedule.R
import com.pechuro.bsuirschedule.databinding.DialogFragmentAddBinding
import com.pechuro.bsuirschedule.ui.base.BaseDialog


class AddDialogFragment : BaseDialog<DialogFragmentAddBinding, AddDialogFragmentViewModel>() {

    companion object {
        fun newInstance(): AddDialogFragment {
            val fragment = AddDialogFragment()
            val bundle = Bundle()
            fragment.arguments = bundle
            return fragment
        }
    }

    override val mViewModel: AddDialogFragmentViewModel
        get() = ViewModelProviders.of(this, mViewModelFactory).get(AddDialogFragmentViewModel::class.java)
    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.dialog_fragment_add

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mViewDataBinding.button
                .setOnClickListener {
                    Toast.makeText(context, "Clicked", Toast.LENGTH_LONG).show()
                }
    }
}