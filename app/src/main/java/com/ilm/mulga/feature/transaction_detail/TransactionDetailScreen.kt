import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.ilm.mulga.R
import com.ilm.mulga.feature.transaction_detail.TransactionDetailViewModel
import com.ilm.mulga.feature.transaction_detail.components.DetailLabel
import com.ilm.mulga.feature.transaction_detail.components.TransactionHeaderView
import com.ilm.mulga.presentation.model.TransactionDetailData
import com.ilm.mulga.presentation.model.type.Category
import com.ilm.mulga.ui.theme.MulGaTheme
import org.koin.androidx.compose.koinViewModel
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionDetailScreen(
    viewModel: TransactionDetailViewModel = koinViewModel(),
    navController: NavController,
    rootNavController: NavController
) {
    // viewModel에서 관리하는 TransactionDetailData
    val detailData by viewModel.transactionDetailData.collectAsState()

    Log.d("HIHIHIHIHI", "TransactionDetailScreen: ${detailData?.group.toString()}")

    // 데이터가 아직 없으면 로딩 상태 표시
    if (detailData == null) {
        Scaffold { innerPadding ->
            Text(
                text = "Loading...",
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(horizontal = 24.dp)
            )
        }
        return
    }

    // safe하게 사용할 수 있도록 로컬 변수에 저장
    val data = detailData!!

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "내역 추가",
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
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 24.dp)
        ) {
            // 헤더 영역: 카테고리, 금액, 편집 버튼
            TransactionHeaderView(
                category = data.category,
                cost = data.cost,
                onEditClick = { viewModel.onEditTransaction(rootNavController) }
            )

            Spacer(modifier = Modifier.height(50.dp))

            Column(
                verticalArrangement = Arrangement.spacedBy(28.dp)
            ) {
                DetailLabel(
                    label = stringResource(id = R.string.field_transaction_name),
                    value = data.title
                )
                DetailLabel(
                    label = stringResource(id = R.string.field_category),
                    value = data.category.displayName
                )
                DetailLabel(
                    label = stringResource(id = R.string.field_merchant),
                    value = data.vendor
                )
                DetailLabel(
                    label = stringResource(id = R.string.field_payment_method),
                    value = data.paymentMethod
                )
                DetailLabel(
                    label = stringResource(id = R.string.field_transaction_date_time),
                    value = data.time.format(DateTimeFormatter.ofPattern("yyyy년 MM월 dd일 HH:mm"))
                )
                DetailLabel(
                    label = stringResource(id = R.string.field_memo),
                    value = data.memo
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TransactionDetailScreenPreview() {
    // 더미 데이터 생성 (Category는 예시로 Category.CAFE 사용)
    val dummyData = TransactionDetailData(
        id = "1",
        title = "아이스 아메리카노 구매",
        category = Category.CAFE,  // Category.CAFE가 존재한다고 가정
        vendor = "스타벅스",
        time = LocalDateTime.now(),
        cost = 1500,
        memo = "아침 출근 전 커피",
        paymentMethod = "카카오 페이"
    )

    // fakeViewModel에 더미 데이터를 설정
    val fakeViewModel = TransactionDetailViewModel().apply {
        setTransactionDetail(dummyData)
    }
    // Preview에서는 rememberNavController()를 사용
    val navController = rememberNavController()
    val rootNavController = rememberNavController()
    TransactionDetailScreen(
        viewModel = fakeViewModel,
        navController = navController,
        rootNavController = rootNavController
    )
}
