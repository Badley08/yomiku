package eu.kanade.presentation.more.settings.screen.about

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import eu.kanade.tachiyomi.BuildConfig
import tachiyomi.i18n.MR
import tachiyomi.i18n.kmk.KMR
import tachiyomi.presentation.core.i18n.stringResource

@Composable
fun WhatsNewDialog(
    onDismissRequest: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = {
            Text(
                text = stringResource(KMR.strings.whats_new_yomiku_title, BuildConfig.VERSION_NAME),
                fontWeight = FontWeight.Bold,
            )
        },
        text = {
            Text(text = stringResource(KMR.strings.whats_new_yomiku_body))
        },
        confirmButton = {
            TextButton(onClick = onDismissRequest) {
                Text(text = stringResource(MR.strings.action_ok))
            }
        },
    )
}

@Preview
@Composable
fun WhatsNewDialogPreview() {
    WhatsNewDialog({})
}
