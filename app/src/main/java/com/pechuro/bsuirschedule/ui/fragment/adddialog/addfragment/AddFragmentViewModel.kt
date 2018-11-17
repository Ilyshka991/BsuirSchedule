package com.pechuro.bsuirschedule.ui.fragment.adddialog.addfragment

import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import com.pechuro.bsuirschedule.R
import com.pechuro.bsuirschedule.constants.ScheduleTypes.EMPLOYEE
import com.pechuro.bsuirschedule.constants.ScheduleTypes.STUDENT
import com.pechuro.bsuirschedule.data.EmployeeRepository
import com.pechuro.bsuirschedule.data.GroupRepository
import com.pechuro.bsuirschedule.data.ScheduleRepository
import com.pechuro.bsuirschedule.ui.base.BaseViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class AddFragmentViewModel @Inject constructor(private val repository: ScheduleRepository,
                                               private val groupRepository: GroupRepository,
                                               private val employeeRepository: EmployeeRepository) : BaseViewModel<AddFragmentNavigator>() {
    val isLoading = ObservableBoolean(false)
    val isError = ObservableBoolean(false)
    val suggestions = MutableLiveData<List<String>>()

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
                            getNavigator()?.onError(R.string.error_empty_suggestions)
                        }))
    }

    fun onCancelClick() {
        isError.set(false)
        getNavigator()?.onCancel()
    }

    fun loadSchedule(scheduleName: String, scheduleTypes: List<Int>) {
        getNavigator()?.onLoading()

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
                    getNavigator()?.onSuccess(scheduleName, scheduleTypes[0])
                    isLoading.set(false)
                }, {
                    isError.set(true)
                    isLoading.set(false)
                })
        )
    }

    private fun isValid(scheduleName: String, scheduleTypes: List<Int>): Boolean {
        if (scheduleTypes.isEmpty()) {
            getNavigator()?.onError(R.string.error_types_not_specified)
            return false
        }
        if (scheduleName.isBlank()) {
            getNavigator()?.onError(R.string.error_blank_schedule_name)
            return false
        }
        if (suggestions.value == null) {
            getNavigator()?.onError(R.string.error_empty_suggestions)
            return false
        }
        if (!suggestions.value!!.contains(scheduleName)) {
            getNavigator()?.onError(R.string.error_unknown_schedule)
            return false
        }
        getNavigator()?.onClearError()
        return true
    }
}