package com.ilm.mulga.feature.analysis

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ilm.mulga.feature.analysis.components.CategoryItemRaw
import com.ilm.mulga.feature.analysis.components.CategoryList
import com.ilm.mulga.feature.analysis.components.DonutChart
import com.ilm.mulga.feature.analysis.components.Graphs
import com.ilm.mulga.feature.analysis.components.PaymentList
import com.ilm.mulga.feature.analysis.components.Separator
import com.ilm.mulga.feature.analysis.components.YearMonthSelector
import java.time.LocalDate
import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.ilm.mulga.data.network.RetrofitClient
import com.ilm.mulga.data.dto.response.AnalysisDto
import com.ilm.mulga.feature.analysis.components.PaymentItemData
import kotlinx.coroutines.launch
import retrofit2.Response

//@Preview(showBackground = true)
@Composable
fun AnalysisScreen() {

    val viewModel: AnalysisViewModel = viewModel()

    // Get today's date for initializing
    val selectedYear = viewModel.selectedYear.value
    val selectedMonth = viewModel.selectedMonth.value

    // State to hold the API response data
    var analysisData by remember { mutableStateOf<AnalysisDto?>(null) }
    var loading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    // CoroutineScope to make the API request
    val scope = rememberCoroutineScope()

    // Function to fetch data from API
    fun fetchAnalysisData(year: Int, month: Int) {
        loading = true
        scope.launch {
            try {
                // Make the API call
                val response: Response<AnalysisDto> = RetrofitClient.apiAnalysisService.getAnalysis(year, month)
                Log.d("APIResponse", "Raw Response Body: ${response.raw()}")
                if (response.isSuccessful) {
                    // Handle successful response
                    analysisData = response.body()
                    errorMessage = null
                    viewModel.setTotal(analysisData?.monthTotal ?: 0)
                    viewModel.setSlices(analysisData?.category?.map { it.value } ?: emptyList())
                    viewModel.setItems(analysisData?.category?.filter { it.value != 0 }?.map { CategoryItemRaw(it.key, it.value) } ?: emptyList())
                    Log.d("APIResponse", "Response body: ${response.body()}")
                } else {
                    // Handle API failure
                    errorMessage = "Error: ${response.code()} - ${response.message()}"
                    Log.e("APIResponse", "Error: ${response.errorBody()}")
                }
            } catch (e: Exception) {
                // Handle network errors or other exceptions
                errorMessage = "Error: ${e.message}"
            } finally {
                loading = false
            }
        }
    }

    // Call the API whenever the year or month changes
    LaunchedEffect(selectedYear, selectedMonth) {
        fetchAnalysisData(selectedYear, selectedMonth)
    }

    // Example list of items passed to CategoryList
    val categoryItems = viewModel.items.value

    val total = viewModel.total.value

    val detail = categoryItems.size <= 5

    val paymentItems = analysisData?.paymentMethod
        ?.map { (source, amount) ->
        PaymentItemData(source, amount) }
        ?: emptyList()

    val chartSlices = viewModel.slices.value

    val chartSlicesSimplified = if (chartSlices.size > 5) {
        chartSlices.take(5) + listOf(total - chartSlices.take(5).sum())
    } else {
        chartSlices
    }

    val barHeights = listOf(100f, 120f, 80f, 150f, 90f, 110f) // Custom heights for each bar
    val labelTexts = listOf("10월", "11월", "12월", "1월", "2월", "3월") // Custom labels
    val line1Data = listOf(0f, 0f, 20f, 20f, 20f, 20f, 20f, 20f, 20f, 40f, 50f, 50f, 50f, 50f, 50f, 70f, 70f, 80f, 100f, 120f, 120f, 150f, 150f, 150f, 190f, 200f, 200f, 200f, 200f, 200f, 200f) // Data for the first line
    val line2Data = listOf(0f, 10f, 10f, 10f, 10f, 20f, 20f, 20f, 20f, 40f, 50f, 50f, 50f, 50f, 50f, 70f, 70f, 80f, 100f, 120f, 120f, 150f, 150f, 150f, 190f, 210f, 240f, 250f, 250f, 250f, 250f) // Data for the second line

    // Handle loading, error, or success state in the UI
    Column(
        modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        YearMonthSelector(
            detail = false,
            selectedYear = selectedYear,
            selectedMonth = selectedMonth,
            onYearMonthChanged = { year, month ->
                viewModel.setSelectedYear(year)
                viewModel.setSelectedMonth(month)
            }
        )

        DonutChart(
            slices = chartSlicesSimplified,
            total = total,
            modifier = Modifier.size(300.dp)
        )

        CategoryList(items = categoryItems.take(5), total = total, detail = detail)

        Separator()

        PaymentList(items = paymentItems)

        Separator()

        Graphs(barHeights, labelTexts, line1Data, line2Data, selectedYear, selectedMonth)
    }
}
