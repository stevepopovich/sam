package com.stevenpopovich.talktothat.taskmanager.tasks

import com.stevenpopovich.talktothat.MainDependencyModule

interface Task {
    fun start()
    fun finish() {
        MainDependencyModule.taskManager.clearTask()
    }
}
