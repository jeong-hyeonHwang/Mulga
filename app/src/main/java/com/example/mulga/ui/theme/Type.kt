package com.example.mulga.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.mulga.R

// Sokcho Bada Dotum 폰트 정의 (정규체)
val SokchoBadaDotum = FontFamily(
    Font(R.font.sokcho_bada_dotum, FontWeight.Normal)
)

// Pretendard 폰트 정의 (여러 굵기 지원)
val Pretendard = FontFamily(
    Font(R.font.pretendard_regular, FontWeight.Normal),
    Font(R.font.pretendard_medium, FontWeight.Medium),
    Font(R.font.pretendard_semi_bold, FontWeight.SemiBold),
    Font(R.font.pretendard_bold, FontWeight.Bold)
)

// MulGaTypography 데이터 클래스 정의
@Immutable
data class MulGaTypography(
    val appTitle: TextStyle,
    val appSubTitle: TextStyle,
    val display: TextStyle,
    val headline: TextStyle,
    val title: TextStyle,
    val subtitle: TextStyle,
    val bodyLarge: TextStyle,
    val bodyMedium: TextStyle,
    val bodySmall: TextStyle,
    val caption: TextStyle,
    val label: TextStyle
)


// Material3 Typography 커스터마이징 (Pretendard 적용)
val defaultMulGaTypography = MulGaTypography(
    // App Title: Sokcho Bada Dotum Regular 40
    appTitle = TextStyle(
        fontFamily = SokchoBadaDotum,
        fontWeight = FontWeight.Normal,
        fontSize = 40.sp
    ),
    // App Sub Title: Sokcho Bada Dotum Regular 16
    appSubTitle = TextStyle(
        fontFamily = SokchoBadaDotum,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp
    ),
    // Display: Pretendard Bold 40
    display = TextStyle(
        fontFamily = Pretendard,
        fontWeight = FontWeight.Bold,
        fontSize = 40.sp
    ),
    // Headline: Pretendard Bold 32
    headline = TextStyle(
        fontFamily = Pretendard,
        fontWeight = FontWeight.Bold,
        fontSize = 32.sp
    ),
    // Title: Pretendard Semibold 24
    title = TextStyle(
        fontFamily = Pretendard,
        fontWeight = FontWeight.SemiBold,
        fontSize = 24.sp
    ),
    // Subtitle: Pretendard Medium 20
    subtitle = TextStyle(
        fontFamily = Pretendard,
        fontWeight = FontWeight.Medium,
        fontSize = 20.sp
    ),
    // Body Large: Pretendard Medium 18
    bodyLarge = TextStyle(
        fontFamily = Pretendard,
        fontWeight = FontWeight.Medium,
        fontSize = 18.sp
    ),
    // Body Medium: Pretendard Medium 16
    bodyMedium = TextStyle(
        fontFamily = Pretendard,
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp
    ),
    // Body Small: Pretendard Semibold 14
    bodySmall = TextStyle(
        fontFamily = Pretendard,
        fontWeight = FontWeight.SemiBold,
        fontSize = 14.sp
    ),
    // Caption: Pretendard Regular 12
    caption = TextStyle(
        fontFamily = Pretendard,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp
    ),
    // Label: Pretendard Medium 8
    label = TextStyle(
        fontFamily = Pretendard,
        fontWeight = FontWeight.Medium,
        fontSize = 8.sp
    )
)

val LocalMulGaTypography = staticCompositionLocalOf { defaultMulGaTypography }