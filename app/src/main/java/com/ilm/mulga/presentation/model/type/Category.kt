package com.ilm.mulga.presentation.model.type

import com.ilm.mulga.R
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class Category(
    val backendKey: String,
    val iconResId: Int,
    val displayName: String
) {
    @SerialName("education")
    EDUCATION("education", R.drawable.ic_category_education, "교육/학습"),
    @SerialName("etc")
    ETC("etc", R.drawable.ic_category_etc, "기타"),
    @SerialName("food")
    FOOD("food", R.drawable.ic_category_food, "식비"),
    @SerialName("health")
    HEALTH("health", R.drawable.ic_category_health, "의료/건강"),
    @SerialName("home")
    HOME("home", R.drawable.ic_category_home, "주거/통신"),
    @SerialName("leisure")
    LEISURE("leisure", R.drawable.ic_category_leisure, "취미/여가"),
    @SerialName("liquor")
    LIQUOR("liquor", R.drawable.ic_category_liquor, "주류/펍"),
    @SerialName("living")
    LIVING("living", R.drawable.ic_category_living, "주거/통신"),
    @SerialName("pet")
    PET("pet", R.drawable.ic_category_pet, "반려동물"),
    @SerialName("shopping")
    SHOPPING("shopping", R.drawable.ic_category_shopping, "쇼핑"),
    @SerialName("subscription")
    SUBSCRIPTION("subscription", R.drawable.ic_category_subscription, "구독"),
    @SerialName("traffic")
    TRAFFIC("traffic", R.drawable.ic_category_traffic, "교통"),
    @SerialName("transfer")
    TRANSFER("transfer", R.drawable.ic_category_transfer, "이체"),
    @SerialName("travel")
    TRAVEL("travel", R.drawable.ic_category_travel, "여행"),
    @SerialName("beauty")
    BEAUTY("beauty", R.drawable.ic_category_beauty, "뷰티/미용"),
    @SerialName("cafe")
    CAFE("cafe", R.drawable.ic_category_cafe, "카페/간식");

    companion object {
        // 백엔드에서 받은 키로 Enum 객체를 찾기 위한 헬퍼 함수
        fun fromBackendKey(key: String): Category? {
            return values().find { it.backendKey.equals(key, ignoreCase = true) }
        }
    }
}
