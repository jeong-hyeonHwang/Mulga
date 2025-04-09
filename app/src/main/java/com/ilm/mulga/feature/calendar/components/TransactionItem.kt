import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.ilm.mulga.R
import com.ilm.mulga.presentation.model.TransactionItemData
import com.ilm.mulga.ui.theme.MulGaTheme
import com.ilm.mulga.util.extension.withCommas

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TransactionItem(
    item: TransactionItemData,
    isDeleteMode: Boolean,
    isSelected: Boolean,
    isCombined: Boolean = false,
    onClick: () -> Unit,
    onLongPress: () -> Unit
) {
    // category가 null이면 아이콘 영역은 LightGray, 있으면 투명 배경
    val iconBackgroundColor = when {
        isSelected -> MulGaTheme.colors.primary
        item.category == null -> MulGaTheme.colors.grey2
        else -> Color.Transparent
    }

    val subTitleText = when {
        isCombined -> stringResource(id = R.string.merge_result)
        else -> item.subtitle
    }

    val subTitleColor = when {
        isCombined -> MulGaTheme.colors.primary
        else -> MulGaTheme.colors.grey2
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .combinedClickable(
                onClick = onClick,
                onLongClick = onLongPress,
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(
                    color = iconBackgroundColor,
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            if (item.category != null && !isSelected) {
                androidx.compose.foundation.Image(
                    painter = painterResource(id = item.category.iconResId),
                    contentDescription = null,
                    modifier = Modifier.size(40.dp)
                )
            }
            if (isDeleteMode && isSelected) {
                androidx.compose.foundation.Image(
                    painter = painterResource(id = R.drawable.ic_util_check),
                    colorFilter = ColorFilter.tint(MulGaTheme.colors.white1),
                    contentDescription = "선택됨",
                    modifier = Modifier.size(20.dp)
                )
            }
        }

        Column(
            modifier = Modifier
                .weight(0.65f)
                .padding(horizontal = 8.dp)
        ) {
            Text(
                text = item.title,
                color = MulGaTheme.colors.black1,
                style = MulGaTheme.typography.bodySmall
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = subTitleText,
                color = subTitleColor,
                style = MulGaTheme.typography.caption
            )
        }

        Column(
            modifier = Modifier.weight(0.25f),
            horizontalAlignment = Alignment.End,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                stringResource(id = R.string.budget_value_unit, item.price.withCommas()),
                color = MulGaTheme.colors.black1,
                style = MulGaTheme.typography.bodySmall
            )
            Text(
                text = item.time,
                color = MulGaTheme.colors.grey2,
                style = MulGaTheme.typography.caption
            )
        }
    }
}