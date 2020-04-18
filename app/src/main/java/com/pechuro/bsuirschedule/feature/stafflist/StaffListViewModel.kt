package com.pechuro.bsuirschedule.feature.stafflist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.pechuro.bsuirschedule.common.base.BaseViewModel
import com.pechuro.bsuirschedule.domain.common.BaseInteractor
import com.pechuro.bsuirschedule.domain.common.getOrDefault
import com.pechuro.bsuirschedule.domain.interactor.GetEmployees
import com.pechuro.bsuirschedule.domain.interactor.GetGroups
import com.pechuro.bsuirschedule.ext.flowLiveData
import com.pechuro.bsuirschedule.feature.stafflist.StaffItemInformation.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.emptyFlow
import javax.inject.Inject

class StaffListViewModel @Inject constructor(
        private val getGroups: GetGroups,
        private val getEmployees: GetEmployees
) : BaseViewModel() {

    private val allGroupsFilter = MutableLiveData("")
    private val allGroupsList = flowLiveData {
        getGroups.execute(BaseInteractor.NoParams).getOrDefault(emptyFlow())
    }
    val allGroupsData: LiveData<List<StaffItemInformation>> = MediatorLiveData<List<StaffItemInformation>>().apply {
        addSource(allGroupsFilter) {
            launchCoroutine(context = Dispatchers.IO) {
                val resultList = getGroupsResultList()
                postValue(resultList)
            }
        }
        addSource(this@StaffListViewModel.allGroupsList) {
            launchCoroutine(context = Dispatchers.IO) {
                val resultList = getGroupsResultList()
                postValue(resultList)
            }
        }
    }

    private val allEmployeesFilter = MutableLiveData("")
    private val allEmployeesList = flowLiveData {
        getEmployees.execute(BaseInteractor.NoParams).getOrDefault(emptyFlow())
    }
    val allEmployeesData: LiveData<List<StaffItemInformation>> = MediatorLiveData<List<StaffItemInformation>>().apply {
        addSource(allEmployeesFilter) {
            launchCoroutine(context = Dispatchers.IO) {
                val resultList = getEmployeeResultList()
                postValue(resultList)
            }
        }
        addSource(allEmployeesList) {
            launchCoroutine(context = Dispatchers.IO) {
                val resultList = getEmployeeResultList()
                postValue(resultList)
            }
        }
    }

    fun filterGroups(name: String) {
        allGroupsFilter.value = name
    }

    fun filterEmployees(abbreviation: String) {
        allEmployeesFilter.value = abbreviation
    }

    private fun getEmployeeResultList(): List<StaffItemInformation> {
        val filter = allEmployeesFilter.value ?: ""
        val currentList = allEmployeesList.value ?: emptyList()
        val filteredList = currentList.filter {
            it.abbreviation.startsWith(filter, ignoreCase = true)
        }.map { EmployeeInfo(it) }
        return if (filteredList.isEmpty() && filter.isNotEmpty()) {
            filteredList.plus(Empty)
        } else {
            filteredList
        }
    }

    private fun getGroupsResultList(): List<StaffItemInformation> {
        val filter = allGroupsFilter.value ?: ""
        val currentList = allGroupsList.value ?: emptyList()
        val filteredList = currentList.filter {
            it.number.startsWith(filter)
        }.map { GroupInfo(it) }
        return if (filteredList.isEmpty() && filter.isNotEmpty()) {
            filteredList.plus(Empty)
        } else {
            filteredList
        }
    }
}