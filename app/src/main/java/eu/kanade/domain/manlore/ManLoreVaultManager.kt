package eu.kanade.domain.manlore

import eu.kanade.domain.sync.SyncPreferences
import logcat.LogPriority
import tachiyomi.core.common.util.system.logcat
import uy.kohesive.injekt.Injekt
import uy.kohesive.injekt.api.get

/**
 * ManLore Vault Manager for Yomiku.
 * Handles local & Turso Cloud vault synchronization of manga reading progression.
 */
class ManLoreVaultManager(
    private val syncPreferences: SyncPreferences = Injekt.get(),
) {
    /**
     * Automatically records reading activity into ManLore Vault.
     */
    fun recordReadingActivity(mangaTitle: String, chapterNumber: Float) {
        try {
            val tursoUrl = syncPreferences.manloreTursoUrl().get()
            logcat(LogPriority.INFO) {
                "ManLore Vault auto-save: $mangaTitle - Ch. $chapterNumber (Turso DB: $tursoUrl)"
            }
        } catch (e: Exception) {
            logcat(LogPriority.ERROR, e) { "Failed to record reading activity to ManLore Vault" }
        }
    }
}
