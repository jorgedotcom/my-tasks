package com.jorgedotcom.mytasks

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.jorgedotcom.mytasks.data.db.DEFAULT_TASK_ID
import com.jorgedotcom.mytasks.ui.theme.MyTasksTheme
import com.jorgedotcom.mytasks.view.new_task.NewTaskScreen
import com.jorgedotcom.mytasks.view.new_task.NewTaskViewModel
import com.jorgedotcom.mytasks.view.tasks.TasksScreen
import com.jorgedotcom.mytasks.view.tasks.TasksUiState
import com.jorgedotcom.mytasks.view.tasks.TasksViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyTasksTheme {
                MyTasksNavHost()
            }
        }
    }
}

@Composable
fun MyTasksNavHost() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "main") {
        composable("main") {
            val tasksViewModel = hiltViewModel<TasksViewModel>()
            val uiState by tasksViewModel.tasksUiState.observeAsState(TasksUiState())

            TasksScreen(
                uiState = uiState,
                onCreateNewTask = { navController.navigate("new-task") },
                onEditTask = { navController.navigate("new-task?taskId=$it") }
            )
        }
        composable(
            "new-task?taskId={taskId}",
            arguments = listOf(
                navArgument("taskId") {
                    defaultValue = DEFAULT_TASK_ID
                    type = NavType.IntType
                }
            )
        ) {
            val newTaskViewModel = hiltViewModel<NewTaskViewModel>()
            val uiState by newTaskViewModel.newTaskUiState.collectAsState()
            if (uiState.shouldNavBack) {
                navController.navigateUp()
            }

            NewTaskScreen(
                uiState = uiState,
                onNavIconClick = { navController.navigateUp() },
                onTitleChange = newTaskViewModel::updateTaskTitle,
                onDescriptionChange = newTaskViewModel::updateTaskDescription,
                onDateSelected = newTaskViewModel::formatDate,
                onCreateTaskClick = newTaskViewModel::addEditTask
            )
        }
    }
}
