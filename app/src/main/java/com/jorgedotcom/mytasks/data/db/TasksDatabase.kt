package com.jorgedotcom.mytasks.data.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [TaskEntity::class],
    version = 1
)
abstract class TasksDatabase : RoomDatabase() {

    abstract fun tasksDao(): TasksDao
}
