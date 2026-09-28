package eu.kanade.domain.sync

import eu.kanade.domain.sync.models.SyncSettings
import eu.kanade.tachiyomi.data.sync.SyncManager
import eu.kanade.tachiyomi.data.sync.models.SyncTriggerOptions
import tachiyomi.core.common.preference.Preference
import tachiyomi.core.common.preference.PreferenceStore
import java.util.UUID

class SyncPreferences(
    private val preferenceStore: PreferenceStore,
) {
    fun clientHost() = preferenceStore.getString("connection_sync_client_host", "https://sync.tachiyomi.org")
    fun clientAPIKey() = preferenceStore.getString("connection_sync_client_api_key", "")
    fun lastSyncTimestamp() = preferenceStore.getLong(Preference.appStateKey("last_sync_timestamp"), 0L)

    fun lastSyncEtag() = preferenceStore.getString("sync_etag", "")

    fun syncInterval() = preferenceStore.getInt("sync_interval", 0)
    fun syncService() = preferenceStore.getInt("sync_service", 0)

    // ManLore Sync (Turso DB)
    fun manloreTursoUrl() = preferenceStore.getString("connection_manlore_turso_url", "libsql://manlore-badley08.aws-ap-northeast-1.turso.io")
    fun manloreTursoToken() = preferenceStore.getString("connection_manlore_turso_token", "eyJhbGciOiJFZERTQSIsInR5cCI6IkpXVCJ9.eyJhIjoicnciLCJpYXQiOjE3OTA2MzMwNjgsImlkIjoiMDFhMGVhMGEtZGYwMS03MDViLWEzZWUtMTM2OTFhZjE3NzA3Iiwia2lkIjoiMUFUMUtsM2dHTFJaRnJrdmhVTjRPbEFub3B6Y0lHOWJwWTdQNUtZMnd4dyIsInJpZCI6IjExZWU5MzVlLWYwZDEtNDIxNi1iN2YzLTk2NjY2ODQ3ZWY0NyJ9.wBnJ-ak9GR5BIHL-ppMbRM7w-HQpPw4Gtdmsr657pBjUopgC4b6bZlNIJySQr80a6XW993lkoO7xLdSIr2sfBA")

    fun googleDriveAccessToken() = preferenceStore.getString(
        Preference.appStateKey("connection_google_drive_access_token"),
        "",
    )

    fun googleDriveRefreshToken() = preferenceStore.getString(
        Preference.appStateKey("connection_google_drive_refresh_token"),
        "",
    )

    fun uniqueDeviceID(): String {
        val uniqueIDPreference = preferenceStore.getString(Preference.appStateKey("unique_device_id"), "")

        // Retrieve the current value of the preference
        var uniqueID = uniqueIDPreference.get()
        if (uniqueID.isBlank()) {
            uniqueID = UUID.randomUUID().toString()
            uniqueIDPreference.set(uniqueID)
        }

        return uniqueID
    }

    fun isSyncEnabled(): Boolean {
        return syncService().get() != SyncManager.SyncService.NONE.value
    }

    fun getSyncSettings(): SyncSettings {
        return SyncSettings(
            libraryEntries = preferenceStore.getBoolean("sync_library_entries", true).get(),
            categories = preferenceStore.getBoolean("sync_categories", true).get(),
            chapters = preferenceStore.getBoolean("sync_chapters", true).get(),
            tracking = preferenceStore.getBoolean("sync_tracking", true).get(),
            history = preferenceStore.getBoolean("sync_history", true).get(),
            appSettings = preferenceStore.getBoolean("sync_appSettings", true).get(),
            extensionStores = preferenceStore.getBoolean("sync_extensionStores", true).get(),
            sourceSettings = preferenceStore.getBoolean("sync_sourceSettings", true).get(),
            privateSettings = preferenceStore.getBoolean("sync_privateSettings", true).get(),

            // SY -->
            customInfo = preferenceStore.getBoolean("sync_customInfo", true).get(),
            readEntries = preferenceStore.getBoolean("sync_readEntries", true).get(),
            savedSearchesFeeds = preferenceStore.getBoolean("sync_savedSearchesFeeds", true).get(),
            // SY <--
        )
    }

    fun setSyncSettings(syncSettings: SyncSettings) {
        preferenceStore.getBoolean("sync_library_entries", true).set(syncSettings.libraryEntries)
        preferenceStore.getBoolean("sync_categories", true).set(syncSettings.categories)
        preferenceStore.getBoolean("sync_chapters", true).set(syncSettings.chapters)
        preferenceStore.getBoolean("sync_tracking", true).set(syncSettings.tracking)
        preferenceStore.getBoolean("sync_history", true).set(syncSettings.history)
        preferenceStore.getBoolean("sync_appSettings", true).set(syncSettings.appSettings)
        preferenceStore.getBoolean("sync_extensionStores", true).set(syncSettings.extensionStores)
        preferenceStore.getBoolean("sync_sourceSettings", true).set(syncSettings.sourceSettings)
        preferenceStore.getBoolean("sync_privateSettings", true).set(syncSettings.privateSettings)

        // SY -->
        preferenceStore.getBoolean("sync_customInfo", true).set(syncSettings.customInfo)
        preferenceStore.getBoolean("sync_readEntries", true).set(syncSettings.readEntries)
        preferenceStore.getBoolean("sync_savedSearchesFeeds", true).set(syncSettings.savedSearchesFeeds)
        // SY <--
    }

    fun getSyncTriggerOptions(): SyncTriggerOptions {
        return SyncTriggerOptions(
            syncOnChapterRead = preferenceStore.getBoolean("sync_on_chapter_read", false).get(),
            syncOnChapterOpen = preferenceStore.getBoolean("sync_on_chapter_open", false).get(),
            syncOnAppStart = preferenceStore.getBoolean("sync_on_app_start", false).get(),
            syncOnAppResume = preferenceStore.getBoolean("sync_on_app_resume", false).get(),
        )
    }

    fun setSyncTriggerOptions(syncTriggerOptions: SyncTriggerOptions) {
        preferenceStore.getBoolean("sync_on_chapter_read", false)
            .set(syncTriggerOptions.syncOnChapterRead)
        preferenceStore.getBoolean("sync_on_chapter_open", false)
            .set(syncTriggerOptions.syncOnChapterOpen)
        preferenceStore.getBoolean("sync_on_app_start", false)
            .set(syncTriggerOptions.syncOnAppStart)
        preferenceStore.getBoolean("sync_on_app_resume", false)
            .set(syncTriggerOptions.syncOnAppResume)
    }

    // KMK -->
    fun showSyncingProgressBanner() = preferenceStore.getBoolean(
        Preference.appStateKey("pref_show_syncing_progress_banner_key"),
        true,
    )
    // KMK <--
}
