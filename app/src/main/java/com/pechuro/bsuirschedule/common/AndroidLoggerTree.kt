package com.pechuro.bsuirschedule.common

import android.os.Build
import android.util.Log
import com.pechuro.bsuirschedule.domain.common.Logger
import com.pechuro.bsuirschedule.domain.common.Logger.LogPriority
import java.util.regex.Pattern
import kotlin.math.min

class AndroidLoggerTree : Logger.Tree() {

    companion object {
        private const val MAX_LOG_LENGTH = 4000
        private const val MAX_TAG_LENGTH = 23
        private const val CALL_STACK_INDEX = 5
        private val ANONYMOUS_CLASS = Pattern.compile("(\\$\\d+)+$")
    }

    override fun getTag(): String? {
        super.getTag()?.let {
            return it
        }
        val stackTrace = Throwable().stackTrace
        check(stackTrace.size > CALL_STACK_INDEX)
        return createStackElementTag(stackTrace[CALL_STACK_INDEX])
    }

    override fun log(priority: LogPriority, tag: String?, message: String, t: Throwable?) {
        val androidLogPriority = getAndroidLogPriority(priority)

        // Split by line, then ensure each line can fit into Log's maximum length.
        var i = 0
        val length = message.length
        while (i < length) {
            var newline = message.indexOf('\n', i)
            newline = if (newline != -1) newline else length
            do {
                val end = min(newline, i + MAX_LOG_LENGTH)
                val part = message.substring(i, end)
                Log.println(androidLogPriority, tag, part)
                i = end
            } while (i < newline)
            i++
        }
    }

    private fun getAndroidLogPriority(priority: LogPriority) = when (priority) {
        LogPriority.DEBUG -> Log.DEBUG
        LogPriority.ASSERT -> Log.ASSERT
        LogPriority.ERROR -> Log.ERROR
        LogPriority.VERBOSE -> Log.VERBOSE
        LogPriority.INFO -> Log.INFO
        LogPriority.WARN -> Log.WARN
    }

    private fun createStackElementTag(element: StackTraceElement): String? {
        var tag = element.className
        val m = ANONYMOUS_CLASS.matcher(tag)
        if (m.find()) {
            tag = m.replaceAll("")
        }
        tag = tag.substring(tag.lastIndexOf('.') + 1)
        return if (tag.length <= MAX_TAG_LENGTH || Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            tag
        } else {
            tag.substring(0, MAX_TAG_LENGTH)
        }
    }
}