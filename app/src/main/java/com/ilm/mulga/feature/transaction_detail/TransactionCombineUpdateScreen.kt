package com.ilm.mulga.feature.transaction_detail

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import com.ilm.mulga.feature.transaction_detail.components.CombineNoticeBadge
import com.ilm.mulga.feature.transaction_detail.components.UnderlinedTextField
import com.ilm.mulga.features.category.components.CategoryModal
import com.ilm.mulga.presentation.model.TransactionDetailData
import com.ilm.mulga.presentation.model.type.Category
import com.ilm.mulga.ui.theme.MulGaTheme
import com.ilm.mulga.util.extension.toKoreanDisplayString
import com.ilm.mulga.util.extension.withCommas
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.time.LocalDateTime
import kotlin.math.absoluteValue

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionCombineUpdateScreen(
    navController: NavController,
    transactionId: String? = null,
    initialData: TransactionDetailData
) {
    var title by remember { mutableStateOf("") }
    var memo by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf<Category?>(null) }
    var isCategoryModalVisible by remember { mutableStateOf(false) }
    var selectedDateTime by remember { mutableStateOf(LocalDateTime.now()) }
    var amount by remember { mutableStateOf("") }

    var isCategoryError by remember { mutableStateOf(false) }

    val viewModel: TransactionCombineUpdateViewModel = viewModel()
    val isSuccess by viewModel.isSuccess
    var showSuccessDialog by remember { mutableStateOf(false)}

    LaunchedEffect(initialData) {
        title = initialData.title
        amount = String.format("%,d", initialData.cost)
        selectedCategory = initialData.category
        selectedDateTime = initialData.time
        memo = initialData.memo
    }

    LaunchedEffect(isSuccess) {
        if (isSuccess) {
            viewModel.successResponse.value?.let { response ->
                val json = Json.encodeToString(response)
                navController.previousBackStackEntry?.savedStateHandle?.set("updatedTransactionJson", json)
            }
            showSuccessDialog = true
        }
    }

    LaunchedEffect(viewModel.isUncombineSuccess.value) {
        if (viewModel.isUncombineSuccess.value) {
            navController.popBackStack()
            navController.popBackStack()
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "내역 수정",
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

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = 24.dp,
                        end = 24.dp,
                        bottom = 24.dp + insets.calculateBottomPadding()
                    ),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // 합치기 해제 버튼
                Button(
                    onClick = {
                        viewModel.uncombineTransaction(transactionId!!)
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(54.dp),
                    shape = RoundedCornerShape(30.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MulGaTheme.colors.transparent,
                        contentColor = MulGaTheme.colors.primary
                    ),
                    border = BorderStroke(1.dp, MulGaTheme.colors.primary)
                ) {
                    Text(
                        text = "합치기 해제하기",
                        style = MulGaTheme.typography.bodyLarge
                    )
                }

                // 완료 버튼
                Button(
                    onClick = {
                        isCategoryError = selectedCategory == null
                        if (isCategoryError) return@Button

                        viewModel.patchTransaction(
                            id = transactionId!!,
                            title = title,
                            cost = amount.replace(",", "").toInt(),
                            category = selectedCategory?.backendKey,
                            vendor = initialData.vendor,
                            paymentMethod = initialData.paymentMethod,
                            dateTime = selectedDateTime,
                            memo = memo
                        )
                    },
                    modifier = Modifier
                        .weight(1f)
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

                Spacer(modifier = Modifier.width(8.dp))
                CombineNoticeBadge(combineNum = initialData.group.size.toString(), modifier = Modifier)
            }

            Row(
                modifier = Modifier.padding(bottom = 14.dp)
            ) {
                Text(
                    text = if (initialData.cost >= 0) "+${initialData.cost.toString().withCommas()}" else "-${initialData.cost.absoluteValue.toString().withCommas()}",
                    modifier = Modifier.weight(1f),
                    style = MulGaTheme.typography.headline,
                    color = MulGaTheme.colors.grey2,
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

            TransactionInputRow(label = "거래일시", content = {
                Row(
                    verticalAlignment = Alignment.Top,
                    modifier = Modifier
                        .weight(1f)
                        .padding(bottom = 8.dp)
                ) {
                    Text(
                        text = selectedDateTime.toKoreanDisplayString(),
                        style = MulGaTheme.typography.bodyLarge,
                        color = MulGaTheme.colors.grey2,
                        modifier = Modifier.weight(1f)
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

        if (showSuccessDialog) {
            CustomDialog(
                title = "저장 완료",
                message = "저장이 완료되었습니다.",
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

@Preview(showBackground = true)
@Composable
fun TransactionCombineUpdateScreenPreview() {
    val fakeNavController = rememberNavController()
    MulGaTheme {
        TransactionCombineUpdateScreen(
            navController = fakeNavController,
            initialData = TransactionDetailData(
                id = "1",
                title = "아이스 아메리카노 구매",
                category = Category.CAFE,  // Category.CAFE가 존재한다고 가정
                vendor = "스타벅스",
                time = LocalDateTime.now(),
                cost = 1500,
                memo = "아침 출근 전 커피",
                paymentMethod = "카카오 페이"
            )
        )
    }
}