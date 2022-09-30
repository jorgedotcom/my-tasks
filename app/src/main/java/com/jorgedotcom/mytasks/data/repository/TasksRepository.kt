package com.jorgedotcom.mytasks.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.jorgedotcom.mytasks.data.db.TasksDao
import com.jorgedotcom.mytasks.data.db.toTask
import com.jorgedotcom.mytasks.domain.model.Task
import com.jorgedotcom.mytasks.domain.model.toTaskEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class TasksRepository @Inject constructor(
    private val tasksDao: TasksDao
) : ITasksRepository {

    override suspend fun createTask(task: Task) {
        tasksDao.addTask(task.toTaskEntity())
    }

    override suspend fun updateTask(task: Task) {
        tasksDao.updateTask(task.toTaskEntity())
    }

    override fun getAllTasksLiveData(): LiveData<List<Task>> {
        return tasksDao.getTasksLiveData().map { tasks ->
            tasks.map { it.toTask() }
        }
    }

    override fun getAllTasksFlow(): Flow<List<Task>> {
        return tasksDao.getTasksStateFlow().map { tasks ->
            tasks.map { it.toTask() }
        }
    }

    override fun getTaskLiveData(id: Int): LiveData<Task?> {
        return tasksDao.getTaskLiveDataById(id).map { it?.toTask() }
    }

    override fun getTaskFlow(id: Int): Flow<Task?> {
        return tasksDao.getTaskFlowById(id).map { it?.toTask() }
    }
}
