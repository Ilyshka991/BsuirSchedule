package com.pechuro.bsuirschedule.feature.stafflist

import android.content.Context
import android.os.Bundle
import android.text.InputType
import android.view.View
import android.view.WindowManager
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bsuir.pechuro.bsuirschedule.R
import com.pechuro.bsuirschedule.common.base.BaseFragment
import com.pechuro.bsuirschedule.ext.*
import kotlinx.android.synthetic.main.fragment_staff_list.*

class StaffListFragment : BaseFragment() {

    interface ActionCallback {

        fun onStaffListItemSelected(info: StaffItemInformation)
    }

    companion object {
        const val TAG = "StaffListFragment"

        private const val BUNDLE_TYPE = "BUNDLE_TYPE"

        fun newInstance(type: StaffType) = StaffListFragment().apply {
            arguments = bundleOf(BUNDLE_TYPE to type)
        }
    }

    override val layoutId: Int = R.layout.fragment_staff_list

    private val viewModel by lazy(LazyThreadSafetyMode.NONE) {
        initViewModel(StaffListViewModel::class)
    }

    private val staffAdapter by lazy(LazyThreadSafetyMode.NONE) {
        StaffAdapter(onItemClicked = {
            actionCallback?.onStaffListItemSelected(it)
        }).also {
            it.setHasStableIds(true)
        }
    }

    private val staffType: StaffType by args(BUNDLE_TYPE)

    private var actionCallback: ActionCallback? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        actionCallback = getCallbackOrNull()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.init(staffType)
        initView()
        observeData()
    }

    override fun onStart() {
        super.onStart()
        context?.showKeyboard(staffListNameInput)
    }

    override fun onStop() {
        super.onStop()
        context?.hideKeyboard(staffListNameInput.windowToken)
    }

    override fun onDestroyView() {
        staffListRecyclerView.clearAdapter()
        super.onDestroyView()
    }

    override fun onDetach() {
        super.onDetach()
        activity?.window?.clearFlags(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        actionCallback = null
    }

    private fun initView() {
        val inputType = when (staffType) {
            StaffType.GROUP, StaffType.AUDITORY -> InputType.TYPE_CLASS_NUMBER
            StaffType.EMPLOYEE -> InputType.TYPE_TEXT_VARIATION_PERSON_NAME
        }
        staffListNameInput.inputType = inputType
        staffListNameInput.addTextListener {
            viewModel.filter(it)
        }

        val hintRes = when (staffType) {
            StaffType.GROUP -> R.string.staff_list_hint_enter_group_number
            StaffType.EMPLOYEE -> R.string.staff_list_hint_enter_employee_name
            StaffType.AUDITORY -> R.string.staff_list_hint_enter_auditory
        }
        staffListNameInputLayout.hint = getString(hintRes)

        staffListRecyclerView.apply {
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            adapter = staffAdapter
            setHasFixedSize(true)
        }
    }

    private fun observeData() {
        viewModel.listData.nonNull().observe(viewLifecycleOwner) {
            staffAdapter.submitList(it)
        }
    }
}