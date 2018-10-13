package com.pechuro.bsuirschedule.ui.fragment.classes.weekitems

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
import com.pechuro.bsuirschedule.ui.fragment.classes.transactioninfo.impl.ClassesWeekInformation
import com.pechuro.bsuirschedule.ui.fragment.classes.weekitems.adapter.ClassesWeekAdapter
import javax.inject.Inject

class ClassesWeekFragment : BaseFragment<FragmentListBinding, ClassesWeekViewModel>() {
    companion object {
        const val ARG_INFO = "arg_information"

        fun newInstance(info: ClassesWeekInformation): ClassesWeekFragment {
            val args = Bundle()
            args.putParcelable(ARG_INFO, info)

            val fragment = ClassesWeekFragment()
            fragment.arguments = args
            return fragment
        }
    }

    @Inject
    lateinit var mLayoutManager: LinearLayoutManager
    @Inject
    lateinit var mAdapter: ClassesWeekAdapter

    override val mViewModel: ClassesWeekViewModel
        get() = ViewModelProviders.of(this, mViewModelFactory).get(ClassesWeekViewModel::class.java)
    override val bindingVariable: Int
        get() = BR._all
    override val layoutId: Int
        get() = R.layout.fragment_list

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUp()
        subscribeToLiveData()
        loadData()
    }

    private fun loadData() {
        val info: ClassesWeekInformation? = arguments?.getParcelable(ARG_INFO)
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