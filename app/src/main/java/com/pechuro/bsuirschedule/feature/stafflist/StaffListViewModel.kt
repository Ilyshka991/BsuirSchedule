package com.pechuro.bsuirschedule.feature.stafflist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.pechuro.bsuirschedule.common.base.BaseViewModel
import com.pechuro.bsuirschedule.domain.common.BaseInteractor
import com.pechuro.bsuirschedule.domain.common.getOrDefault
import com.pechuro.bsuirschedule.domain.entity.Auditory
import com.pechuro.bsuirschedule.domain.entity.Group
import com.pechuro.bsuirschedule.domain.interactor.GetAuditories
import com.pechuro.bsuirschedule.domain.interactor.GetEmployees
import com.pechuro.bsuirschedule.domain.interactor.GetGroups
import com.pechuro.bsuirschedule.ext.flowLiveData
import com.pechuro.bsuirschedule.feature.stafflist.StaffItemInformation.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class StaffListViewModel @Inject constructor(
        private val getGroups: GetGroups,
        private val getEmployees: GetEmployees,
        private val getAuditories: GetAuditories
) : BaseViewModel() {

    private val filterData = MutableLiveData("")

    private val allGroupsListData = flowLiveData {
        getGroups.execute(BaseInteractor.NoParams).getOrDefault(emptyFlow())
                .map { list -> list.sortedWith(compareBy<Group> { it.faculty.abbreviation }.thenBy { it.number }) }
    }
    private val allAuditoriesListData = flowLiveData {
        getAuditories.execute(BaseInteractor.NoParams).getOrDefault(emptyFlow())
                .map { list -> list.sortedWith(compareBy<Auditory> { it.building.name }.thenBy { it.name }) }
    }
    private val allEmployeesListData = flowLiveData {
        getEmployees.execute(BaseInteractor.NoParams).getOrDefault(emptyFlow())
                .map { list -> list.sortedBy { it.abbreviation } }
    }

    private val _listData = MediatorLiveData<List<StaffItemInformation>>()
    val listData: LiveData<List<StaffItemInformation>>
        get() = _listData

    private lateinit var type: StaffType

    fun init(type: StaffType) {
        if (this::type.isInitialized) return
        this.type = type
        val source = when (type) {
            StaffType.AUDITORY -> allAuditoriesListData
            StaffType.GROUP -> allGroupsListData
            StaffType.EMPLOYEE -> allEmployeesListData
        }
        _listData.apply {
            addSource(filterData) {
                launchCoroutine(context = Dispatchers.IO) {
                    val resultList = getResultList()
                    postValue(resultList)
                }
            }
            addSource(source) {
                launchCoroutine(context = Dispatchers.IO) {
                    val resultList = getResultList()
                    postValue(resultList)
                }
            }
        }
    }

    fun filter(filter: String) {
        filterData.value = filter
    }

    private fun getResultList(): List<StaffItemInformation> {
        val filter = filterData.value ?: ""
        val filteredList = when (type) {
            StaffType.AUDITORY -> {
                val currentList = allAuditoriesListData.value ?: emptyList()
                currentList.filter {
                    it.name.startsWith(filter)
                }.map { AuditoryInfo(it) }
            }
            StaffType.GROUP -> {
                val currentList = allGroupsListData.value ?: emptyList()
                currentList.filter {
                    it.number.startsWith(filter)
                }.map { GroupInfo(it) }
            }
            StaffType.EMPLOYEE -> {
                val currentList = allEmployeesListData.value ?: emptyList()
                currentList.filter {
                    it.firstName.startsWith(filter, ignoreCase = true)
                            || it.lastName.startsWith(filter, ignoreCase = true)
                }.map { EmployeeInfo(it) }
            }
        }
        return if (filteredList.isEmpty() && filter.isNotEmpty()) {
            filteredList.plus(Empty)
        } else {
            filteredList
        }
    }
}