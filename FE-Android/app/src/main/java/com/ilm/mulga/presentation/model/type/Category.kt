package com.ilm.mulga.presentation.model.type

import androidx.compose.ui.graphics.Color
import com.ilm.mulga.R
import com.ilm.mulga.ui.theme.LocalMulGaColors
import com.ilm.mulga.ui.theme.MulGaTheme
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class Category(
    val backendKey: String,
    val iconResId: Int,
    val displayName: String,
    val colorResId: Int,
) {

    @SerialName("education")
    EDUCATION("education", R.drawable.ic_category_education, "교육/학습", R.color.category_education),
    @SerialName("etc")
    ETC("etc", R.drawable.ic_category_etc, "기타", R.color.grey2),
    @SerialName("food")
    FOOD("food", R.drawable.ic_category_food, "식비", R.color.category_food),
    @SerialName("health")
    HEALTH("health", R.drawable.ic_category_health, "의료/건강", R.color.category_health),
    @SerialName("home")
    HOME("home", R.drawable.ic_category_home, "주거/통신", R.color.category_home),
    @SerialName("leisure")
    LEISURE("leisure", R.drawable.ic_category_leisure, "취미/여가", R.color.category_leisure),
    @SerialName("liquor")
    LIQUOR("liquor", R.drawable.ic_category_liquor, "주류/펍", R.color.category_liquor),
    @SerialName("living")
    LIVING("living", R.drawable.ic_category_living, "주거/통신", R.color.category_living),
    @SerialName("pet")
    PET("pet", R.drawable.ic_category_pet, "반려동물", R.color.category_pet),
    @SerialName("shopping")
    SHOPPING("shopping", R.drawable.ic_category_shopping, "쇼핑", R.color.category_shopping),
    @SerialName("subscription")
    SUBSCRIPTION("subscription", R.drawable.ic_category_subscription, "구독", R.color.category_subscription),
    @SerialName("traffic")
    TRAFFIC("traffic", R.drawable.ic_category_traffic, "교통", R.color.category_traffic),
    @SerialName("transfer")
    TRANSFER("transfer", R.drawable.ic_category_transfer, "이체", R.color.category_transfer),
    @SerialName("travel")
    TRAVEL("travel", R.drawable.ic_category_travel, "여행", R.color.category_travel),
    @SerialName("beauty")
    BEAUTY("beauty", R.drawable.ic_category_beauty, "뷰티/미용", R.color.category_beauty),
    @SerialName("cafe")
    CAFE("cafe", R.drawable.ic_category_cafe, "카페/간식", R.color.category_cafe);

    companion object {
        // 백엔드에서 받은 키로 Enum 객체를 찾기 위한 헬퍼 함수
        fun fromBackendKey(key: String): Category? {
            return entries.find { it.backendKey.equals(key, ignoreCase = true) }
        }
    }
}
