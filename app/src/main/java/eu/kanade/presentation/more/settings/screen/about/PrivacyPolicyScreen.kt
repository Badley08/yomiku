package eu.kanade.presentation.more.settings.screen.about

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import eu.kanade.presentation.components.AppBar
import eu.kanade.presentation.util.Screen
import tachiyomi.i18n.MR
import tachiyomi.presentation.core.components.material.Scaffold
import tachiyomi.presentation.core.components.material.padding
import tachiyomi.presentation.core.i18n.stringResource

class PrivacyPolicyScreen : Screen() {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val scrollState = rememberScrollState()

        Scaffold(
            topBar = { scrollBehavior ->
                AppBar(
                    title = stringResource(MR.strings.privacy_policy),
                    navigateUp = navigator::pop,
                    scrollBehavior = scrollBehavior,
                )
            },
        ) { contentPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
                    .padding(contentPadding)
                    .padding(horizontal = 20.dp, vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                Text(
                    text = "Yomiku Privacy Policy",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                )

                Text(
                    text = "Last updated: October 2026",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )

                SectionHeader(title = "1. Overview & Privacy Principles")
                Text(
                    text = "Yomiku is an open-source, privacy-first manga and comic reader application that operates on a local-first architecture. We firmly believe that your reading habits, library collection, and personal data belong exclusively to you. Yomiku does not sell, rent, or trade your personal information.",
                    style = MaterialTheme.typography.bodyMedium,
                )

                SectionHeader(title = "2. Data Stored Locally on Your Device")
                Text(
                    text = "By default, the vast majority of application data is stored solely on your local Android device, including:\n" +
                        "• Manga Library, downloaded chapters, and local categories.\n" +
                        "• Reading history, chapter progress, and last-read timestamps.\n" +
                        "• ManLore Vault reading session time, XP calculations, and quest achievements.\n" +
                        "• Application preferences and custom reader layout configurations.\n\n" +
                        "This data never leaves your device unless you explicitly enable sync or perform a manual backup.",
                    style = MaterialTheme.typography.bodyMedium,
                )

                SectionHeader(title = "3. Third-Party Extensions & Source Access")
                Text(
                    text = "Yomiku allows users to install community extensions (such as Keiyoushi). When browsing or reading through an extension, your device connects directly to the respective content provider's servers. Yomiku does not act as an intermediary or proxy for your browsing traffic.",
                    style = MaterialTheme.typography.bodyMedium,
                )

                SectionHeader(title = "4. Optional Cloud Synchronization & Analytics")
                Text(
                    text = "If you opt into ManLore Cloud Sync (powered by Firebase), your account credentials (email and hashed password) and catalog progress will be securely transmitted to Firebase servers. You have complete control to disconnect, delete your session, or run entirely in local Guest mode at any time.\n\n" +
                        "Crash reporting and diagnostic telemetry are strictly anonymized and can be toggled on or off under Security & Privacy settings.",
                    style = MaterialTheme.typography.bodyMedium,
                )

                SectionHeader(title = "5. Your Data Control & Deletion")
                Text(
                    text = "You maintain complete ownership over your data. You may export full backups in JSON format at any time, clear application caches, or completely reset your local database via Settings > Data and Storage.",
                    style = MaterialTheme.typography.bodyMedium,
                )

                SectionHeader(title = "6. Contact & Source Code")
                Text(
                    text = "Yomiku is fully transparent and open source. For questions, bug reports, or source inspections, visit our repository at https://github.com/Badley08/yomiku.",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(bottom = 24.dp),
                )
            }
        }
    }

    @Composable
    private fun SectionHeader(title: String) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(top = 8.dp),
        )
    }
}
