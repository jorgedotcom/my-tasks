package com.jorgedotcom.mytasks.data.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface TasksDao {

    @Insert
    suspend fun addTask(taskEntity: TaskEntity)

    @Update
    suspend fun updateTask(taskEntity: TaskEntity)

    @Query("SELECT * FROM TaskEntity")
    fun getTasksLiveData(): LiveData<List<TaskEntity>>

    @Query("SELECT * FROM TaskEntity")
    fun getTasksStateFlow(): Flow<List<TaskEntity>>

    @Query("SELECT * FROM TaskEntity WHERE id = :id")
    fun getTaskLiveDataById(id: Int): LiveData<TaskEntity?>

    @Query("SELECT * FROM TaskEntity WHERE id = :id")
    fun getTaskFlowById(id: Int): Flow<TaskEntity?>
}
