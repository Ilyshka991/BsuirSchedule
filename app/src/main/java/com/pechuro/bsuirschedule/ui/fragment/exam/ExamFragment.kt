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
import com.pechuro.bsuirschedule.ui.base.BaseFragment
import com.pechuro.bsuirschedule.ui.data.ScheduleInformation
import com.pechuro.bsuirschedule.ui.fragment.exam.adapter.ExamAdapter
import javax.inject.Inject

class ExamFragment : BaseFragment<FragmentListBinding, ExamViewModel>() {

    @Inject
    lateinit var layoutManager: LinearLayoutManager
    @Inject
    lateinit var adapter: ExamAdapter

    override val viewModel: ExamViewModel
        get() = ViewModelProviders.of(this, viewModelFactory).get(ExamViewModel::class.java)
    override val bindingVariables: Map<Int, Any>
        get() = mapOf(Pair(BR.data, viewModel))
    override val layoutId: Int
        get() = R.layout.fragment_list

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
        subscribeToLiveData()
        if (savedInstanceState == null) loadData()
    }

    private fun loadData() {
        val info: ScheduleInformation? = arguments?.getParcelable(ARG_INFO)
        info?.let { viewModel.loadData(it) }
    }

    private fun setupView() {
        layoutManager.orientation = RecyclerView.VERTICAL
        viewDataBinding.recyclerView.layoutManager = layoutManager
        viewDataBinding.recyclerView.adapter = adapter
    }

    private fun subscribeToLiveData() {
        viewModel.listItemsLiveData.observe(this,
                Observer {
                    if (it != null) {
                        adapter.setItems(it)
                    }
                })
    }

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
}