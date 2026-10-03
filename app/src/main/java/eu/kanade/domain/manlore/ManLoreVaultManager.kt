package eu.kanade.domain.manlore

import android.app.Application
import android.content.Context
import eu.kanade.domain.sync.SyncPreferences
import logcat.LogPriority
import org.json.JSONArray
import org.json.JSONObject
import tachiyomi.core.common.util.system.logcat
import uy.kohesive.injekt.Injekt
import uy.kohesive.injekt.api.get
import java.io.File

/**
 * ManLore Vault Manager for Yomiku.
 * Directly records title, description, cover, and genres from Yomiku UI/UX
 * into the ManLore Vault without external AniList/tracker fetches.
 */
class ManLoreVaultManager(
    private val syncPreferences: SyncPreferences = Injekt.get(),
    private val context: Context = Injekt.get<Application>(),
) {
    private val vaultFile = File(context.filesDir, "manlore_vault_entries.json")

    /**
     * Automatically records reading activity and full metadata directly from UI/UX into ManLore Vault.
     */
    fun recordReadingActivity(
        mangaTitle: String,
        chapterNumber: Float,
        readDurationMs: Long = 0L,
        description: String? = null,
        coverUrl: String? = null,
        genres: List<String>? = null,
        author: String? = null,
        artist: String? = null,
    ) {
        try {
            val entries = loadEntries()
            var existingIndex: Int? = null
            for (i in 0 until entries.length()) {
                val item = entries.getJSONObject(i)
                if (item.optString("title").equals(mangaTitle, ignoreCase = true)) {
                    existingIndex = i
                    break
                }
            }

            val item = if (existingIndex != null) {
                entries.getJSONObject(existingIndex)
            } else {
                JSONObject().apply {
                    put("id", "yomiku_" + System.currentTimeMillis())
                    put("type", "manga")
                    put("status", "reading")
                    put("totalReadDurationMs", 0L)
                }
            }

            item.put("title", mangaTitle)
            item.put("chapters", chapterNumber.toInt())
            item.put("chapterFloat", chapterNumber.toDouble())

            // Accumulate read time — never overwrite, only add the current session
            if (readDurationMs > 0L) {
                val previous = item.optLong("totalReadDurationMs", 0L)
                val accumulated = previous + readDurationMs
                item.put("totalReadDurationMs", accumulated)
                // Human-readable helpers (minutes and hours) for ManLore WebView display
                item.put("totalReadMinutes", accumulated / 60_000)
                item.put("totalReadHours", accumulated / 3_600_000)
            }

            if (!description.isNullOrBlank()) {
                item.put("description", description)
                item.put("notes", description)
            }
            if (!coverUrl.isNullOrBlank()) {
                item.put("image", coverUrl)
                item.put("imageUrl", coverUrl)
            }
            if (!author.isNullOrBlank()) {
                item.put("author", author)
            }
            if (!artist.isNullOrBlank()) {
                item.put("artist", artist)
            }
            if (!genres.isNullOrEmpty()) {
                item.put("genres", JSONArray(genres))
            }
            item.put("updatedAt", System.currentTimeMillis())

            if (existingIndex != null) {
                entries.put(existingIndex, item)
            } else {
                entries.put(item)
            }

            vaultFile.writeText(entries.toString())

            val tursoUrl = syncPreferences.manloreTursoUrl().get()
            logcat(LogPriority.INFO) {
                val totalMin = item.optLong("totalReadMinutes", 0L)
                "ManLore Vault: $mangaTitle Ch.$chapterNumber | session +${readDurationMs/1000}s | total ${totalMin}min | Turso=$tursoUrl"
            }
        } catch (e: Exception) {
            logcat(LogPriority.ERROR, e) { "Failed to record reading activity to ManLore Vault" }
        }
    }

    /**
     * Exposes all saved UI/UX manga entries as a JSON string for the ManLore WebView.
     */
    fun getVaultEntriesAsJson(): String {
        return try {
            if (vaultFile.exists()) vaultFile.readText() else "[]"
        } catch (_: Exception) {
            "[]"
        }
    }

    private fun loadEntries(): JSONArray {
        return try {
            if (vaultFile.exists()) {
                JSONArray(vaultFile.readText())
            } else {
                JSONArray()
            }
        } catch (_: Exception) {
            JSONArray()
        }
    }
}
