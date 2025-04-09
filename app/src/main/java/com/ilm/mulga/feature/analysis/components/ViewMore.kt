package com.ilm.mulga.feature.analysis.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.navigation.NavController
import com.ilm.mulga.ui.theme.MulGaTheme

@Composable
fun ViewMore(analysisNavController: NavController) {

    Column(modifier = Modifier.fillMaxWidth()) {
        // Full-width horizontal line
        HorizontalDivider(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 12.dp),
            thickness = 1.dp,
            color = MulGaTheme.colors.grey4
        )

        // "더보기" text with right chevron below
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    analysisNavController.navigate("AnalysisDetailScreen")
                },
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // "더보기" text
            Text(
                text = "더보기", // "More" in Korean
                style = MulGaTheme.typography.caption,
                color = MulGaTheme.colors.grey1
            )

            // Right chevron icon
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight, // Right chevron icon
                contentDescription = "More",
                modifier = Modifier.size(16.dp), // Size of the chevron icon
                tint = MulGaTheme.colors.grey1
            )
        }
    }
}
