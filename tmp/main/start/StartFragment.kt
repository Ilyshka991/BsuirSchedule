package com.pechuro.bsuirschedule.feature.main.start

import android.os.Bundle
import androidx.databinding.library.baseAdapters.BR
import androidx.lifecycle.ViewModelProviders
import com.pechuro.bsuirschedule.R
import com.pechuro.bsuirschedule.databinding.FragmentStartBinding
import com.pechuro.bsuirschedule.common.BaseFragment
import com.pechuro.bsuirschedule.feature.main.adddialog.AddDialog

class StartFragment : BaseFragment<FragmentStartBinding, StartFragmentViewModel>() {

    override val viewModel: StartFragmentViewModel
        get() = ViewModelProviders.of(this, viewModelFactory).get(StartFragmentViewModel::class.java)
    override val layoutId: Int
        get() = R.layout.fragment_start
    override val bindingVariables: Map<Int, Any>
        get() = mapOf(BR.viewModel to viewModel)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setListeners()
    }

    private fun setListeners() {
        viewDataBinding.fab.setOnClickListener {
            AddDialog.newInstance().show(fragmentManager!!, AddDialog.TAG)
        }
    }

    companion object {
        fun newInstance() = StartFragment().apply {
            arguments = Bundle().apply {
            }
        }
    }
}