package com.example.mulga.util.extension

import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter

/**
 * ISO 8601 형식의 시간 문자열을 "HH:mm" 형식으로 변환합니다.
 * 예) "2025-03-25T14:45:00Z" -> "14:45"
 */
fun formatTimeToHourMinute(timeString: String): String {
    return try {
        val odt = OffsetDateTime.parse(timeString)
        val formatter = DateTimeFormatter.ofPattern("HH:mm")
        odt.format(formatter)
    } catch (e: Exception) {
        "--:--"
    }
}
