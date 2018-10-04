package com.pechuro.bsuirschedule.ui.fragment.list

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
import com.pechuro.bsuirschedule.ui.fragment.classes.DayScheduleInformation
import javax.inject.Inject

class ListFragment : BaseFragment<FragmentListBinding, ListViewModel>() {

    private lateinit var mBinding: FragmentListBinding

    @Inject
    lateinit var mLayoutManager: LinearLayoutManager
    @Inject
    lateinit var mListAdapter: ListAdapter

    companion object {
        const val ARG_INFO = "arg_information"

        fun newInstance(info: DayScheduleInformation): ListFragment {
            val args = Bundle()
            args.putParcelable(ARG_INFO, info)

            val fragment = ListFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override val mViewModel: ListViewModel
        get() = ViewModelProviders.of(this, mViewModelFactory).get(ListViewModel::class.java)
    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.fragment_list

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinding = mViewDataBinding
        setUp()
        subscribeToLiveData()

        if (savedInstanceState == null) {
            val info: DayScheduleInformation? = arguments?.getParcelable(ARG_INFO)
            info?.let { mViewModel.loadData(it) }
        }
    }

    private fun setUp() {
        mLayoutManager.orientation = RecyclerView.VERTICAL
        mBinding.cacheRecyclerView.layoutManager = mLayoutManager
        mBinding.cacheRecyclerView.adapter = mListAdapter
    }

    private fun subscribeToLiveData() {
        mViewModel.listItemsLiveData.observe(this,
                Observer {
                    if (it != null) mViewModel.addItems(it)
                })
    }
}