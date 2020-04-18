package com.pechuro.bsuirschedule.ext

import android.os.Bundle
import android.os.Parcelable
import java.io.Serializable

inline fun <reified T : Parcelable> Bundle.parcelableOrException(key: String) =
        getParcelable<T>(key) ?: throw IllegalArgumentException("'parcelable' data must be set!")

inline fun <reified T : Serializable> Bundle.serializableOrException(key: String) =
        getSerializable(key) as? T
                ?: throw IllegalArgumentException("'parcelable' data must be set!")

inline fun <reified T : Parcelable> Bundle.parcelableArrayOrException(key: String): Array<T> =
        getParcelableArray(key) as? Array<T>
                ?: throw IllegalArgumentException("'parcelable' data must be set!")
