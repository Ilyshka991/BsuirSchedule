package com.pechuro.bsuirschedule.data.common

import com.pechuro.bsuirschedule.domain.exception.DataSourceException
import com.pechuro.bsuirschedule.remote.common.NetworkUnavailableException
import com.squareup.moshi.JsonDataException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException
import java.net.ConnectException
import java.net.UnknownHostException

abstract class BaseRepository {

    suspend inline fun <T> performApiCallCatching(
            defaultValue: T,
            crossinline call: suspend () -> T
    ) = try {
        performApiCall(call)
    } catch (e: Exception) {
        defaultValue
    }

    suspend inline fun <T> performApiCall(crossinline call: suspend () -> T) = try {
        withContext(Dispatchers.IO) {
            call()
        }
    } catch (e: Exception) {
        e.printStackTrace()
        val exception = when (e) {
            is ConnectException, is NetworkUnavailableException -> DataSourceException.NoDataSourceConnection
            is JsonDataException -> DataSourceException.InvalidData
            is UnknownHostException, is IOException, is HttpException -> DataSourceException.DataSourceUnavailable
            else -> DataSourceException.UnknownException
        }
        throw exception
    }

    suspend inline fun <T> performDaoCall(crossinline call: suspend () -> T) = try {
        withContext(Dispatchers.IO) {
            call()
        }
    } catch (e: Exception) {
        throw DataSourceException.UnknownException
    }
}