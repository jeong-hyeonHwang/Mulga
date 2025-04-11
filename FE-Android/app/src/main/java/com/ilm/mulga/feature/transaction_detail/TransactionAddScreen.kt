package com.ilm.mulga.feature.transaction_detail

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.ilm.mulga.R
import com.ilm.mulga.feature.component.dialog.CustomDialog
import com.ilm.mulga.feature.component.toggle.ToggleSwitch
import com.ilm.mulga.feature.component.transaction.DatePickerModal
import com.ilm.mulga.feature.component.transaction.TimePickerModal
import com.ilm.mulga.feature.login.numberCommaTransformation
import com.ilm.mulga.feature.transaction_detail.components.UnderlinedTextField
import com.ilm.mulga.features.category.components.CategoryModal
import com.ilm.mulga.presentation.model.TransactionDetailData
import com.ilm.mulga.presentation.model.type.Category
import com.ilm.mulga.ui.theme.MulGaTheme
import com.ilm.mulga.util.extension.toKoreanDisplayString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.time.LocalDateTime
import kotlin.math.absoluteValue

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionAddScreen(
    navController: NavController,
    transactionId: String? = null,
    initialData: TransactionDetailData? = null
) {
    var title by remember { mutableStateOf("") }
    var memo by remember { mutableStateOf("") }
    var vendor by remember { mutableStateOf("") }
    var payment by remember { mutableStateOf("") }
    var selectedToggleIndex by remember { mutableStateOf(0) }
    var selectedCategory by remember { mutableStateOf<Category?>(null) }
    var isCategoryModalVisible by remember { mutableStateOf(false) }
    var selectedDateTime by remember { mutableStateOf(LocalDateTime.now()) }
    var showDatePicker by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }
    var amount by remember { mutableStateOf("") }

    var isAmountError by remember { mutableStateOf(false) }
    var isCategoryError by remember { mutableStateOf(false) }
    var isPaymentError by remember { mutableStateOf(false) }

    val viewModel: TransactionAddViewModel = viewModel()
    val isSuccess by viewModel.isSuccess
    val isEditMode by remember { mutableStateOf(transactionId != null && initialData != null) }
    var showSuccessDialog by remember { mutableStateOf(false)}

    LaunchedEffect(initialData) {
        if (isEditMode) {
            title = initialData?.title ?: ""
            amount = initialData?.cost?.absoluteValue?.toString() ?: ""
            selectedToggleIndex = if ((initialData?.cost ?: 0) < 0) 0 else 1
            selectedCategory = initialData?.category
            vendor = initialData?.vendor ?: ""
            payment = initialData?.paymentMethod ?: ""
            selectedDateTime = initialData?.time ?: LocalDateTime.now()
            memo = initialData?.memo ?: ""
        }
    }

    LaunchedEffect(isSuccess) {
        if (isSuccess) {
            viewModel.successResponse.value?.let { response ->
                if (isEditMode) {
                    val json = Json.encodeToString(response)
                    navController.previousBackStackEntry?.savedStateHandle?.set("updatedTransactionJson", json)
                }
            }
            showSuccessDialog = true
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = if (isEditMode) "내역 수정" else "내역 추가",
                        style = MulGaTheme.typography.bodyLarge,
                        color = MulGaTheme.colors.grey1
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_util_caret_left),
                            contentDescription = "뒤로가기",
                            tint = MulGaTheme.colors.grey1,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MulGaTheme.colors.white1,
                    titleContentColor = MulGaTheme.colors.grey1
                )
            )
        },
        bottomBar = {
            val insets = WindowInsets.navigationBars.asPaddingValues()
            Button(
                onClick = {
                    isAmountError = amount.isBlank()
                    isCategoryError = selectedCategory == null
                    isPaymentError = payment.isBlank()

                    if (isAmountError || isCategoryError || isPaymentError) return@Button

                    if (isEditMode) {
                        viewModel.patchTransaction(
                            id = transactionId!!,
                            title = title,
                            cost = if (selectedToggleIndex == 0) -amount.toInt() else amount.toInt(),
                            category = selectedCategory?.backendKey,
                            vendor = vendor,
                            paymentMethod = payment,
                            dateTime = selectedDateTime,
                            memo = memo
                        )
                    } else {
                        viewModel.submitTransaction(
                            title = title,
                            cost = if (selectedToggleIndex == 0) -amount.toInt() else amount.toInt(),
                            category = selectedCategory?.backendKey ?: "",
                            vendor = vendor,
                            paymentMethod = payment,
                            dateTime = selectedDateTime,
                            memo = memo
                        )
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = 24.dp,
                        end = 24.dp,
                        bottom = 24.dp + insets.calculateBottomPadding()
                    )
                    .height(54.dp),
                shape = RoundedCornerShape(30.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MulGaTheme.colors.primary,
                    contentColor = MulGaTheme.colors.white1
                )
            ) {
                Text(
                    text = "완료",
                    style = MulGaTheme.typography.bodyLarge
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .background(MulGaTheme.colors.white1)
                .padding(horizontal = 24.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(vertical = 16.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .clip(CircleShape)
                        .background(
                            if (selectedCategory != null) Color.Transparent else MulGaTheme.colors.grey3
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    selectedCategory?.let {
                        Icon(
                            painter = painterResource(id = it.iconResId),
                            contentDescription = it.displayName,
                            modifier = Modifier.size(24.dp),
                            tint = Color.Unspecified
                        )
                    }
                }

                Text(
                    text = selectedCategory?.displayName ?: "카테고리를 선택해주세요",
                    style = MulGaTheme.typography.bodyMedium,
                    color = MulGaTheme.colors.grey2,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }

            Row(
                modifier = Modifier.padding(bottom = 14.dp)
            ) {
                Text(
                    text = if (selectedToggleIndex == 0) "-" else "+",
                    style = MulGaTheme.typography.headline,
                    color = MulGaTheme.colors.black1,
                    modifier = Modifier
                        .width(30.dp)
                        .alignByBaseline()
                )

                UnderlinedTextField(
                    value = amount,
                    onValueChange = {
                        amount = it.filter { char -> char.isDigit() }
                        isAmountError = false
                    },
                    placeholder = "금액",
                    errorText = if (isAmountError) "금액을 입력해주세요" else null,
                    modifier = Modifier
                        .weight(1f)
                        .alignByBaseline(),
                    singleLine = true,
                    textStyle = MulGaTheme.typography.headline,
                    visualTransformation = { text -> numberCommaTransformation(text) }
                )

                ToggleSwitch(
                    selectedIndex = selectedToggleIndex,
                    onOptionSelected = { selectedToggleIndex = it },
                    firstLabel = "지출",
                    secondLabel = "수입",
                    modifier = Modifier
                        .padding(bottom = 8.dp)
                        .align(Alignment.CenterVertically)
                )
            }

            TransactionInputRow(label = "내역명", content = {
                UnderlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    placeholder = "내역명",
                    modifier = Modifier
                        .fillMaxWidth(),
                    singleLine = false,
                    maxLines = 5
                )
            })

            TransactionInputRow(label = "카테고리", content = {
                Column {
                    Row(
                        verticalAlignment = Alignment.Top,
                        modifier = Modifier
                            .clickable { isCategoryModalVisible = true }
                            .padding(bottom = 8.dp)
                    ) {
                        Text(
                            text = selectedCategory?.displayName ?: "카테고리",
                            style = MulGaTheme.typography.bodyLarge,
                            color = MulGaTheme.colors.primary,
                            modifier = Modifier.weight(1f)
                        )
                        Icon(
                            painter = painterResource(id = R.drawable.ic_util_caret_right),
                            contentDescription = "카테고리 선택",
                            tint = MulGaTheme.colors.grey1,
                            modifier = Modifier.size(16.dp)
                        )
                    }

                    if (isCategoryError) {
                        Text(
                            "카테고리를 선택해주세요",
                            color = MulGaTheme.colors.red1,
                            style = MulGaTheme.typography.caption
                        )
                    }
                }
            })

            TransactionInputRow(label = "거래처", content = {
                UnderlinedTextField(
                    value = vendor,
                    onValueChange = { vendor = it },
                    placeholder = "거래처",
                    modifier = Modifier
                        .fillMaxWidth(),
                    singleLine = false,
                    maxLines = 5
                )
            })

            TransactionInputRow(label = "결제수단", content = {
                UnderlinedTextField(
                    value = payment,
                    onValueChange = {
                        payment = it
                        isPaymentError = false
                    },
                    placeholder = "결제수단",
                    errorText = if (isPaymentError) "결제수단을 입력해주세요" else null,
                    modifier = Modifier
                        .fillMaxWidth(),
                    singleLine = false,
                    maxLines = 5
                )
            })

            TransactionInputRow(label = "거래일시", content = {
                Row(
                    verticalAlignment = Alignment.Top,
                    modifier = Modifier
                        .weight(1f)
                        .clickable { showDatePicker = true }
                        .padding(bottom = 8.dp)
                ) {
                    Text(
                        text = selectedDateTime.toKoreanDisplayString(),
                        style = MulGaTheme.typography.bodyLarge,
                        color = MulGaTheme.colors.primary,
                        modifier = Modifier.weight(1f)
                    )
                    Icon(
                        painter = painterResource(id = R.drawable.ic_util_caret_right),
                        contentDescription = "날짜 선택",
                        tint = MulGaTheme.colors.grey1,
                        modifier = Modifier.size(16.dp)
                    )
                }
            })

            TransactionInputRow(label = "메모", content = {
                UnderlinedTextField(
                    value = memo,
                    onValueChange = { memo = it },
                    placeholder = "메모를 입력해주세요",
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 100.dp),
                    singleLine = false,
                    maxLines = 5
                )
            })
        }

        if (isCategoryModalVisible) {
            CategoryModal(
                selectedCategory = selectedCategory,
                onCategorySelected = {
                    selectedCategory = it
                    isCategoryError = false
                    isCategoryModalVisible = false
                },
                onDismiss = { isCategoryModalVisible = false }
            )
        }

        if (showDatePicker) {
            DatePickerModal(
                onConfirm = {
                    selectedDateTime = selectedDateTime.withYear(it.year)
                        .withMonth(it.monthValue)
                        .withDayOfMonth(it.dayOfMonth)
                    showDatePicker = false
                    showTimePicker = true
                },
                onDismiss = { showDatePicker = false }
            )
        }

        if (showTimePicker) {
            TimePickerModal(
                onConfirm = {
                    selectedDateTime = selectedDateTime.withHour(it.hour)
                        .withMinute(it.minute)
                },
                onDismiss = { showTimePicker = false }
            )
        }

        if (showSuccessDialog) {
            CustomDialog(
                title = if (isEditMode) "수정 완료" else "저장 완료",
                message = if (isEditMode) "수정이 완료되었습니다." else "저장이 완료되었습니다.",
                onDismiss = {
                    showSuccessDialog = false
                    navController.popBackStack()
                },
                onConfirm = {
                    showSuccessDialog = false
                    navController.popBackStack()
                },
                backgroundColor = MulGaTheme.colors.primary
            )
        }

    }
}

@Composable
fun TransactionInputRow(
    label: String,
    content: @Composable () -> Unit,
    verticalPadding: androidx.compose.ui.unit.Dp = 16.dp
) {
    Row(
        modifier = Modifier.padding(vertical = verticalPadding),
        verticalAlignment = Alignment.Top
    ) {
        Text(
            text = label,
            style = MulGaTheme.typography.bodyLarge,
            color = MulGaTheme.colors.grey1,
            modifier = Modifier
                .width(108.dp)
                .alignByBaseline()
        )
        content()
    }
}

@Preview(showBackground = true)
@Composable
fun TransactionAddScreenPreview() {
    val fakeNavController = rememberNavController()
    MulGaTheme {
        TransactionAddScreen(navController = fakeNavController)
    }
}