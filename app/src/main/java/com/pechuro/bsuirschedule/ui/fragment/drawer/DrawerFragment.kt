package com.pechuro.bsuirschedule.ui.fragment.drawer

import android.os.Bundle
import android.view.View
import androidx.databinding.library.baseAdapters.BR
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pechuro.bsuirschedule.R
import com.pechuro.bsuirschedule.databinding.FragmentDrawerBinding
import com.pechuro.bsuirschedule.ui.activity.settings.SettingsActivity
import com.pechuro.bsuirschedule.ui.base.BaseFragment
import com.pechuro.bsuirschedule.ui.fragment.drawer.adapter.NavItemAdapter
import com.pechuro.bsuirschedule.ui.utils.EventBus
import javax.inject.Inject

class DrawerFragment : BaseFragment<FragmentDrawerBinding, DrawerFragmentViewModel>() {
    @Inject
    lateinit var layoutManager: LinearLayoutManager
    @Inject
    lateinit var navAdapter: NavItemAdapter

    override val viewModel: DrawerFragmentViewModel
        get() = ViewModelProviders.of(this, viewModelFactory).get(DrawerFragmentViewModel::class.java)
    override val bindingVariables: Map<Int, Any>
        get() = mapOf(Pair(BR.viewModel, viewModel))
    override val layoutId: Int
        get() = R.layout.fragment_drawer

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
        subscribeToLiveData()
        setViewListeners()
    }

    private fun setupView() {
        layoutManager.orientation = RecyclerView.VERTICAL
        viewDataBinding.navItemList.layoutManager = layoutManager
        viewDataBinding.navItemList.adapter = navAdapter
    }

    private fun subscribeToLiveData() {
        viewModel.menuItems.observe(this, Observer {
            navAdapter.setItems(it)
        })
    }

    private fun setViewListeners() {
        viewDataBinding.navFooterSettings.setOnClickListener {
            context?.let { context ->
                val intent = SettingsActivity.newIntent(context)
                startActivity(intent)
            }
        }
        viewDataBinding.navFooterAddSchedule.setOnClickListener {
            EventBus.publish(DrawerEvent.OnOpenAddDialog)
        }
    }

    companion object {
        fun newInstance(): DrawerFragment {
            val args = Bundle()
            val fragment = DrawerFragment()
            fragment.arguments = args
            return fragment
        }
    }
}