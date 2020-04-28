package com.pechuro.bsuirschedule.common

data class CheckableData<T : Any>(
        val data: T,
        val checked: Boolean
)