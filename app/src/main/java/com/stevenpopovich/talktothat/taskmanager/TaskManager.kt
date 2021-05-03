package com.stevenpopovich.talktothat.taskmanager

class TaskManager {
    private var currentTask: Task? = null

    fun stop() {
        currentTask?.finish()
        currentTask = null
    }

    fun runTask(task: Task) {
        if (currentTask == null) {
            currentTask = task
            currentTask?.start()
        }
    }
}
