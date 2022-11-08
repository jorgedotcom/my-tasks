package com.jorgedotcom.mytasks.di

import androidx.room.Room
import com.jorgedotcom.mytasks.data.db.TasksDatabase
import com.jorgedotcom.mytasks.data.repository.ITasksRepository
import com.jorgedotcom.mytasks.data.repository.TasksRepository
import com.jorgedotcom.mytasks.view.new_task.NewTaskViewModel
import com.jorgedotcom.mytasks.view.tasks.TasksViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val appModule = module {
    single {
        Room.databaseBuilder(
            get(),
            TasksDatabase::class.java,
            "tasks-db"
        ).build()
    }
    single {
        get<TasksDatabase>().tasksDao()
    }
    single<ITasksRepository> {
        TasksRepository(get())
    }
    viewModelOf(::TasksViewModel)
    viewModelOf(::NewTaskViewModel)
}
