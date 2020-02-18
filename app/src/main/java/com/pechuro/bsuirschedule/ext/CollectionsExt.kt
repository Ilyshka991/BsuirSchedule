package com.pechuro.bsuirschedule.ext

import java.util.*

fun <T> emptyQueue(): Queue<T> = ArrayDeque()

fun <T> Array<T>.toQueue(): Queue<T> = ArrayDeque<T>().apply {
    addAll(this)
}