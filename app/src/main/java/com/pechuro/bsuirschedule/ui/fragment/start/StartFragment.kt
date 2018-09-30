package com.pechuro.bsuirschedule.ui.fragment.start

import android.os.Bundle
import android.view.View
import androidx.databinding.library.baseAdapters.BR
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.pechuro.bsuirschedule.R
import com.pechuro.bsuirschedule.databinding.FragmentStartBinding
import com.pechuro.bsuirschedule.ui.base.BaseFragment
import com.pechuro.bsuirschedule.ui.fragment.start.StartFragmentDirections.actionStartToSchedule

class StartFragment : BaseFragment<FragmentStartBinding, StartFragmentViewModel>() {

    private lateinit var binding: FragmentStartBinding

    companion object {
        fun newInstance(): StartFragment {
            val args = Bundle()
            val fragment = StartFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override val mViewModel: StartFragmentViewModel
        get() = ViewModelProviders.of(this, mViewModelFactory).get(StartFragmentViewModel::class.java)
    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.fragment_start

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = mViewDataBinding
        binding.fab.setOnClickListener {
            findNavController().navigate(actionStartToSchedule())
        }
    }
}