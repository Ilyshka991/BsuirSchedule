package com.pechuro.bsuirschedule.ui.fragment.adddialog.addfragment

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.databinding.library.baseAdapters.BR
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.pechuro.bsuirschedule.R
import com.pechuro.bsuirschedule.databinding.FragmentAddBinding
import com.pechuro.bsuirschedule.ui.base.BaseFragment

class AddFragment : BaseFragment<FragmentAddBinding, AddFragmentViewModel>() {
    companion object {
        const val ARG_SCHEDULE_TYPE = "arg_add_fragment_schedule_type"

        fun newInstance(scheduleType: Int): AddFragment {
            val args = Bundle()
            args.putInt(ARG_SCHEDULE_TYPE, scheduleType)

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(arguments?.getInt(ARG_SCHEDULE_TYPE)) {
            if (this != null && savedInstanceState == null) {
                mViewModel.loadSuggestions(this)
            }
        }

        subscribeToLiveData()
    }

    private fun subscribeToLiveData() {
        mViewModel.suggestions.observe(this,
                Observer {
                    if (it != null) {
                        val adapter = ArrayAdapter<String>(
                                mViewDataBinding.textInput.context,
                                R.layout.item_autocomplete_adapter,
                                it)
                        mViewDataBinding.textInput.setAdapter(adapter)
                    }
                })
    }
}