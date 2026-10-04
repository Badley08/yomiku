Looking to report an issue/bug or make a feature request? Please refer to the [README file](https://github.com/Badley08/yomiku#issues-feature-requests-and-contributing).

---

Thanks for your interest in contributing to Yomiku!


# Code contributions

Pull requests are welcome!

If you're interested in taking on [an open issue](https://github.com/Badley08/yomiku/issues), please comment on it so others are aware.
You do not need to ask for permission nor an assignment.

## Prerequisites

Before you start, please note that the ability to use following technologies is **required** and that existing contributors will not actively teach them to you.

- Basic [Android development](https://developer.android.com/)
- [Kotlin](https://kotlinlang.org/)

### Tools

- [Android Studio](https://developer.android.com/studio)
- Emulator or phone with developer options enabled to test changes.

## Getting help

<!-- TODO (Badley08): add Yomiku Discord invite link once the server is live -->
- Open an issue on [GitHub](https://github.com/Badley08/yomiku/issues) for help and questions while developing.

# Translations

<!-- TODO (Badley08): set up a Yomiku Weblate project -->
Translations will be managed via Weblate in a future update. For now, please open a pull request with your translation files.


# Forks

Forks are allowed so long as they abide by [the project's LICENSE](https://github.com/Badley08/yomiku/blob/main/LICENSE).

When creating a fork, remember to:

- To avoid confusion with the main app:
    - Change the app name
    - Change the app icon
    - Change or disable the [app update checker](https://github.com/Badley08/yomiku/blob/main/app/src/main/java/eu/kanade/tachiyomi/data/updater/AppUpdateChecker.kt)
- To avoid installation conflicts:
    - Change the `applicationId` in [`build.gradle.kts`](https://github.com/Badley08/yomiku/blob/main/app/build.gradle.kts)
- To avoid having your data polluting the main app's analytics and crash report services:
    - If you want to use Firebase analytics, replace [`google-services.json`](https://github.com/Badley08/yomiku/blob/main/app/src/standard/google-services.json) with your own
    - If you want to use ACRA crash reporting, replace the `ACRA_URI` endpoint in [`build.gradle.kts`](https://github.com/Badley08/yomiku/blob/main/app/build.gradle.kts) with your own


### Supporting Cloud Sync - Google Drive Implementation
1. Go to [Google Cloud Console](https://console.cloud.google.com)
2. Create a new project
3. Go to API & Services -> Library -> Google Drive API and click enable
4. Go to API & Services -> Oauth consent screen
5. Create it, fill in the app name, user support email, and developer contact information
6. In the next screen, click add or remove scopes, and add the `.../auth/drive.appdata` and `.../auth/drive.file` scopes
7. Don't add any test users and go back to the dashboard
8. Click publish
9. Go to API & Services -> Credentials
10. Click Create credentials -> Oauth client ID
11. Select Android, give it a name, and set `eu.kanade.google.oauth` as the package name
12. To get the SHA-1 key, run `keytool -printcert -jarfile app-standard-universal-release.apk` on your apk, and copy the listed SHA-1
13. Expand advanced settings, and enable Custom URL scheme
14. After that just download the json, name it to `client_secrets.json` and put it in `app/src/main/assets/`
