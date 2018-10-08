package com.pechuro.bsuirschedule.ui.fragment.adddialog.addfragment

import android.os.Bundle
import androidx.databinding.library.baseAdapters.BR
import androidx.lifecycle.ViewModelProviders
import com.pechuro.bsuirschedule.R
import com.pechuro.bsuirschedule.databinding.FragmentAddBinding
import com.pechuro.bsuirschedule.ui.base.BaseFragment

class AddFragment : BaseFragment<FragmentAddBinding, AddFragmentViewModel>() {
    companion object {
        const val ARG_INFO = "arg_add_fragment_schedule_type"

        fun newInstance(scheduleType: Int): AddFragment {
            val args = Bundle()
            args.putInt(ARG_INFO, scheduleType)

            val fragment = AddFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override val mViewModel: AddFragmentViewModel
        get() = ViewModelProviders.of(this, mViewModelFactory).get(AddFragmentViewModel::class.java)
    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.fragment_add
}