package com.ilm.mulga.data.service

import com.ilm.mulga.data.dto.request.TransactionRequestDto
import com.ilm.mulga.data.dto.request.TransactionUpdateRequestDto
import com.ilm.mulga.data.dto.response.MonthlyTransactionResponseDto
import com.ilm.mulga.presentation.model.TransactionDetailData
import kotlinx.serialization.json.Json
import retrofit2.Response
import java.time.YearMonth
import kotlin.random.Random

class FakeTransactionService : TransactionService {

    override suspend fun getMonthlyTransactions(year: Int, month: Int): Response<MonthlyTransactionResponseDto> {
        val daysInMonth = YearMonth.of(year, month).lengthOfMonth()

        // daily 섹션 JSON 생성: 1일부터 31일까지
        val dailyJson = buildString {
            append("{")
            for (day in 1..31) {
                append("\"$day\": ")
                if (day == 10) {
                    append(
                        "{ \"isValid\": false, \"income\": ${0}, \"expense\": ${0}, " +
                                "\"date\": \"${year}-${"%02d".format(month)}-${"%02d".format(day)}\" }"
                    )
                } else if (day <= daysInMonth) {
                    // 실제 존재하는 날: isValid true, 더미 데이터
                    append(
                        "{ \"isValid\": true, \"income\": ${1000 * day}, \"expense\": ${-5000 * day}, " +
                                "\"date\": \"${year}-${"%02d".format(month)}-${"%02d".format(day)}\" }"
                    )
                } else {
                    // 실제 일수가 없는 날: isValid false, income과 expense 0, 날짜는 마지막 날로 설정
                    append(
                        "{ \"isValid\": false, \"income\": 0, \"expense\": 0, " +
                                "\"date\": \"${year}-${"%02d".format(month)}-${"%02d".format(daysInMonth)}\" }"
                    )
                }
                if (day < 31) append(",")
            }
            append("}")
        }

        // transactions 섹션 JSON 생성: 1일부터 31일까지
        val transactionsJson = buildString {
            append("{")
            for (day in 1..31) {
                append("\"$day\": ")
                if (day <= daysInMonth) {
                    // 테스트 케이스 적용
                    when (day) {
                        10 -> {
                            // 케이스 1: 거래 내역이 하나도 없는 경우
                            append("[]")
                        }
                        11 -> {
                            // 케이스 2: 거래 내역이 있는데 vendor가 없는 경우
                            append("[{")
                            append("\"_id\": \"id_day$day\",")
                            append("\"year\": $year,")
                            append("\"month\": $month,")
                            append("\"day\": $day,")
                            append("\"isCombined\": false,")
                            append("\"title\": \"Transaction for day $day\",")
                            append("\"cost\": ${-1000 * day},")
                            append("\"category\": \"dummy\",")
                            append("\"memo\": \"Memo for day $day\",")
                            // vendor가 빈 문자열
                            append("\"vendor\": \"\",")
                            append("\"time\": \"${year}-${"%02d".format(month)}-${"%02d".format(day)}T12:00:00Z\",")
                            append("\"paymentMethod\": \"Card\",")
                            append("\"group\": []")
                            append("}]")
                        }
                        12 -> {
                            // 케이스 3: 거래 내역이 있는데 title이 없는 경우
                            append("[{")
                            append("\"_id\": \"id_day$day\",")
                            append("\"year\": $year,")
                            append("\"month\": $month,")
                            append("\"day\": $day,")
                            append("\"isCombined\": false,")
                            // title이 빈 문자열
                            append("\"title\": \"\",")
                            append("\"cost\": ${-1000 * day},")
                            append("\"category\": \"dummy\",")
                            append("\"memo\": \"Memo for day $day\",")
                            append("\"vendor\": \"Vendor for day $day\",")
                            append("\"time\": \"${year}-${"%02d".format(month)}-${"%02d".format(day)}T12:00:00Z\",")
                            append("\"paymentMethod\": \"Card\",")
                            append("\"group\": []")
                            append("}]")
                        }
                        13 -> {
                            // 케이스 4: 거래 내역 개수를 1~8개 랜덤
                            val count = Random.nextInt(6, 9) // 1 이상 8 이하
                            append("[")
                            for (i in 1..count) {
                                append("{")
                                append("\"_id\": \"id_day${day}_$i\",")
                                append("\"year\": $year,")
                                append("\"month\": $month,")
                                append("\"day\": $day,")
                                append("\"isCombined\": false,")
                                append("\"title\": \"Transaction $i for day $day\",")
                                append("\"cost\": ${-1000 * day * i},")
                                append("\"category\": \"dummy\",")
                                append("\"memo\": \"Memo $i for day $day\",")
                                append("\"vendor\": \"Vendor $i for day $day\",")
                                append("\"time\": \"${year}-${"%02d".format(month)}-${"%02d".format(day)}T12:00:00Z\",")
                                append("\"paymentMethod\": \"Card\",")
                                append("\"group\": []")
                                append("}")
                                if (i < count) append(",")
                            }
                            append("]")
                        }
                        else -> {
                            // 기본: 한 건의 거래 내역 생성
                            append("[{")
                            append("\"_id\": \"id_day$day\",")
                            append("\"year\": $year,")
                            append("\"month\": $month,")
                            append("\"day\": $day,")
                            append("\"isCombined\": false,")
                            append("\"title\": \"Dummy transaction for day $day\",")
                            append("\"cost\": ${-1000 * day},")
                            append("\"category\": \"dummy\",")
                            append("\"memo\": \"Memo for day $day\",")
                            append("\"vendor\": \"Vendor for day $day\",")
                            append("\"time\": \"${year}-${"%02d".format(month)}-${"%02d".format(day)}T12:00:00Z\",")
                            append("\"paymentMethod\": \"Card\",")
                            append("\"group\": []")
                            append("}]")
                        }
                    }
                } else {
                    append("[]")
                }
                if (day < 31) append(",")
            }
            append("}")
        }

        val jsonString = """
            {
              "monthTotal": 2000000,
              "year": $year,
              "month": $month,
              "daily": $dailyJson,
              "transactions": $transactionsJson
            }
        """.trimIndent()

        val json = Json {
            prettyPrint = true
            ignoreUnknownKeys = true
        }

        val response = json.decodeFromString<MonthlyTransactionResponseDto>(jsonString)
        return Response.success(response)
    }

    override suspend fun postTransaction(request: TransactionRequestDto): Response<TransactionDetailData> {
        TODO("Not yet implemented")
    }



    override suspend fun deleteTransactions(ids: List<String>): Response<Unit> {
        TODO("Not yet implemented")
    }
    override suspend fun patchTransaction(request: TransactionUpdateRequestDto): Response<TransactionDetailData> {
        TODO("Not yet implemented")
    }
}
