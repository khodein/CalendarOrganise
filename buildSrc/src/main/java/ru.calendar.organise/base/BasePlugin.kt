package ru.calendar.organise.base

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import ru.calendar.organise.model.DepConfig

abstract class BasePlugin : Plugin<Project> {

    protected fun checkAppOrLib(project: Project) {
        var isPlugin = false

        project.plugins.withId("com.android.application") {
            isPlugin = true
        }

        project.plugins.withId("com.android.library") {
            isPlugin = true
        }

        if (!isPlugin) {
            throw Exception("Добавьте com.android.library или com.android.application")
        }
    }

    protected fun setDependency(
        list: List<DepConfig>,
        target: Project,
    ) {
        target.dependencies {
            list.forEach { config ->
                add(
                    configurationName = config.method.value,
                    dependencyNotation = config.dependency,
                )
            }
        }
    }
}