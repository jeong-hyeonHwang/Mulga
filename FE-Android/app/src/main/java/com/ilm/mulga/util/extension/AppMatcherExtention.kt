package com.ilm.mulga.util.extension

import com.ilm.mulga.domain.model.AppInfoEntity

class AppMatcherExtention(private val appsList: List<AppInfoEntity>) {

    private val textSimilarity = TextSimilarityExtention()

    // 가장 유사한 앱 찾기
    fun findMostSimilarApp(query: String): AppInfoEntity? {
        if (appsList.isEmpty()) return null

        var bestMatch: AppInfoEntity = appsList[0]
        var highestSimilarity = textSimilarity.calculateSimilarity(
            query.lowercase(),
            appsList[0].appName.lowercase()
        )

        for (app in appsList) {
            val similarity = textSimilarity.calculateSimilarity(
                query.lowercase(),
                app.appName.lowercase()
            )
            if (similarity > highestSimilarity) {
                highestSimilarity = similarity
                bestMatch = app
            }
        }

        // 유사도가 너무 낮으면 null 반환
        return if (highestSimilarity > 0.3) bestMatch else null
    }
}