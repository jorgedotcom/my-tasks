package com.jorgedotcom.mytasks.di

import android.content.Context
import androidx.room.Room
import com.jorgedotcom.mytasks.data.db.TasksDao
import com.jorgedotcom.mytasks.data.db.TasksDatabase
import com.jorgedotcom.mytasks.data.repository.ITasksRepository
import com.jorgedotcom.mytasks.data.repository.TasksRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): TasksDatabase {
        return Room.databaseBuilder(
            context,
            TasksDatabase::class.java,
            "tasks-db"
        ).build()
    }

    @Provides
    fun provideTasksDao(database: TasksDatabase): TasksDao {
        return database.tasksDao()
    }

    @Provides
    fun provideTasksRepository(tasksDao: TasksDao): ITasksRepository {
        return TasksRepository(tasksDao)
    }
}