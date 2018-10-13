package com.pechuro.bsuirschedule.ui.fragment.adddialog.addfragment

import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import com.pechuro.bsuirschedule.R
import com.pechuro.bsuirschedule.constant.ScheduleTypes.EMPLOYEE
import com.pechuro.bsuirschedule.constant.ScheduleTypes.STUDENT
import com.pechuro.bsuirschedule.data.EmployeeRepository
import com.pechuro.bsuirschedule.data.GroupRepository
import com.pechuro.bsuirschedule.data.ScheduleRepository
import com.pechuro.bsuirschedule.ui.base.BaseViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class AddFragmentViewModel @Inject constructor(private val repository: ScheduleRepository,
                                               private val groupRepository: GroupRepository,
                                               private val employeeRepository: EmployeeRepository) : BaseViewModel() {
    val isLoading = ObservableField(false)
    val isError = ObservableField(false)
    val suggestions = MutableLiveData<List<String>>()
    lateinit var navigator: AddFragmentNavigator

    fun loadSuggestions(scheduleType: Int) {
        val observable = when (scheduleType) {
            STUDENT -> groupRepository.getGroupNumbers()
            EMPLOYEE -> employeeRepository.getNames()
            else -> throw IllegalArgumentException()
        }

        compositeDisposable.add(
                observable.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({
                            suggestions.value = it
                        }, {
                            navigator.onError(R.string.error_empty_suggestions)
                        }))
    }

    fun onCancelClick() {
        isError.set(false)
        navigator.onCancel()
    }

    fun loadSchedule(scheduleName: String, scheduleTypes: List<Int>) {
        navigator.onLoading()

        isLoading.set(true)
        isError.set(false)

        if (!isValid(scheduleName, scheduleTypes)) {
            isLoading.set(false)
            return
        }

        compositeDisposable.add(repository.loadClasses(scheduleName, scheduleTypes)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    navigator.onSuccess(scheduleName, scheduleTypes[0])
                    isLoading.set(false)
                }, {
                    isError.set(true)
                    isLoading.set(false)
                })
        )
    }

    private fun isValid(scheduleName: String, scheduleTypes: List<Int>): Boolean {
        if (scheduleTypes.isEmpty()) {
            navigator.onError(R.string.error_types_not_specified)
            return false
        }
        if (scheduleName.isBlank()) {
            navigator.onError(R.string.error_blank_schedule_name)
            return false
        }
        if (suggestions.value == null) {
            navigator.onError(R.string.error_empty_suggestions)
            return false
        }
        if (!suggestions.value!!.contains(scheduleName)) {
            navigator.onError(R.string.error_unknown_schedule)
            return false
        }
        navigator.onClearError()
        return true
    }
}