package com.stevenpopovich.talktothat.taskmanager

import java.util.concurrent.SynchronousQueue

class TaskManager {

    private val taskQueue = SynchronousQueue<Task>()

    fun runTask(task: Task) {
        taskQueue.add(task)
    }
}
