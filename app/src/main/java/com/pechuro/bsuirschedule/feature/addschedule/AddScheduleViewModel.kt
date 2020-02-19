package com.pechuro.bsuirschedule.feature.addschedule

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.pechuro.bsuirschedule.common.base.BaseViewModel
import com.pechuro.bsuirschedule.domain.common.BaseInteractor
import com.pechuro.bsuirschedule.domain.common.fold
import com.pechuro.bsuirschedule.domain.common.getOrDefault
import com.pechuro.bsuirschedule.domain.entity.Employee
import com.pechuro.bsuirschedule.domain.entity.Group
import com.pechuro.bsuirschedule.domain.entity.ScheduleType
import com.pechuro.bsuirschedule.domain.interactor.GetEmployees
import com.pechuro.bsuirschedule.domain.interactor.GetGroups
import com.pechuro.bsuirschedule.domain.interactor.LoadEmployeeSchedule
import com.pechuro.bsuirschedule.domain.interactor.LoadGroupSchedule
import com.pechuro.bsuirschedule.ext.flowLiveData
import com.pechuro.bsuirschedule.feature.addschedule.fragment.SuggestionItemInformation
import com.pechuro.bsuirschedule.feature.addschedule.fragment.SuggestionItemInformation.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AddScheduleViewModel @Inject constructor(
        private val getGroups: GetGroups,
        private val getEmployees: GetEmployees,
        private val loadEmployeeSchedule: LoadEmployeeSchedule,
        private val loadGroupSchedule: LoadGroupSchedule
) : BaseViewModel() {

    val state = MutableLiveData<State>(State.Idle)

    private val allGroupsFilter = MutableLiveData<String>("")
    private val allGroupsList = flowLiveData {
        getGroups.execute(BaseInteractor.NoParams).getOrDefault(emptyFlow())
    }
    val allGroupsData: LiveData<List<SuggestionItemInformation>> = MediatorLiveData<List<SuggestionItemInformation>>().apply {
        addSource(allGroupsFilter) { number ->
            launchCoroutine {
                val resultList = withContext(Dispatchers.IO) {
                    val currentList = allGroupsList.value ?: emptyList()
                    val filteredList = currentList.filter {
                        it.number.startsWith(number)
                    }.map { GroupInfo(it) }
                    if (filteredList.isEmpty() && number.isNotEmpty()) {
                        filteredList.plusElement(Empty)
                    } else {
                        filteredList
                    }
                }
                value = resultList
            }
        }
        addSource(this@AddScheduleViewModel.allGroupsList) { newList ->
            launchCoroutine {
                val resultList = withContext(Dispatchers.IO) {
                    val filterNumber = allGroupsFilter.value ?: ""
                    val filteredList = newList.filter {
                        it.number.startsWith(filterNumber)
                    }.map { GroupInfo(it) }
                    if (filteredList.isEmpty()) filteredList.plusElement(Empty) else filteredList
                }
                value = resultList
            }
        }
    }

    private val allEmployeesFilter = MutableLiveData<String>("")
    private val allEmployeesList = flowLiveData {
        getEmployees.execute(BaseInteractor.NoParams).getOrDefault(emptyFlow())
    }
    val allEmployeesData: LiveData<List<SuggestionItemInformation>> = MediatorLiveData<List<SuggestionItemInformation>>().apply {
        addSource(allEmployeesFilter) { abbreviation ->
            launchCoroutine {
                val resultList = withContext(Dispatchers.IO) {
                    val currentList = allEmployeesList.value ?: emptyList()
                    val filteredList = currentList.filter {
                        it.abbreviation.startsWith(abbreviation, ignoreCase = true)
                    }.map { EmployeeInfo(it) }
                    if (filteredList.isEmpty() && abbreviation.isNotEmpty()) {
                        filteredList.plusElement(Empty)
                    } else {
                        filteredList
                    }
                }
                value = resultList
            }
        }
        addSource(allEmployeesList) { newList ->
            launchCoroutine {
                val resultList = withContext(Dispatchers.IO) {
                    val filterNumber = allEmployeesFilter.value ?: ""
                    val filteredList = newList.filter {
                        it.abbreviation.startsWith(filterNumber, ignoreCase = true)
                    }.map { EmployeeInfo(it) }
                    if (filteredList.isEmpty()) filteredList.plusElement(Empty) else filteredList
                }
                value = resultList
            }
        }
    }

    fun filterGroups(name: String) {
        allGroupsFilter.value = name
    }

    fun filterEmployees(abbreviation: String) {
        allEmployeesFilter.value = abbreviation
    }

    fun loadSchedule(group: Group, types: List<ScheduleType>) {
        if (types.isEmpty()) return
        state.value = State.Loading
        launchCoroutine {
            loadGroupSchedule.execute(LoadGroupSchedule.Params(group, types)).fold(
                    onSuccess = {
                        state.value = State.Complete
                    },
                    onFailure = {
                        state.value = State.Error
                    }
            )
        }
    }

    fun loadSchedule(employee: Employee, types: List<ScheduleType>) {
        if (types.isEmpty()) return
        state.value = State.Loading
        launchCoroutine {
            loadEmployeeSchedule.execute(LoadEmployeeSchedule.Params(employee, types)).fold(
                    onSuccess = {
                        state.value = State.Complete
                    },
                    onFailure = {
                        state.value = State.Error
                    }
            )
        }
    }

    fun complete() {
        state.value = State.Complete
    }

    fun cancel() {
        state.value = State.Idle
    }

    sealed class State {
        object Idle : State()
        object Loading : State()
        object Error : State()
        object Complete : State()
    }
}