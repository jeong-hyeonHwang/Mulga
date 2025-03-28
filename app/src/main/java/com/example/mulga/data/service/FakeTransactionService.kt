package com.example.mulga.data.service

import com.example.mulga.data.dto.response.MonthlyTransactionResponseDto
import com.google.gson.Gson
import kotlinx.serialization.json.Json
import retrofit2.Response

class FakeTransactionService : TransactionService {
    override suspend fun getMonthlyTransactions(year: Int, month: Int): Response<MonthlyTransactionResponseDto> {
        val jsonString = """
            {
              "monthTotal": 2000000,
              "year": 2025,
              "month": 3,
              "daily": {
                "31": { "isValid": true, "income": 2100, "expense": -7500 },
                "30": { "isValid": true, "income": 1900, "expense": -7200 },
                "29": { "isValid": true, "income": 1700, "expense": -7000 },
                "28": { "isValid": true, "income": 1500, "expense": -6700 },
                "27": { "isValid": true, "income": 2000, "expense": -6500 },
                "26": { "isValid": true, "income": 1800, "expense": -6300 },
                "25": { "isValid": true, "income": 1600, "expense": -6100 },
                "24": { "isValid": true, "income": 1300, "expense": -5900 },
                "23": { "isValid": true, "income": 1700, "expense": -5600 },
                "22": { "isValid": true, "income": 1500, "expense": -5700 },
                "21": { "isValid": true, "income": 1900, "expense": -5800 },
                "20": { "isValid": true, "income": 1200, "expense": -6000 },
                "19": { "isValid": true, "income": 2300, "expense": -5500 },
                "18": { "isValid": true, "income": 2100, "expense": -5300 },
                "17": { "isValid": true, "income": 1400, "expense": -4900 },
                "16": { "isValid": true, "income": 1800, "expense": -4700 },
                "15": { "isValid": true, "income": 2000, "expense": -5200 },
                "14": { "isValid": true, "income": 1900, "expense": -5000 },
                "13": { "isValid": true, "income": 1500, "expense": -4800 },
                "12": { "isValid": true, "income": 1700, "expense": -4200 },
                "11": { "isValid": true, "income": 1600, "expense": -4000 },
                "10": { "isValid": true, "income": 1400, "expense": -5500 },
                "9": { "isValid": true, "income": 1100, "expense": -4500 },
                "8": { "isValid": true, "income": 1300, "expense": -5000 },
                "7": { "isValid": true, "income": 2200, "expense": -6000 },
                "6": { "isValid": true, "income": 1000, "expense": -3500 },
                "5": { "isValid": true, "income": 1800, "expense": -4000 },
                "4": { "isValid": true, "income": 1200, "expense": -7000 },
                "3": { "isValid": true, "income": 1500, "expense": -3000 },
                "2": { "isValid": true, "income": 2000, "expense": -4500 },
                "1": { "isValid": true, "income": 1000, "expense": -5000 }
              },
              "transactions": {
                "31": [
                  {
                    "_id": "id_day31",
                    "year": 2025,
                    "month": 3,
                    "day": 31,
                    "isCombined": false,
                    "title": "월말 정산",
                    "cost": -7500,
                    "category": "expense",
                    "memo": "월말 정산 내역",
                    "vendor": "은행",
                    "time": "2025-03-31T23:59:00Z",
                    "paymentMethod": "카드",
                    "group": []
                  }
                ],
                "30": [],
                "29": [
                  {
                    "_id": "id_day29",
                    "year": 2025,
                    "month": 3,
                    "day": 29,
                    "isCombined": false,
                    "title": "점심 식사",
                    "cost": -7000,
                    "category": "food",
                    "memo": "식당 R에서 점심",
                    "vendor": "식당 R",
                    "time": "2025-03-29T12:00:00Z",
                    "paymentMethod": "카드",
                    "group": []
                  }
                ],
                "28": [
                  {
                    "_id": "id_day28",
                    "year": 2025,
                    "month": 3,
                    "day": 28,
                    "isCombined": false,
                    "title": "식사",
                    "cost": -6700,
                    "category": "food",
                    "memo": "식당 Q에서 저녁",
                    "vendor": "식당 Q",
                    "time": "2025-03-28T19:00:00Z",
                    "paymentMethod": "카드",
                    "group": []
                  }
                ],
                "27": [
                  {
                    "_id": "id_day27",
                    "year": 2025,
                    "month": 3,
                    "day": 27,
                    "isCombined": false,
                    "title": "교통비",
                    "cost": -6500,
                    "category": "transport",
                    "memo": "택시 이용",
                    "vendor": "택시회사",
                    "time": "2025-03-27T09:00:00Z",
                    "paymentMethod": "카드",
                    "group": []
                  }
                ],
                "26": [],
                "25": [
                  {
                    "_id": "id_day25",
                    "year": 2025,
                    "month": 3,
                    "day": 25,
                    "isCombined": false,
                    "title": "식사",
                    "cost": -6100,
                    "category": "food",
                    "memo": "식당 P에서 점심",
                    "vendor": "식당 P",
                    "time": "2025-03-25T12:00:00Z",
                    "paymentMethod": "카드",
                    "group": []
                  }
                ],
                "24": [
                  {
                    "_id": "id_day24",
                    "year": 2025,
                    "month": 3,
                    "day": 24,
                    "isCombined": false,
                    "title": "저녁 식사",
                    "cost": -5900,
                    "category": "food",
                    "memo": "식당 O에서 저녁",
                    "vendor": "식당 O",
                    "time": "2025-03-24T19:00:00Z",
                    "paymentMethod": "카드",
                    "group": []
                  }
                ],
                "23": [],
                "22": [
                  {
                    "_id": "id_day22",
                    "year": 2025,
                    "month": 3,
                    "day": 22,
                    "isCombined": false,
                    "title": "식사",
                    "cost": -5700,
                    "category": "food",
                    "memo": "식당 M에서 점심",
                    "vendor": "식당 M",
                    "time": "2025-03-22T12:00:00Z",
                    "paymentMethod": "카드",
                    "group": []
                  },
                  {
                    "_id": "id_day22",
                    "year": 2025,
                    "month": 3,
                    "day": 22,
                    "isCombined": false,
                    "title": "식사",
                    "cost": -5700,
                    "category": "food",
                    "memo": "식당 M에서 점심",
                    "vendor": "식당 M",
                    "time": "2025-03-22T12:00:00Z",
                    "paymentMethod": "카드",
                    "group": []
                  },
                  {
                    "_id": "id_day22",
                    "year": 2025,
                    "month": 3,
                    "day": 22,
                    "isCombined": false,
                    "title": "식사",
                    "cost": -5700,
                    "category": "food",
                    "memo": "식당 M에서 점심",
                    "vendor": "식당 M",
                    "time": "2025-03-22T12:00:00Z",
                    "paymentMethod": "카드",
                    "group": []
                  },
                  {
                    "_id": "id_day22",
                    "year": 2025,
                    "month": 3,
                    "day": 22,
                    "isCombined": false,
                    "title": "식사",
                    "cost": -5700,
                    "category": "food",
                    "memo": "식당 M에서 점심",
                    "vendor": "식당 M",
                    "time": "2025-03-22T12:00:00Z",
                    "paymentMethod": "카드",
                    "group": []
                  }
                  
                ],
                "21": [
                  {
                    "_id": "id_day21",
                    "year": 2025,
                    "month": 3,
                    "day": 21,
                    "isCombined": false,
                    "title": "간식",
                    "cost": -5800,
                    "category": "snack",
                    "memo": "편의점 간식",
                    "vendor": "편의점",
                    "time": "2025-03-21T16:00:00Z",
                    "paymentMethod": "현금",
                    "group": []
                  }
                ],
                "20": [],
                "19": [
                  {
                    "_id": "id_day19",
                    "year": 2025,
                    "month": 3,
                    "day": 19,
                    "isCombined": false,
                    "title": "저녁 식사",
                    "cost": -5500,
                    "category": "food",
                    "memo": "식당 L에서 저녁",
                    "vendor": "식당 L",
                    "time": "2025-03-19T19:00:00Z",
                    "paymentMethod": "카드",
                    "group": []
                  }
                ],
                "18": [
                  {
                    "_id": "id_day18",
                    "year": 2025,
                    "month": 3,
                    "day": 18,
                    "isCombined": false,
                    "title": "점심 식사",
                    "cost": -5300,
                    "category": "food",
                    "memo": "식당 K에서 점심",
                    "vendor": "식당 K",
                    "time": "2025-03-18T12:00:00Z",
                    "paymentMethod": "카드",
                    "group": []
                  }
                ],
                "17": [
                  {
                    "_id": "id_day17",
                    "year": 2025,
                    "month": 3,
                    "day": 17,
                    "isCombined": false,
                    "title": "식사",
                    "cost": -4900,
                    "category": "food",
                    "memo": "식당 J에서 점심",
                    "vendor": "식당 J",
                    "time": "2025-03-17T12:00:00Z",
                    "paymentMethod": "카드",
                    "group": []
                  }
                ],
                "16": [
                  {
                    "_id": "id_day16",
                    "year": 2025,
                    "month": 3,
                    "day": 16,
                    "isCombined": false,
                    "title": "교통비",
                    "cost": -4700,
                    "category": "transport",
                    "memo": "버스 요금",
                    "vendor": "버스회사",
                    "time": "2025-03-16T08:30:00Z",
                    "paymentMethod": "카드",
                    "group": []
                  }
                ],
                "15": [
                  {
                    "_id": "id_day15",
                    "year": 2025,
                    "month": 3,
                    "day": 15,
                    "isCombined": false,
                    "title": "간식",
                    "cost": -5200,
                    "category": "snack",
                    "memo": "편의점 간식",
                    "vendor": "편의점",
                    "time": "2025-03-15T15:30:00Z",
                    "paymentMethod": "현금",
                    "group": []
                  }
                ],
                "14": [
                  {
                    "_id": "id_day14",
                    "year": 2025,
                    "month": 3,
                    "day": 14,
                    "isCombined": false,
                    "title": "저녁 식사",
                    "cost": -5000,
                    "category": "food",
                    "memo": "식당 I에서 저녁",
                    "vendor": "식당 I",
                    "time": "2025-03-14T19:00:00Z",
                    "paymentMethod": "카드",
                    "group": []
                  }
                ],
                "13": [
                  {
                    "_id": "id_day13",
                    "year": 2025,
                    "month": 3,
                    "day": 13,
                    "isCombined": false,
                    "title": "점심 식사",
                    "cost": -4800,
                    "category": "food",
                    "memo": "식당 H에서 점심",
                    "vendor": "식당 H",
                    "time": "2025-03-13T12:15:00Z",
                    "paymentMethod": "카드",
                    "group": []
                  }
                ],
                "12": [
                  {
                    "_id": "id_day12",
                    "year": 2025,
                    "month": 3,
                    "day": 12,
                    "isCombined": false,
                    "title": "식사",
                    "cost": -4200,
                    "category": "food",
                    "memo": "식당 G에서 점심",
                    "vendor": "식당 G",
                    "time": "2025-03-12T12:00:00Z",
                    "paymentMethod": "카드",
                    "group": []
                  }
                ],
                "11": [
                  {
                    "_id": "id_day11",
                    "year": 2025,
                    "month": 3,
                    "day": 11,
                    "isCombined": false,
                    "title": "간식",
                    "cost": -4000,
                    "category": "snack",
                    "memo": "편의점 간식",
                    "vendor": "편의점",
                    "time": "2025-03-11T16:00:00Z",
                    "paymentMethod": "현금",
                    "group": []
                  }
                ],
                "10": [
                  {
                    "_id": "id_day10",
                    "year": 2025,
                    "month": 3,
                    "day": 10,
                    "isCombined": false,
                    "title": "저녁 식사",
                    "cost": -5500,
                    "category": "food",
                    "memo": "식당 F에서 저녁",
                    "vendor": "식당 F",
                    "time": "2025-03-10T19:30:00Z",
                    "paymentMethod": "카드",
                    "group": []
                  }
                ],
                "9": [
                  {
                    "_id": "id_day9",
                    "year": 2025,
                    "month": 3,
                    "day": 9,
                    "isCombined": false,
                    "title": "점심 식사",
                    "cost": -4500,
                    "category": "food",
                    "memo": "식당 E에서 점심",
                    "vendor": "식당 E",
                    "time": "2025-03-09T12:30:00Z",
                    "paymentMethod": "카드",
                    "group": []
                  }
                ],
                "8": [
                  {
                    "_id": "id_day8",
                    "year": 2025,
                    "month": 3,
                    "day": 8,
                    "isCombined": false,
                    "title": "카페 방문",
                    "cost": -5000,
                    "category": "cafe",
                    "memo": "카페 C에서 커피",
                    "vendor": "카페 C",
                    "time": "2025-03-08T10:00:00Z",
                    "paymentMethod": "현금",
                    "group": []
                  }
                ],
                "7": [
                  {
                    "_id": "id_day7",
                    "year": 2025,
                    "month": 3,
                    "day": 7,
                    "isCombined": false,
                    "title": "쇼핑",
                    "cost": -6000,
                    "category": "shopping",
                    "memo": "의류 쇼핑",
                    "vendor": "쇼핑몰",
                    "time": "2025-03-07T14:00:00Z",
                    "paymentMethod": "카드",
                    "group": []
                  }
                ],
                "6": [
                  {
                    "_id": "id_day6",
                    "year": 2025,
                    "month": 3,
                    "day": 6,
                    "isCombined": false,
                    "title": "교통비",
                    "cost": -3500,
                    "category": "transport",
                    "memo": "버스 요금",
                    "vendor": "버스회사",
                    "time": "2025-03-06T08:30:00Z",
                    "paymentMethod": "카드",
                    "group": []
                  }
                ],
                "5": [
                  {
                    "_id": "id_day5",
                    "year": 2025,
                    "month": 3,
                    "day": 5,
                    "isCombined": false,
                    "title": "간식",
                    "cost": -4000,
                    "category": "snack",
                    "memo": "편의점 간식",
                    "vendor": "편의점",
                    "time": "2025-03-05T15:00:00Z",
                    "paymentMethod": "현금",
                    "group": []
                  }
                ],
                "4": [],
                "3": [
                  {
                    "_id": "id_day3",
                    "year": 2025,
                    "month": 3,
                    "day": 3,
                    "isCombined": false,
                    "title": "",
                    "cost": -3000,
                    "category": "food",
                    "memo": "식당 C에서 점심",
                    "vendor": "식당 C",
                    "time": "2025-03-03T12:00:00Z",
                    "paymentMethod": "카드",
                    "group": []
                  }
                ],
                "2": [
                  {
                    "_id": "id_day2",
                    "year": 2025,
                    "month": 3,
                    "day": 2,
                    "isCombined": false,
                    "title": "커피 한잔",
                    "cost": -4500,
                    "category": "cafe",
                    "memo": "카페 B에서 커피",
                    "vendor": "",
                    "time": "2025-03-02T09:30:00Z",
                    "paymentMethod": "현금",
                    "group": []
                  }
                ],
                "1": [
                  {
                    "_id": "id_day1",
                    "year": 2025,
                    "month": 3,
                    "day": 1,
                    "isCombined": false,
                    "title": "아침 식사",
                    "cost": -5000,
                    "category": "food",
                    "memo": "식당 A에서 아침 식사",
                    "vendor": "식당 A",
                    "time": "2025-03-01T08:00:00Z",
                    "paymentMethod": "카드",
                    "group": []
                  }
                ]
              }
            }

        """.trimIndent()

        val json = Json {
            prettyPrint = true
            ignoreUnknownKeys = true
        }

        // JSON 문자열을 MonthlyResponse 객체로 디코딩
        val response = json.decodeFromString<MonthlyTransactionResponseDto>(jsonString)

        // 성공 응답으로 감싸서 반환
        return Response.success(response)
    }
}
