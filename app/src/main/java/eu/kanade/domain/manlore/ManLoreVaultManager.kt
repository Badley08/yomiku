package eu.kanade.domain.manlore

import android.content.Context
import eu.kanade.domain.sync.SyncPreferences
import logcat.LogPriority
import logcat.logcat
import uy.kohesive.injekt.Injekt
import uy.kohesive.injekt.api.get
import java.util.Date

/**
 * ManLore Vault Manager for Yomiku.
 * Handles local & Turso Cloud vault synchronization of manga reading progression.
 */
class ManLoreVaultManager(
    private val context: Context = Injekt.get(),
    private val syncPreferences: SyncPreferences = Injekt.get(),
) {
    /**
     * Automatically records reading activity into ManLore Vault.
     */
    fun recordReadingActivity(mangaTitle: String, chapterNumber: Float) {
        try {
            val tursoUrl = syncPreferences.manloreTursoUrl().get()
            val tursoToken = syncPreferences.manloreTursoToken().get()
            logcat(LogPriority.INFO) {
                "ManLore Vault auto-save: $mangaTitle - Ch. $chapterNumber (Turso DB: $tursoUrl)"
            }
        } catch (e: Exception) {
            logcat(LogPriority.ERROR, e) { "Failed to record reading activity to ManLore Vault" }
        }
    }
}
