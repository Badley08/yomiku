package mihon.domain.extension.model

data class ExtensionStore(
    val indexUrl: String,
    val name: String,
    val badgeLabel: String,
    val signingKey: String,
    val contact: Contact,
    val isLegacy: Boolean,
    val extensionListUrl: String?,
) {
    data class Contact(
        val website: String,
        val discord: String?,
    )
}

// URL for extension repository setup guide
const val REPO_HELP = "https://github.com/Badley08/yomiku"

// Signing keys inherited from upstream Komikku (Badley08)
// TODO (Badley08): replace with Yomiku-specific signing keys when the extension store is set up
const val UPSTREAM_KOMIKKU_SIGNATURE = "cbec121aa82ebb02aaa73806992e0368a97d47b5451ed6524816d03084c45905"
const val REPO_SIGNATURE = "9add655a78e96c4ec7a53ef89dccb557cb5d767489fac5e785d671a5a75d4da2"
