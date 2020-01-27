package com.pechuro.bsuirschedule.data.common

import com.pechuro.bsuirschedule.domain.common.Logger
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

    protected suspend inline fun <T> performApiCallCatching(
            defaultValue: T,
            crossinline call: suspend () -> T
    ) = try {
        performApiCall(call)
    } catch (e: Exception) {
        defaultValue
    }

    protected suspend inline fun <T> performApiCall(crossinline call: suspend () -> T) = try {
        withContext(Dispatchers.IO) {
            call()
        }
    } catch (e: Exception) {
        Logger.e(e)
        val exception = when (e) {
            is ConnectException, is NetworkUnavailableException -> DataSourceException.NoDataSourceConnection
            is JsonDataException, is IOException -> DataSourceException.InvalidData
            is UnknownHostException, is HttpException -> DataSourceException.DataSourceUnavailable
            else -> DataSourceException.UnknownException
        }
        throw exception
    }

    protected suspend inline fun <T> performDaoCall(crossinline call: suspend () -> T) = try {
        withContext(Dispatchers.IO) {
            call()
        }
    } catch (e: Exception) {
        Logger.e(e)
        throw DataSourceException.UnknownException
    }
}