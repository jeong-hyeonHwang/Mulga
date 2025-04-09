import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.ilm.mulga.feature.component.dialog.CustomDialogWithCancel
import com.ilm.mulga.R

@Composable
fun DeleteConfirmDialog(
    title: String,
    message: String,
    actionText: String,
    onCancel: () -> Unit,
    onConfirm: () -> Unit
) {
    CustomDialogWithCancel(
        title = title,
        message = message,
        actionText = actionText,
        onCancel = onCancel,
        onConfirm = onConfirm
    )
}
