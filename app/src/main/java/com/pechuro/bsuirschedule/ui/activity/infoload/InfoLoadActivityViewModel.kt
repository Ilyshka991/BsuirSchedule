package com.pechuro.bsuirschedule.ui.activity.infoload

import androidx.databinding.ObservableField
import com.pechuro.bsuirschedule.data.EmployeeRepository
import com.pechuro.bsuirschedule.data.GroupRepository
import com.pechuro.bsuirschedule.ui.base.BaseViewModel
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class InfoLoadActivityViewModel @Inject constructor(
        private val groupRepository: GroupRepository,
        private val employeeRepository: EmployeeRepository) : BaseViewModel() {

    val isLoading = ObservableField<Boolean>()
    lateinit var navigator: InfoLoadNavigator

    init {
        loadInfo()
    }

    fun onRetryClick() {
        loadInfo()
    }

    private fun loadInfo() {
        isLoading.set(true)
        compositeDisposable.add(
                Single.merge(groupRepository.load(), employeeRepository.load())
                        .toList()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({
                            navigator.onSuccess()
                        }, {
                            isLoading.set(false)
                        })
        )
    }
}