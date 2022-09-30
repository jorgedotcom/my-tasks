package com.jorgedotcom.mytasks.data.repository

import androidx.lifecycle.LiveData
import com.jorgedotcom.mytasks.domain.model.Task
import kotlinx.coroutines.flow.Flow

interface ITasksRepository {

    suspend fun createTask(task: Task)

    suspend fun updateTask(task: Task)

    fun getAllTasksLiveData(): LiveData<List<Task>>

    fun getAllTasksFlow(): Flow<List<Task>>

    fun getTaskLiveData(id: Int): LiveData<Task?>

    fun getTaskFlow(id: Int): Flow<Task?>
}
