package com.pechuro.bsuirschedule.domain.ext

import java.util.ArrayDeque
import java.util.Queue

fun <T> emptyQueue(): Queue<T> = ArrayDeque()

fun <T> Array<T>.toQueue(): Queue<T> = ArrayDeque<T>().apply {
    addAll(this)
}

fun <T> List<T>.addIfEmpty(item: T) = if (isEmpty()) listOf(item) else this

fun <T> List<T>.addIfEmpty(block: () -> T) = if (isEmpty()) listOf(block()) else this

fun <T> Set<T>.addIfEmpty(block: () -> T) = if (isEmpty()) setOf(block()) else this