package com.ilm.mulga.util.extension

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter

/**
 * ISO 8601 형식의 시간 문자열을 "HH:mm" 형식으로 변환합니다.
 * 예) "2025-03-25T14:45:00Z" -> "14:45"
 */
fun formatTimeToHourMinute(dateTime: LocalDateTime): String {
    return try {
        val formatter = DateTimeFormatter.ofPattern("HH:mm")
        dateTime.format(formatter)
    } catch (e: Exception) {
        "--:--"
    }
}

fun formatTimeToHourMinuteForMain(dateTime: LocalDateTime): String {
    return try {
        val today = LocalDate.now()
        val formatter = if (dateTime.toLocalDate() == today) {
            DateTimeFormatter.ofPattern("HH:mm")
        } else {
            DateTimeFormatter.ofPattern("yy.MM.dd HH:mm")
        }
        dateTime.format(formatter)
    } catch (e: Exception) {
        "--:--"
    }
}