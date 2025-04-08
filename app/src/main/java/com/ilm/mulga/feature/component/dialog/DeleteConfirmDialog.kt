import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.ilm.mulga.feature.component.dialog.CustomDialogWithCancel
import com.ilm.mulga.R

@Composable
fun DeleteConfirmDialog(
    dataCount: Int,
    onCancel: () -> Unit,
    onConfirm: () -> Unit
) {
    CustomDialogWithCancel(
        title = stringResource(id = R.string.dialog_delete_confirmation_title),
        message = stringResource(id = R.string.dialog_delete_confirmation_message, dataCount),
        onCancel = onCancel,
        onConfirm = onConfirm
    )
}
