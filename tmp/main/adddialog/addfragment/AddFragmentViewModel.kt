package com.pechuro.bsuirschedule.feature.main.adddialog.addfragment

import androidx.databinding.ObservableBoolean
import androidx.lifecycle.MutableLiveData
import com.pechuro.bsuirschedule.R
import com.pechuro.bsuirschedule.toDelete.ScheduleTypes.EMPLOYEE
import com.pechuro.bsuirschedule.toDelete.ScheduleTypes.STUDENT
import com.pechuro.bsuirschedule.toDelete.EmployeeRepository
import com.pechuro.bsuirschedule.toDelete.GroupRepository
import com.pechuro.bsuirschedule.toDelete.ScheduleRepository
import com.pechuro.bsuirschedule.common.BaseViewModel
import com.pechuro.bsuirschedule.feature.main.ScheduleInformation
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class AddFragmentViewModel @Inject constructor(private val repository: ScheduleRepository,
                                               private val groupRepository: GroupRepository,
                                               private val employeeRepository: EmployeeRepository) : BaseViewModel() {
    val isLoading = ObservableBoolean()
    val isError = ObservableBoolean()
    val suggestions = MutableLiveData<List<String>>()
    val status = SingleLiveEvent<Status>()

    fun loadSuggestions(scheduleType: Int) {
        val source = when (scheduleType) {
            STUDENT -> groupRepository.getGroupNumbers()
            EMPLOYEE -> employeeRepository.getNames()
            else -> throw IllegalArgumentException()
        }

        source
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    suggestions.value = it
                }, {
                    status.call(Status.OnError(R.string.add_fragment_error_empty_suggestions))
                })
                .let(compositeDisposable::add)
    }

    fun cancel() {
        isError.set(false)
        status.call(Status.OnCancel)
    }

    fun loadSchedule(scheduleName: String, scheduleTypes: List<Int>) {
        if (!isValid(scheduleName, scheduleTypes)) {
            return
        }

        status.call(Status.OnLoading)

        isLoading.set(true)
        isError.set(false)

        repository.loadClasses(scheduleName, scheduleTypes)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    if (it.size > 0) {
                        val info = with(it[0]) {
                            ScheduleInformation(id, name, type)
                        }
                        status.call(Status.OnSuccess(info))
                    } else {
                        isError.set(true)
                    }
                    isLoading.set(false)
                }, {
                    isError.set(true)
                    isLoading.set(false)
                })
                .let(compositeDisposable::add)
    }

    private fun isValid(scheduleName: String, scheduleTypes: List<Int>): Boolean {
        if (scheduleTypes.isEmpty()) {
            status.call(Status.OnError(R.string.add_fragment_error_types_not_specified))
            return false
        }
        if (scheduleName.isBlank()) {
            status.call(Status.OnError(R.string.add_fragment_error_blank_schedule_name))
            return false
        }
        if (suggestions.value == null) {
            status.call(Status.OnError(R.string.add_fragment_error_empty_suggestions))
            return false
        }
        if (!suggestions.value!!.contains(scheduleName)) {
            status.call(Status.OnError(R.string.add_fragment_error_unknown_schedule))
            return false
        }
        status.call(Status.OnClearError)
        return true
    }
}

sealed class Status {
    class OnError(val messageId: Int) : Status()
    object OnClearError : Status()
    class OnSuccess(val info: ScheduleInformation) : Status()
    object OnLoading : Status()
    object OnCancel : Status()
}
