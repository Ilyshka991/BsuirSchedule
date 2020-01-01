package com.pechuro.bsuirschedule.feature.main.classes.classesitem

import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pechuro.bsuirschedule.R
import com.pechuro.bsuirschedule.local.preferences.PrefsConstants
import com.pechuro.bsuirschedule.local.preferences.PrefsDelegate
import com.pechuro.bsuirschedule.common.SharedPreferencesEvent
import com.pechuro.bsuirschedule.databinding.FragmentListBinding
import com.pechuro.bsuirschedule.feature.main.FabEvent
import com.pechuro.bsuirschedule.common.BaseFragment
import com.pechuro.bsuirschedule.ui.fragment.classes.classesitem.adapter.ClassesAdapter
import com.pechuro.bsuirschedule.ui.fragment.classes.data.classesinformation.ClassesBaseInformation
import com.pechuro.bsuirschedule.feature.main.employeedetails.EmployeeDetailsDialog
import com.pechuro.bsuirschedule.feature.main.itemoptions.ItemOptionsDialog
import com.pechuro.bsuirschedule.common.EventBus
import javax.inject.Inject

class ClassesItemFragment : BaseFragment<FragmentListBinding, ClassesItemViewModel>() {
    @Inject
    lateinit var linearLayoutManager: LinearLayoutManager
    @Inject
    lateinit var recyclerAdapter: ClassesAdapter
    @Inject
    lateinit var recyclerPool: RecyclerView.RecycledViewPool

    override val viewModel: ClassesItemViewModel
        get() = ViewModelProviders.of(this, viewModelFactory).get(ClassesItemViewModel::class.java)
    override val layoutId: Int
        get() = R.layout.fragment_list

    private var subgroupNumber: Int by com.pechuro.bsuirschedule.local.preferences.PrefsDelegate(com.pechuro.bsuirschedule.local.preferences.PrefsConstants.SUBGROUP_NUMBER, com.pechuro.bsuirschedule.local.preferences.PrefsConstants.SUBGROUP_ALL)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupView()
        subscribeToLiveData()
        loadData()
        setViewListeners()
        setEventListeners()
    }

    private fun setupView() {
        linearLayoutManager.recycleChildrenOnDetach = true

        viewDataBinding.recyclerView.apply {
            layoutManager = linearLayoutManager
            adapter = recyclerAdapter
            setRecycledViewPool(recyclerPool)
        }
    }

    private fun setEventListeners() {
        compositeDisposable.addAll(
                EventBus.listen(ClassesItemEvent::class.java).subscribe {
                    if (userVisibleHint) {
                        when (it) {
                            is ClassesItemEvent.OnItemLongClick -> ItemOptionsDialog.newInstance(it.id)
                                    .show(childFragmentManager, ItemOptionsDialog.TAG)
                            is ClassesItemEvent.OnItemClick -> EmployeeDetailsDialog.newInstance(it.employees)
                                    .show(childFragmentManager, EmployeeDetailsDialog.TAG)
                        }
                    }
                },
                EventBus.listen(SharedPreferencesEvent.OnChanged::class.java).subscribe {
                    when (it.key) {
                        com.pechuro.bsuirschedule.local.preferences.PrefsConstants.SUBGROUP_NUMBER -> loadData()
                    }
                })
    }

    private fun setViewListeners() {
        viewDataBinding.recyclerView
                .addOnScrollListener(object : RecyclerView.OnScrollListener() {
                    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                        super.onScrolled(recyclerView, dx, dy)
                        val visibleItemCount = linearLayoutManager.childCount
                        val totalItemCount = linearLayoutManager.itemCount
                        val pastVisibleItems = linearLayoutManager.findFirstVisibleItemPosition()
                        if (pastVisibleItems + visibleItemCount >= totalItemCount) {
                            EventBus.publish(if (dy > 0) FabEvent.OnFabHide else FabEvent.OnFabShowPos)
                        }
                    }
                })
    }

    private fun loadData() {
        val info: ClassesBaseInformation? = arguments?.getParcelable(ARG_INFO)
        info?.let { viewModel.loadData(it, subgroupNumber) }
    }

    private fun subscribeToLiveData() {
        viewModel.listItemsLiveData.observe(this, Observer {
            recyclerAdapter.setItems(it)
        })
    }

    companion object {
        private const val ARG_INFO = "arg_information"

        fun newInstance(info: ClassesBaseInformation) = ClassesItemFragment().apply {
            arguments = Bundle().apply {
                putParcelable(ARG_INFO, info)
            }
        }
    }
}