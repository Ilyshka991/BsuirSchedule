package com.pechuro.bsuirschedule.ui.fragment.exam

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pechuro.bsuirschedule.BR
import com.pechuro.bsuirschedule.R
import com.pechuro.bsuirschedule.databinding.FragmentListBinding
import com.pechuro.bsuirschedule.ui.activity.navigation.ScheduleInformation
import com.pechuro.bsuirschedule.ui.base.BaseFragment
import com.pechuro.bsuirschedule.ui.fragment.exam.adapter.ExamAdapter
import javax.inject.Inject

class ExamFragment : BaseFragment<FragmentListBinding, ExamViewModel>() {
    companion object {
        const val ARG_INFO = "arg_information"

        fun newInstance(info: ScheduleInformation): ExamFragment {
            val args = Bundle()
            args.putParcelable(ARG_INFO, info)

            val fragment = ExamFragment()
            fragment.arguments = args
            return fragment
        }
    }

    @Inject
    lateinit var mLayoutManager: LinearLayoutManager
    @Inject
    lateinit var mAdapter: ExamAdapter

    override val mViewModel: ExamViewModel
        get() = ViewModelProviders.of(this, mViewModelFactory).get(ExamViewModel::class.java)
    override val bindingVariable: Int
        get() = BR.data
    override val layoutId: Int
        get() = R.layout.fragment_list

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUp()
        subscribeToLiveData()
        loadData()
    }

    private fun loadData() {
        val info: ScheduleInformation? = arguments?.getParcelable(ARG_INFO)
        info?.let { mViewModel.loadData(it) }
    }

    private fun setUp() {
        mLayoutManager.orientation = RecyclerView.VERTICAL
        mViewDataBinding.recyclerView.layoutManager = mLayoutManager
        mViewDataBinding.recyclerView.adapter = mAdapter
    }

    private fun subscribeToLiveData() {
        mViewModel.listItemsLiveData.observe(this,
                Observer {
                    if (it != null) {
                        mAdapter.setItems(it)
                    }
                })
    }
}