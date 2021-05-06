package com.stevenpopovich.talktothat.taskmanager

import com.stevenpopovich.talktothat.taskmanager.tasks.Task

class TaskManager(
    private var currentTask: Task? = null
) {
    fun stop() {
        currentTask?.finish()
    }

    fun clearTask() {
        currentTask = null
    }

    fun runTask(task: Task) {
        if (currentTask == null) {
            currentTask = task
            currentTask?.start()
        }
    }
}
