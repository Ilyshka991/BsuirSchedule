package com.pechuro.bsuirschedule.domain.common

import java.io.PrintWriter
import java.io.StringWriter

object Logger {

    @Volatile
    private var trees = emptyArray<Tree>()

    private val ROOT_TREE: Tree = object : Tree() {

        override fun v(message: String?, vararg args: Any) {
            for (tree in trees) {
                tree.v(message, *args)
            }
        }

        override fun v(t: Throwable?, message: String?, vararg args: Any) {
            for (tree in trees) {
                tree.v(t, message, *args)
            }
        }

        override fun v(t: Throwable?) {
            for (tree in trees) {
                tree.v(t)
            }
        }

        override fun d(message: String?, vararg args: Any) {
            for (tree in trees) {
                tree.d(message, *args)
            }
        }

        override fun d(t: Throwable?, message: String?, vararg args: Any) {
            for (tree in trees) {
                tree.d(t, message, *args)
            }
        }

        override fun d(t: Throwable?) {
            for (tree in trees) {
                tree.d(t)
            }
        }

        override fun i(message: String?, vararg args: Any) {
            for (tree in trees) {
                tree.i(message, *args)
            }
        }

        override fun i(t: Throwable?, message: String?, vararg args: Any) {
            for (tree in trees) {
                tree.i(t, message, *args)
            }
        }

        override fun i(t: Throwable?) {
            for (tree in trees) {
                tree.i(t)
            }
        }

        override fun w(message: String?, vararg args: Any) {
            for (tree in trees) {
                tree.w(message, *args)
            }
        }

        override fun w(t: Throwable?, message: String?, vararg args: Any) {
            for (tree in trees) {
                tree.w(t, message, *args)
            }
        }

        override fun w(t: Throwable?) {
            for (tree in trees) {
                tree.w(t)
            }
        }

        override fun e(message: String?, vararg args: Any) {
            for (tree in trees) {
                tree.e(message, *args)
            }
        }

        override fun e(t: Throwable?, message: String?, vararg args: Any) {
            for (tree in trees) {
                tree.e(t, message, *args)
            }
        }

        override fun e(t: Throwable?) {
            for (tree in trees) {
                tree.e(t)
            }
        }

        override fun wtf(message: String?, vararg args: Any) {
            for (tree in trees) {
                tree.wtf(message, *args)
            }
        }

        override fun wtf(t: Throwable?, message: String?, vararg args: Any) {
            for (tree in trees) {
                tree.wtf(t, message, *args)
            }
        }

        override fun wtf(t: Throwable?) {
            for (tree in trees) {
                tree.wtf(t)
            }
        }

        override fun log(priority: LogPriority, tag: String?, message: String, t: Throwable?) {
            throw AssertionError("Missing override for log method.")
        }
    }

    fun tag(tag: String): Tree {
        val forest = trees
        for (tree in forest) {
            tree.explicitTag.set(tag)
        }
        return ROOT_TREE
    }

    fun add(tree: Tree) {
        require(!(tree === ROOT_TREE)) { "Cannot add tree into itself." }
        trees += tree
    }

    fun v(message: String?, vararg args: Any) {
        ROOT_TREE.v(message, *args)
    }

    fun v(t: Throwable?, message: String?, vararg args: Any) {
        ROOT_TREE.v(t, message, *args)
    }

    fun v(t: Throwable?) {
        ROOT_TREE.v(t)
    }

    fun d(message: String?, vararg args: Any) {
        ROOT_TREE.d(message, *args)
    }

    fun d(t: Throwable?, message: String?, vararg args: Any) {
        ROOT_TREE.d(t, message, *args)
    }

    fun d(t: Throwable?) {
        ROOT_TREE.d(t)
    }

    fun i(message: String?, vararg args: Any) {
        ROOT_TREE.i(message, *args)
    }

    fun i(t: Throwable?, message: String?, vararg args: Any) {
        ROOT_TREE.i(t, message, *args)
    }

    fun i(t: Throwable?) {
        ROOT_TREE.i(t)
    }

    fun w(message: String?, vararg args: Any) {
        ROOT_TREE.w(message, *args)
    }

    fun w(t: Throwable?, message: String?, vararg args: Any) {
        ROOT_TREE.w(t, message, *args)
    }

    fun w(t: Throwable?) {
        ROOT_TREE.w(t)
    }

