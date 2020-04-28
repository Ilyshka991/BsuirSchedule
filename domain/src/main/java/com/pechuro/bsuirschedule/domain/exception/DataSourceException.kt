package com.pechuro.bsuirschedule.domain.exception

sealed class DataSourceException : Exception() {

    object DataSourceUnavailable : DataSourceException()

    object NoDataSourceConnection : DataSourceException()

    object InvalidData : DataSourceException()

    object UnknownException : DataSourceException()

    object CancellationException : DataSourceException()
}