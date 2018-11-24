package com.pechuro.bsuirschedule.ui.fragment.classes.classesitem

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pechuro.bsuirschedule.R
import com.pechuro.bsuirschedule.databinding.FragmentListBinding
import com.pechuro.bsuirschedule.ui.activity.navigation.FabEvent
import com.pechuro.bsuirschedule.ui.base.BaseFragment
import com.pechuro.bsuirschedule.ui.fragment.classes.classesinformation.ClassesBaseInformation
import com.pechuro.bsuirschedule.ui.fragment.classes.classesitem.adapter.ClassesAdapter
import com.pechuro.bsuirschedule.ui.fragment.itemoptions.ItemOptionsDialog
import com.pechuro.bsuirschedule.ui.utils.EventBus
import javax.inject.Inject

class ClassesItemFragment : BaseFragment<FragmentListBinding, ClassesItemViewModel>() {
    @Inject
    lateinit var layoutManager: LinearLayoutManager
    @Inject
    lateinit var adapter: ClassesAdapter

    override val viewModel: ClassesItemViewModel
        get() = ViewModelProviders.of(this, viewModelFactory).get(ClassesItemViewModel::class.java)
    override val bindingVariables: Map<Int, Any>?
        get() = null
    override val layoutId: Int
        get() = R.layout.fragment_list

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
        subscribeToLiveData()
        loadData()
        setListeners()
        setEventListeners()
    }

    private fun setEventListeners() {
        compositeDisposable.addAll(
                EventBus.listen(ClassesItemEvent::class.java).subscribe {
                    if (userVisibleHint) {
                        when (it) {
                            is ClassesItemEvent.OnItemLongClick -> ItemOptionsDialog.newInstance(it.id)
                                    .show(childFragmentManager, ItemOptionsDialog.TAG)
                        }
                    }
                }
        )
    }

    private fun setListeners() {
        viewDataBinding.recyclerView
                .addOnScrollListener(object : RecyclerView.OnScrollListener() {
                    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                        super.onScrolled(recyclerView, dx, dy)
                        val visibleItemCount = layoutManager.childCount
                        val totalItemCount = layoutManager.itemCount
                        val pastVisibleItems = layoutManager.findFirstVisibleItemPosition()
                        if (pastVisibleItems + visibleItemCount >= totalItemCount) {
                            EventBus.publish(if (dy > 0) FabEvent.OnFabHide else FabEvent.OnFabShowPos)
                        }
                    }
                })
    }

    private fun loadData() {
        val info: ClassesBaseInformation? = arguments?.getParcelable(ARG_INFO)
        info?.let { viewModel.loadData(it) }
    }

    private fun setupView() {
        layoutManager.orientation = RecyclerView.VERTICAL
        viewDataBinding.recyclerView.layoutManager = layoutManager
        viewDataBinding.recyclerView.adapter = adapter
    }

    private fun subscribeToLiveData() {
        viewModel.listItemsLiveData.observe(this, Observer {
            adapter.setItems(it)
        })
    }

    companion object {
        private const val ARG_INFO = "arg_information"

        fun newInstance(info: ClassesBaseInformation): ClassesItemFragment {
            val args = Bundle()
            args.putParcelable(ARG_INFO, info)

            val fragment = ClassesItemFragment()
            fragment.arguments = args
            return fragment
        }
    }
}