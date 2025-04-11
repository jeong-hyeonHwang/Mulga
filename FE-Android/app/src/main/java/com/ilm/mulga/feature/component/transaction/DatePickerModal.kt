package com.ilm.mulga.feature.component.transaction

import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.window.DialogProperties
import com.ilm.mulga.ui.theme.MulGaTheme
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerModal(
    onConfirm: (LocalDate) -> Unit,
    onDismiss: () -> Unit
) {
    val state = rememberDatePickerState()

    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                state.selectedDateMillis?.let {
                    val localDate = Instant.ofEpochMilli(it)
                        .atZone(ZoneId.systemDefault())
                        .toLocalDate()
                    onConfirm(localDate)
                }
                onDismiss()
            }) {
                Text("확인", color = MulGaTheme.colors.primary)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("취소", color = MulGaTheme.colors.grey1)
            }
        },
        properties = DialogProperties(),
        colors = DatePickerDefaults.colors(
            containerColor = MulGaTheme.colors.grey5,
            titleContentColor = MulGaTheme.colors.grey1,
            headlineContentColor = MulGaTheme.colors.grey1,
            weekdayContentColor = MulGaTheme.colors.grey3,
            subheadContentColor = MulGaTheme.colors.grey1,
            yearContentColor = MulGaTheme.colors.grey1,
            currentYearContentColor = MulGaTheme.colors.primary,
            selectedYearContentColor = MulGaTheme.colors.grey5,
            selectedYearContainerColor = MulGaTheme.colors.primary,
            dayContentColor = MulGaTheme.colors.grey1,
            selectedDayContentColor = MulGaTheme.colors.grey5,
            selectedDayContainerColor = MulGaTheme.colors.primary,
            todayContentColor = MulGaTheme.colors.primary,
            todayDateBorderColor = MulGaTheme.colors.primary
        )
    ) {
        DatePicker(
            state = state,
            colors = DatePickerDefaults.colors(
                containerColor = MulGaTheme.colors.grey5,
                titleContentColor = MulGaTheme.colors.grey1,
                headlineContentColor = MulGaTheme.colors.grey1,
                weekdayContentColor = MulGaTheme.colors.grey3,
                subheadContentColor = MulGaTheme.colors.grey1,
                yearContentColor = MulGaTheme.colors.grey1,
                currentYearContentColor = MulGaTheme.colors.primary,
                selectedYearContentColor = MulGaTheme.colors.grey5,
                selectedYearContainerColor = MulGaTheme.colors.primary,
                dayContentColor = MulGaTheme.colors.grey1,
                selectedDayContentColor = MulGaTheme.colors.grey5,
                selectedDayContainerColor = MulGaTheme.colors.primary,
                todayContentColor = MulGaTheme.colors.primary,
                todayDateBorderColor = MulGaTheme.colors.primary
            )
        )
    }
}