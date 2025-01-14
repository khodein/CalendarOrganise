package ru.calendar.organise.base

import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalog
import ru.calendar.organise.model.DepConfig

abstract class BaseLib {

    abstract fun invoke(
        libs: VersionCatalog,
        target: Project
    ): DepConfig

    protected fun getDependency(
        module: String,
        name: String? = null,
        version: String,
    ): String {
        return buildString {
            appendPath(
                isDelimiter = false,
                path = module
            )
            name?.let {
                appendPath(
                    isDelimiter = true,
                    path = name
                )
            }
            appendPath(
                isDelimiter = true,
                path = version
            )
        }
    }

    private fun StringBuilder.appendPath(
        isDelimiter: Boolean,
        path: String
    ) {
        if (isDelimiter) append(DELIMITER)
        append(path)
    }

    private companion object {
        const val DELIMITER = ":"
    }
}