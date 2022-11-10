package com.jorgedotcom.mytasks.data.db

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(AndroidJUnit4::class)
@SmallTest
class TasksDaoTest {

    private lateinit var tasksDatabase: TasksDatabase
    private lateinit var tasksDao: TasksDao

    @Before
    fun setUp() {
        tasksDatabase = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            TasksDatabase::class.java
        ).allowMainThreadQueries().build()
        tasksDao = tasksDatabase.tasksDao()
    }

    @After
    fun tearDown() {
        tasksDatabase.close()
    }

    @Test
    fun addTask_insertsDataCorrectly() = runTest {
        // Given
        val taskEntity = TaskEntity(
            title = "Title",
            description = "Description",
            date = "Date"
        )

        // When
        tasksDao.addTask(taskEntity)

        // Then
        val result = tasksDao.getTaskFlowById(1).first()
        assertThat(result?.title, `is`(taskEntity.title))
        assertThat(result?.description, `is`(taskEntity.description))
        assertThat(result?.date, `is`(taskEntity.date))
    }

    @Test
    fun updateTask_updatesDataCorrectly() = runTest {
        // Given
        val taskEntity = TaskEntity(
            title = "Title",
            description = "Description",
            date = "Date"
        )
        tasksDao.addTask(taskEntity)

        // When
        val updatedTask = TaskEntity(
            id = 1,
            title = "New Title",
            description = "New Description",
            date = "New Date"
        )
        tasksDao.updateTask(updatedTask)

        // Then
        val result = tasksDao.getTaskFlowById(1).first()
        assertThat(result?.title, `is`(updatedTask.title))
        assertThat(result?.description, `is`(updatedTask.description))
        assertThat(result?.date, `is`(updatedTask.date))
    }
}