    fun e(message: String?, vararg args: Any) {
        ROOT_TREE.e(message, *args)
    }

    fun e(t: Throwable?, message: String?, vararg args: Any) {
        ROOT_TREE.e(t, message, *args)
    }

    fun e(t: Throwable?) {
        ROOT_TREE.e(t)
    }

    fun wtf(message: String?, vararg args: Any) {
        ROOT_TREE.wtf(message, *args)
    }

    fun wtf(t: Throwable?, message: String?, vararg args: Any) {
        ROOT_TREE.wtf(t, message, *args)
    }

    fun wtf(t: Throwable?) {
        ROOT_TREE.wtf(t)
    }

    enum class LogPriority {
        VERBOSE, DEBUG, INFO, ERROR, WARN, ASSERT
    }

    abstract class Tree {

        internal val explicitTag = ThreadLocal<String>()

        protected abstract fun log(priority: LogPriority, tag: String?, message: String, t: Throwable?)

        open fun v(message: String?, vararg args: Any) {
            prepareLog(LogPriority.VERBOSE, null, message, args)
        }

        open fun v(t: Throwable?, message: String?, vararg args: Any) {
            prepareLog(LogPriority.VERBOSE, t, message, args)
        }

        open fun v(t: Throwable?) {
            prepareLog(LogPriority.VERBOSE, t, null)
        }

        open fun d(message: String?, vararg args: Any) {
            prepareLog(LogPriority.DEBUG, null, message, args)
        }

        open fun d(t: Throwable?, message: String?, vararg args: Any) {
            prepareLog(LogPriority.DEBUG, t, message, args)
        }

        open fun d(t: Throwable?) {
            prepareLog(LogPriority.DEBUG, t, null)
        }

        open fun i(message: String?, vararg args: Any) {
            prepareLog(LogPriority.DEBUG, null, message, args)
        }

        open fun i(t: Throwable?, message: String?, vararg args: Any) {
            prepareLog(LogPriority.INFO, t, message, args)
        }

        open fun i(t: Throwable?) {
            prepareLog(LogPriority.INFO, t, null)
        }

        open fun w(message: String?, vararg args: Any) {
            prepareLog(LogPriority.WARN, null, message, args)
        }

        open fun w(t: Throwable?, message: String?, vararg args: Any) {
            prepareLog(LogPriority.DEBUG, t, message, args)
        }

        open fun w(t: Throwable?) {
            prepareLog(LogPriority.WARN, t, null)
        }

        open fun e(message: String?, vararg args: Any) {
            prepareLog(LogPriority.ERROR, null, message, args)
        }

        open fun e(t: Throwable?, message: String?, vararg args: Any) {
            prepareLog(LogPriority.ERROR, t, message, args)
        }

        open fun e(t: Throwable?) {
            prepareLog(LogPriority.ERROR, t, null)
        }

        open fun wtf(message: String?, vararg args: Any) {
            prepareLog(LogPriority.ASSERT, null, message, args)
        }

        open fun wtf(t: Throwable?, message: String?, vararg args: Any) {
            prepareLog(LogPriority.ASSERT, t, message, args)
        }

        open fun wtf(t: Throwable?) {
            prepareLog(LogPriority.ASSERT, t, null)
        }

        protected open fun getTag(): String? {
            val tag = explicitTag.get()
            if (tag != null) {
                explicitTag.remove()
            }
            return tag
        }

        protected open fun formatMessage(message: String, args: Array<out Any>): String {
            return String.format(message, *args)
        }

        private fun prepareLog(priority: LogPriority, t: Throwable?, message: String?, vararg args: Any) {
            var resultMessage = message
            val tag = getTag()
            if (resultMessage != null && resultMessage.isEmpty()) {
                resultMessage = null
            }
            if (resultMessage == null) {
                if (t == null) {
                    return
                }
                resultMessage = getStackTraceString(t)
            } else {
                if (args.isNotEmpty()) {
                    resultMessage = formatMessage(resultMessage, args)
                }
                if (t != null) {
                    resultMessage += "\n" + getStackTraceString(t)
                }
            }
            log(priority, tag, resultMessage, t)
        }

        private fun getStackTraceString(t: Throwable): String {
            val sw = StringWriter(256)
            val pw = PrintWriter(sw, false)
            t.printStackTrace(pw)
            pw.flush()
            return sw.toString()
        }
    }
}