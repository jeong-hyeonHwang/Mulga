import androidx.compose.runtime.Composable
import com.ilm.mulga.feature.component.dialog.CustomDestructiveDialogWithCancel

@Composable
fun DeleteConfirmDialog(
    title: String,
    message: String,
    actionText: String,
    onCancel: () -> Unit,
    onConfirm: () -> Unit
) {
    CustomDestructiveDialogWithCancel(
        title = title,
        message = message,
        actionText = actionText,
        onCancel = onCancel,
        onConfirm = onConfirm
    )
}
