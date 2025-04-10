package com.ilm.mulga.util.extension

import kotlin.math.min

class TextSimilarityExtention {
    // 문자열 유사도 계산 (0~1)
    fun calculateSimilarity(s1: String, s2: String): Double {
        val distance = levenshteinDistance(s1, s2)
        val maxLength = maxOf(s1.length, s2.length)
        return if (maxLength == 0) 1.0 else 1.0 - (distance.toDouble() / maxLength)
    }

    // 레벤슈타인 거리 계산 함수
    private fun levenshteinDistance(s1: String, s2: String): Int {
        val m = s1.length
        val n = s2.length

        // s1이 빈 문자열인 경우, s2의 길이를 반환
        if (m == 0) return n

        // s2가 빈 문자열인 경우, s1의 길이를 반환
        if (n == 0) return m

        val dp = Array(m + 1) { IntArray(n + 1) }

        // 첫 행과 열 초기화
        for (i in 0..m) dp[i][0] = i
        for (j in 0..n) dp[0][j] = j

        // DP 테이블 채우기
        for (i in 1..m) {
            for (j in 1..n) {
                val cost = if (s1[i - 1] == s2[j - 1]) 0 else 1
                dp[i][j] = min(
                    min(dp[i - 1][j] + 1, dp[i][j - 1] + 1),
                    dp[i - 1][j - 1] + cost
                )
            }
        }

        return dp[m][n]
    }
}