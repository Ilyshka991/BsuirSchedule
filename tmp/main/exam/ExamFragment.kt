package com.pechuro.bsuirschedule.feature.main.exam

import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.pechuro.bsuirschedule.BR
import com.pechuro.bsuirschedule.R
import com.pechuro.bsuirschedule.databinding.FragmentListBinding
import com.pechuro.bsuirschedule.common.BaseFragment
import com.pechuro.bsuirschedule.feature.main.ScheduleInformation
import com.pechuro.bsuirschedule.feature.main.exam.adapter.ExamAdapter
import javax.inject.Inject

class ExamFragment : BaseFragment<FragmentListBinding, ExamViewModel>() {

    @Inject
    lateinit var linearLayoutManager: LinearLayoutManager
    @Inject
    lateinit var recyclerAdapter: ExamAdapter

    override val viewModel: ExamViewModel
        get() = ViewModelProviders.of(this, viewModelFactory).get(ExamViewModel::class.java)
    override val layoutId: Int
        get() = R.layout.fragment_list
    override val bindingVariables: Map<Int, Any>
        get() = mapOf(BR.data to viewModel)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupView()
        subscribeToLiveData()
        if (savedInstanceState == null) loadData()
    }

    private fun setupView() {
        viewDataBinding.recyclerView.apply {
            layoutManager = linearLayoutManager
            adapter = recyclerAdapter
        }
    }

    private fun loadData() {
        val info: ScheduleInformation? = arguments?.getParcelable(ARG_INFO)
        info?.let { viewModel.loadData(it) }
    }

    private fun subscribeToLiveData() {
        viewModel.listItemsLiveData.observe(this,
                Observer {
                    recyclerAdapter.setItems(it)
                })
    }

    companion object {
        const val ARG_INFO = "arg_information"

        fun newInstance(info: ScheduleInformation) = ExamFragment().apply {
            arguments = Bundle().apply {
                putParcelable(ARG_INFO, info)
            }
        }
    }
}