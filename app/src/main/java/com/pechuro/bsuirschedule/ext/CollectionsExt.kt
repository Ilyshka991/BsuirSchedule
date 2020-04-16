package com.pechuro.bsuirschedule.ext

import java.util.*

fun <T> emptyQueue(): Queue<T> = ArrayDeque()

fun <T> Array<T>.toQueue(): Queue<T> = ArrayDeque<T>().apply {
    addAll(this)
}

fun <T> List<T>.addIfEmpty(item: T) = if (isEmpty()) listOf(item) else this

fun <T> List<T>.addIfEmpty(block: () -> T) = if (isEmpty()) listOf(block()) else this