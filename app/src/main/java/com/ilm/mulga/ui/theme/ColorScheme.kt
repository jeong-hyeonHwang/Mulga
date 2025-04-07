package com.ilm.mulga.ui.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

@Immutable
data class MulGaColors(
    // Primary Colors
    val primary: Color,      // 기존 PrimaryColor
    val gradient1: Color,    // 기존 Gradient1
    val gradient2: Color,    // 기존 Gradient2
    val red1: Color,          // 기존 Red1

    // Neutral Colors
    val black1: Color,        // 기존 Black1
    val grey1: Color,        // 기존 Grey1
    val grey2: Color,        // 기존 Grey2
    val grey3: Color,        // 기존 Grey3
    val grey4: Color,        // 기존 Grey4
    val grey5: Color,        // 기존 Grey5
    val white1: Color,        // 기존 White1
    val transparent: Color,   // 기존 투명

    // Category Colors
    val categoryBeauty: Color,         // 기존 CategoryBeauty
    val categorySubscription: Color,   // 기존 CategorySubscription
    val categoryLeisure: Color,        // 기존 CategoryLeisure
    val categoryHome: Color,           // 기존 CategoryHome
    val categoryLiquor: Color,         // 기존 CategoryLiquor
    val categoryCafe: Color,           // 기존 CategoryCafe
    val categoryEducation: Color,      // 기존 CategoryEducation
    val categoryTransfer: Color,       // 기존 CategoryTransfer
    val categoryHealth: Color,         // 기존 CategoryHealth
    val categoryLiving: Color,         // 기존 CategoryLiving
    val categoryFood: Color,           // 기존 CategoryFood
    val categoryShopping: Color,       // 기존 CategoryShopping
    val categoryTravel: Color,         // 기존 CategoryTravel
    val categoryTraffic: Color,        // 기존 CategoryTraffic
    val categoryPet: Color,            // 기존 CategoryPet

    val google_border: Color,
    val google_font: Color
)

val defaultMulGaColors = MulGaColors(
    // Primary Colors
    primary = Color(0xFF00A4CD),
    gradient1 = Color(0xFF1FC6E1),
    gradient2 = Color(0xFF2DC0F6),
    red1 = Color(0xFFDC444E),

    // Neutral Colors
    black1 = Color(0xFF171717),
    grey1 = Color(0xFF344A53),
    grey2 = Color(0xFF98AFBA),
    grey3 = Color(0xFFCDDAE0),
    grey4 = Color(0xFFE6ECF0),
    grey5 = Color(0xFFF6F8FB),
    white1 = Color(0xFFFFFFFF),
    transparent = Color(0x00000000),

    // Category Colors
    categoryBeauty = Color(0xFFE81E63),
    categorySubscription = Color(0xFFCD382D),
    categoryLeisure = Color(0xFFFF5722),
    categoryHome = Color(0xFFFF9800),
    categoryLiquor = Color(0xFFF0B400),
    categoryCafe = Color(0xFFD1BB00),
    categoryEducation = Color(0xFFAEBB2D),
    categoryTransfer = Color(0xFF8BC34A),
    categoryHealth = Color(0xFF4CAF50),
    categoryLiving = Color(0xFF009688),
    categoryFood = Color(0xFF00BCD4),
    categoryShopping = Color(0xFF006FFF),
    categoryTravel = Color(0xFF3C2DDB),
    categoryTraffic = Color(0xFF9C27B0),
    categoryPet = Color(0xFF795548),

    google_border = Color(0xFF747775),
    google_font = Color(0xFF1F1F1F)
)

val LocalMulGaColors = staticCompositionLocalOf { defaultMulGaColors }
