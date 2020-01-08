package com.pechuro.bsuirschedule.feature.main.start

import android.os.Bundle
import com.pechuro.bsuirschedule.R
import com.pechuro.bsuirschedule.common.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_start.*

class StartFragment : BaseFragment() {

    companion object {

        fun newInstance() = StartFragment()
    }

    interface Callback {

        fun addSchedule()
    }

    override val layoutId: Int
        get() = R.layout.fragment_start

    var actionCallback: Callback? = null

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initView()
    }

    private fun initView() {
        startFragmentAddFab.setOnClickListener {
            actionCallback?.addSchedule()
        }
    }
}