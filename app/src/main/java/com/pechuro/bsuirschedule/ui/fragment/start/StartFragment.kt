package com.pechuro.bsuirschedule.ui.fragment.start

import android.os.Bundle
import android.view.View
import androidx.databinding.library.baseAdapters.BR
import androidx.lifecycle.ViewModelProviders
import com.pechuro.bsuirschedule.R
import com.pechuro.bsuirschedule.databinding.FragmentStartBinding
import com.pechuro.bsuirschedule.ui.base.BaseFragment
import com.pechuro.bsuirschedule.ui.fragment.adddialog.AddDialog

class StartFragment : BaseFragment<FragmentStartBinding, StartFragmentViewModel>() {
    override val viewModel: StartFragmentViewModel
        get() = ViewModelProviders.of(this, viewModelFactory).get(StartFragmentViewModel::class.java)
    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.fragment_start

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setListeners()
    }

    private fun setListeners() {
        viewDataBinding.fab.setOnClickListener {
            AddDialog.newInstance().show(fragmentManager, "add_dialog")
        }
    }

    companion object {
        fun newInstance(): StartFragment {
            val args = Bundle()
            val fragment = StartFragment()
            fragment.arguments = args
            return fragment
        }
    }
}