package mihon.core.migration.migrations

import logcat.LogPriority
import mihon.core.migration.Migration
import mihon.core.migration.MigrationContext
import mihon.domain.extension.repository.ExtensionStoreRepository
import tachiyomi.core.common.util.lang.withIOContext
import tachiyomi.core.common.util.system.logcat

class BuiltinExtensionStoreMigration : Migration {
    override val version: Float = 82f

    override suspend fun invoke(migrationContext: MigrationContext): Boolean = withIOContext {
        val repository = migrationContext.get<ExtensionStoreRepository>() ?: return@withIOContext false
        try {
            repository.insertFromPreference(
                indexUrl = "https://github.com/keiyoushi/extensions/raw/repo/index.pb",
                name = "Keiyoushi",
            )
            repository.refreshAll()
        } catch (e: Exception) {
            logcat(LogPriority.ERROR, e) { "Failed to insert built-in Keiyoushi store" }
        }
        return@withIOContext true
    }
}
