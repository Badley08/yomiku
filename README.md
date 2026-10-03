<div align="center">

<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 432 432" width="96" height="96">
  <circle cx="216" cy="216" r="180" fill="#0F172A"/>
  <circle cx="216" cy="216" r="160" fill="#74B9FF" opacity="0.18"/>
  <path fill="#FFFFFF" d="M204.93 256.56 205.15 264.47C205.15 279.61 199.96 285.93 185.50 285.93C169.68 285.93 158.16 281.87 158.16 270.79C158.16 261.08 168.78 254.98 186.40 254.98C192.73 254.98 199.05 255.66 204.93 256.56ZM233.85 118.50H199.73C201.09 124.15 201.76 133.87 202.22 144.94C202.44 154.65 202.44 167.98 202.44 181.77C202.44 193.74 203.35 212.95 204.02 230.57C199.73 230.12 195.21 229.90 190.69 229.90C149.12 229.90 129.01 248.65 129.01 272.15C129.01 302.88 155.44 313.50 187.76 313.50C225.72 313.50 235.88 294.52 235.88 274.41L235.66 266.50C255.77 275.77 272.71 289.10 285.59 301.98L302.99 275.09C287.40 260.63 263.00 244.81 234.30 236.45C233.17 219.73 232.27 201.65 231.82 188.09C250.12 187.64 276.78 186.51 295.76 184.93L294.86 158.04C276.10 160.30 249.67 161.21 231.59 161.43L231.82 144.94C232.04 136.12 232.72 125.05 233.85 118.50Z"/>
</svg>

# Yomiku

**Unified Android Manga Reader with ManLore Vault Integration**

[![Build Status](https://img.shields.io/github/actions/workflow/status/Badley08/yomiku/build_push.yml?branch=main&style=flat-square&label=CI%20Build&color=74B9FF&labelColor=0F172A)](https://github.com/Badley08/yomiku/actions/workflows/build_push.yml)
[![Latest Release](https://img.shields.io/github/v/release/Badley08/yomiku?style=flat-square&label=Release&color=74B9FF&labelColor=0F172A)](https://github.com/Badley08/yomiku/releases/latest)
[![License](https://img.shields.io/github/license/Badley08/yomiku?style=flat-square&color=74B9FF&labelColor=0F172A)](LICENSE)
[![Android](https://img.shields.io/badge/Android-7.0%2B-74B9FF?style=flat-square&labelColor=0F172A&logo=android&logoColor=white)](https://developer.android.com)

</div>

---

## Overview

Yomiku is an Android manga reader application combining the foundations of [Komikku](https://gitlab.com/AuroraOSS/komikku) and [Tachiyomi](https://github.com/tachiyomiorg/tachiyomi) with an integrated ManLore Vault — a personal reading tracker with XP, quests, and statistics.

The name **Yomiku** (読む, *yomu*) means "to read" in Japanese.

---

## Features

| Category | Details |
|---|---|
| **ManLore Vault** | Track reading progress, quests, XP, statistics, and wishlist directly in-app |
| **Reading Statistics** | Total read time displayed in days/hours/minutes — tap to toggle hourly view |
| **Turso Sync** | High-performance cloud synchronization with local SQLite fallback |
| **Offline PWA** | ManLore Vault is available offline via Service Worker caching |
| **Material 3** | Adaptive Material You theming with dynamic color support |
| **No external metadata** | Manga metadata is sourced directly from the UI — no Anilist or Jikan calls |

---

## Requirements

- Android 7.0 (API 24) or higher
- ARM64-v8a / ARMv7 / x86 / x86_64 architecture

---

## Installation

Download the latest APK from the [Releases](https://github.com/Badley08/yomiku/releases/latest) page.

> If unsure which variant to pick, download `Yomiku-<version>.apk` (universal).

| Variant | Architecture |
|---|---|
| `Yomiku-<version>.apk` | Universal (all devices) |
| `Yomiku-arm64-v8a-<version>.apk` | Modern 64-bit ARM |
| `Yomiku-armeabi-v7a-<version>.apk` | Legacy 32-bit ARM |
| `Yomiku-x86-<version>.apk` | x86 (32-bit) |
| `Yomiku-x86_64-<version>.apk` | x86 (64-bit) |

---

## Build

### Prerequisites

- JDK 17 (LTS)
- Android SDK with Build Tools 35
- Node.js 24.x (LTS)
- Gradle 9.x (via wrapper)

### Local Build

```bash
# Clone the repository
git clone https://github.com/Badley08/yomiku.git
cd yomiku

# Preview build (no signing required)
./gradlew assemblePreview

# Release build
./gradlew assembleRelease -Penable-updater
```

### CI/CD Workflows

| Workflow | Trigger | Purpose |
|---|---|---|
| `build_push.yml` | Push to `main` | CI build and optional signing |
| `build_pull_request.yml` | Pull request | Format check, unit tests, preview APK |
| `build_preview.yml` | Manual dispatch | Tagged preview release |
| `build_release.yml` | Tag `v*` pushed | Signed stable release |

---

## Project Structure

```
yomiku/
├── app/                        # Main application module
│   └── src/main/
│       ├── assets/manlore/     # ManLore Vault PWA (HTML, JS, CSS, SW)
│       ├── java/eu/kanade/     # Kotlin source code
│       └── res/                # Android resources and drawables
├── .github/
│   ├── workflows/              # GitHub Actions CI/CD
│   └── dependabot.yml          # Automated dependency updates
└── build.gradle.kts            # Root build configuration
```

---

## Security

Security scanning is performed automatically via [CodeQL](https://codeql.github.com) and [Dependabot](https://github.com/features/security).

To report a vulnerability, please open a [GitHub Security Advisory](https://github.com/Badley08/yomiku/security/advisories/new) — do not use public issues.

---

## Acknowledgements

Yomiku is built upon the work of the following open-source projects:

- [Komikku](https://gitlab.com/AuroraOSS/komikku) — Manga reader base
- [Tachiyomi](https://github.com/tachiyomiorg/tachiyomi) — Core reader infrastructure
- [Mihon](https://github.com/mihonapp/mihon) — UI and architecture patterns

---

## License

Distributed under the Apache License 2.0. See [LICENSE](LICENSE) for details.
