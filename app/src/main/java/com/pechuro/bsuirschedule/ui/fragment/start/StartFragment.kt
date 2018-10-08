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

    override val mViewModel: StartFragmentViewModel
        get() = ViewModelProviders.of(this, mViewModelFactory).get(StartFragmentViewModel::class.java)
    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.fragment_start

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mViewDataBinding.fab.setOnClickListener {
            AddDialog.newInstance().show(fragmentManager, "dfs")
        }
    }
}