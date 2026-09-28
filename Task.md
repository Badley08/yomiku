# Yomiku (ManLore + Komikku) - Task List & Roadmap

## 📌 Status Legend
- `[X]` : Terminé / Ajouté
- `[-]` : Écarté / Non retenu
- `[ ]` : En attente / À réaliser

---

## 🚀 Étape 1 : Clone & Configuration du Projet Yomiku
- [X] Cloner le dépôt `Badley08/komikku` [X]
- [X] Créer la structure du dépôt distinct **Yomiku** (fusion ManLore + Komikku) [X]
- [X] Renommer toutes les références `Komikku` en `Yomiku` (applicationId = `app.yomiku`, rootProject.name = `Yomiku`, app_name = `Yomiku`, scheme = `yomiku`, en conservant le package `eu.kanade.tachiyomi`) [X]
- [X] Mettre à jour la section Crédits pour remercier les contributeurs de Komikku [X]
- [X] Suppression des liens/icônes Discord et branding Komikku [X]
- [X] Créer/mettre à jour la documentation README.md en anglais [X]

---

## 🔗 Étape 2 : Intégration ManLore & Yomiku
- [X] Intégration fluide de la navigation entre ManLore et Yomiku (onglet `ManLoreTab` WebView dans l'application) [X]
- [X] Sauvegarde automatique des lectures de mangas directement dans le Vault de ManLore via `ManLoreVaultManager` [X]
- [X] Gestion de la connexion utilisateur (choix entre stockage local ou synchro Cloud) [X]
- [X] Accessibilité des paramètres et navigation unifiée au sein d'une seule application [X]

---

## 🗄️ Étape 3 : Migration Base de Données (Turso) & Synchronisation
- [X] Remplacer Back4app par Turso (`libsql://manlore-badley08.aws-ap-northeast-1.turso.io`) [X]
- [X] Configurer le jeton d'authentification Turso dans `SyncPreferences` [X]
- [X] Dans les paramètres de synchronisation, **supprimer WebDAV** pour ne conserver strictement que **Google Drive** et **ManLore Sync** [X]
- [X] Rédiger et intégrer un message d'information aux utilisateurs concernant la nouvelle base de données [X]
- [X] Intégrer les schémas et la gestion de `manlore.db` directement dans le main de Yomiku [X]

---

## 🛠️ Étape 4 : CI/CD GitHub Actions & Release V1
- [X] Créer/configurer les workflows GitHub Actions pour builder Yomiku V1 (`build_release.yml` et `build_push.yml`) [X]
- [X] Configurer les prérequis du workflow : Node.js LTS (24.x), Java 17 LTS (via `.java-version`), Gradle 9.x [X]
- [X] Optimisation du build et génération des APKs signés sous le nom `Yomiku-v1.apk` [X]
