package com.jorgedotcom.mytasks.view.new_task

import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.google.android.material.datepicker.MaterialDatePicker
import com.jorgedotcom.mytasks.R
import com.jorgedotcom.mytasks.view.DefaultTextField
import com.jorgedotcom.mytasks.view.MyTasksAppBar
import com.jorgedotcom.mytasks.view.MyTasksExtendedFab

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewTaskScreen(
    uiState: NewTaskUiState,
    onNavIconClick: () -> Unit,
    onTitleChange: (String) -> Unit,
    onDescriptionChange: (String) -> Unit,
    onDateSelected: (Long) -> Unit,
    onCreateTaskClick: () -> Unit
) {
    val activity = LocalContext.current as AppCompatActivity

    Scaffold(
        topBar = {
            MyTasksAppBar(
                title = if (uiState.isEdit) "Edit Task" else "New Task",
                navigationIcon = Icons.Rounded.ArrowBack,
                onNavIconClick = onNavIconClick
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(10.dp)
        ) {
            Spacer(modifier = Modifier.height(5.dp))
            DefaultTextField(
                modifier = Modifier.fillMaxWidth(),
                placeholder = "Learn Jetpack Compose",
                value = uiState.taskTitle,
                onValueChange = onTitleChange,
                singleLine = true
            )
            Spacer(modifier = Modifier.height(5.dp))
            DefaultTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                placeholder = "Take a look at the official docs...",
                value = uiState.taskDescription,
                onValueChange = onDescriptionChange
            )
            Spacer(modifier = Modifier.height(5.dp))
            Row(
                modifier = Modifier
                    .height(TextFieldDefaults.MinHeight)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .border(
                        color = MaterialTheme.colorScheme.outline,
                        width = 1.dp,
                        shape = RoundedCornerShape(10.dp)
                    )
                    .clickable { showDatePicker(activity, onDateSelected) }
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (uiState.formattedDate != null) {
                    Text(
                        modifier = Modifier.weight(1f),
                        text = uiState.formattedDate,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                } else {
                    Text(
                        modifier = Modifier.weight(1f),
                        text = "Due to...",
                        color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.5f)
                    )
                }
                Icon(
                    painter = painterResource(R.drawable.ic_calendar),
                    contentDescription = null,
                )
            }
            Spacer(modifier = Modifier.height(20.dp))
            MyTasksExtendedFab(
                modifier = Modifier.fillMaxWidth(),
                text = if (uiState.isEdit) "Update Task" else "Create New Task",
                onClick = onCreateTaskClick
            )
        }
    }
}

fun showDatePicker(
    activity: AppCompatActivity,
    onDateSelected: (Long) -> Unit
) {
    val picker = MaterialDatePicker.Builder
        .datePicker()
        .setTheme(R.style.DatePickerTheme)
        .build()
    picker.show(activity.supportFragmentManager, picker.toString())
    picker.addOnPositiveButtonClickListener { onDateSelected(it) }
}