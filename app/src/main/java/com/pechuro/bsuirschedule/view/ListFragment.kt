package com.pechuro.bsuirschedule.view

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.pechuro.bsuirschedule.App
import com.pechuro.bsuirschedule.R
import com.pechuro.bsuirschedule.repository.entity.Employee
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


class ListFragment : BaseFragment() {

    private val groupRepository = App.injectGroupRepository()
    private var mRecyclerView: RecyclerView? = null
    private var mAdapter: RecyclerViewAdapter? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.list_fragment, container, false)
        mRecyclerView = view.findViewById(R.id.recyclerView)

        val linearLayoutManager = LinearLayoutManager(context)
        linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
        mRecyclerView?.layoutManager = linearLayoutManager

        mAdapter = RecyclerViewAdapter(arrayListOf(), context!!)
        mRecyclerView?.adapter = mAdapter
        return view
    }

    override fun onStart() {
        super.onStart()
        subscribe(groupRepository.getEmployees()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    println(it)
                    showData(it)
                }, { print("pzdc") }))
    }

    private fun showData(data: List<Employee>) = mAdapter?.setItems(data)
}
