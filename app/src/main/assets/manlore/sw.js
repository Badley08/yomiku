/* ============================================
   MANLORE v9.0.2 - SERVICE WORKER
   Offline PWA Support & Relative Scope Routing
   ============================================ */

'use strict';

const CACHE_NAME = 'manlore-v9.0.2-cache';

const STATIC_ASSETS = [
    './',
    './index.html',
    './styles.css',
    './app.css',
    './i18n.js',
    './jikan.js',
    './logic.js',
    './analyse.js',
    './wishlist.js',
    './quests.js',
    './logger.js',
    './app.js',
    './manifest.json',
    './manlore_fr-quests_xp.json',
    './manlore_en-quests_xp.json',
    './manlore_es-quests_xp.json',
    './manlore-logo.png',
    './manlore-logo-48.png',
    './manlore-logo-72.png',
    './manlore-logo-96.png',
    './manlore-logo-128.png',
    './manlore-logo-144.png',
    './manlore-logo-192.png',
    './manlore-logo-256.png',
    './manlore-logo-384.png',
    './manlore-logo-512.png',
];

/* ── Strict allowlists (replaces all substring checks) ─────────────────── */

/** Hostnames allowed to receive network-only requests (API calls). */
const API_HOSTS_ALLOWLIST = new Set([
    'parseapi.back4app.com',
    'api.jikan.moe',
    'kitsu.io',
    'api.mangadex.org',
]);

/** Hostnames whose responses may be cached (fonts/CDN). */
const FONT_HOSTS_ALLOWLIST = new Set([
    'fonts.googleapis.com',
    'fonts.gstatic.com',
]);

/**
 * Returns true only when the URL's hostname exactly matches an entry in
 * the provided Set. Using exact hostname equality prevents the
 * incomplete-url-substring-sanitization (js/incomplete-url-substring-sanitization)
 * and related CWE-20 / CWE-116 findings.
 */
function hostnameAllowed(hostname, allowlist) {
    return allowlist.has(hostname);
}

// ── Install ───────────────────────────────────────────────────────────────
self.addEventListener('install', (event) => {
    console.log('[SW] Installing v9.0.2...');
    event.waitUntil(
        caches.open(CACHE_NAME).then((cache) =>
            Promise.allSettled(
                STATIC_ASSETS.map((asset) =>
                    cache.add(asset).catch((e) =>
                        console.warn('[SW] Cache note:', asset, e.message)
                    )
                )
            )
        ).then(() => self.skipWaiting())
    );
});

// ── Activate — clean old caches ───────────────────────────────────────────
self.addEventListener('activate', (event) => {
    console.log('[SW] Activating v9.0.2...');
    event.waitUntil(
        caches.keys().then((keys) =>
            Promise.all(
                keys
                    .filter((k) => k !== CACHE_NAME)
                    .map((k) => {
                        console.log('[SW] Deleting old cache:', k);
                        return caches.delete(k);
                    })
            )
        ).then(() => self.clients.claim())
    );
});

// ── Fetch ─────────────────────────────────────────────────────────────────
self.addEventListener('fetch', (event) => {
    // Only handle GET requests
    if (event.request.method !== 'GET') return;

    let url;
    try {
        url = new URL(event.request.url);
    } catch {
        // Unparseable URL — ignore
        return;
    }

    // Reject non-HTTPS origins (except same-origin relative assets) to
    // prevent functionality-from-untrusted-source.
    if (url.protocol !== 'https:' && url.origin !== self.location.origin) {
        return;
    }

    // API calls — network only with safe offline fallback
    if (hostnameAllowed(url.hostname, API_HOSTS_ALLOWLIST)) {
        event.respondWith(
            fetch(event.request).catch(() =>
                new Response(
                    JSON.stringify({ error: 'offline' }),
                    { headers: { 'Content-Type': 'application/json' } }
                )
            )
        );
        return;
    }

    // Font / CDN — cache-first (only for explicitly allowed origins)
    if (hostnameAllowed(url.hostname, FONT_HOSTS_ALLOWLIST)) {
        event.respondWith(
            caches.match(event.request).then((cached) => {
                if (cached) return cached;
                return fetch(event.request).then((response) => {
                    if (response.ok) {
                        const clone = response.clone();
                        caches
                            .open(CACHE_NAME)
                            .then((cache) => cache.put(event.request, clone));
                    }
                    return response;
                });
            })
        );
        return;
    }

    // Same-origin static assets — Stale-While-Revalidate
    if (url.origin === self.location.origin) {
        event.respondWith(
            caches.match(event.request).then((cached) => {
                const networkFetch = fetch(event.request)
                    .then((response) => {
                        if (response.ok) {
                            const clone = response.clone();
                            caches
                                .open(CACHE_NAME)
                                .then((cache) =>
                                    cache.put(event.request, clone)
                                );
                        }
                        return response;
                    })
                    .catch(() => null);

                return (
                    cached ||
                    networkFetch.then(
                        (res) =>
                            res ||
                            (event.request.destination === 'document'
                                ? caches.match('./index.html')
                                : new Response('Offline', { status: 503 }))
                    )
                );
            })
        );
    }
    // All other cross-origin requests are silently ignored (not proxied).
});

// ── Push Notifications ────────────────────────────────────────────────────
self.addEventListener('push', (event) => {
    /** @type {{ title?: string, body?: string, tag?: string, [lang: string]: unknown }} */
    let data = { title: 'ManLore', body: 'New notification from ManLore' };

    try {
        if (event.data) {
            const parsed = event.data.json();
            // Only accept plain objects — reject arrays/primitives
            if (parsed !== null && typeof parsed === 'object' && !Array.isArray(parsed)) {
                data = parsed;
            }
        }
    } catch {
        if (event.data) data.body = event.data.text();
    }

    // Safe string extraction — avoids XSS-through-dom by never inserting
    // untrusted content into the DOM (Notification API only accepts strings).
    const safeStr = (val, fallback) =>
        typeof val === 'string' ? val : fallback;

    const clientLang = ((self.navigator && self.navigator.language) || 'en')
        .split('-')[0]
        .toLowerCase();

    const localized = (lang) =>
        data[lang] !== null &&
        typeof data[lang] === 'object' &&
        !Array.isArray(data[lang])
            ? data[lang]
            : null;

    const loc =
        localized(clientLang) ||
        localized('en') ||
        localized('fr') ||
        localized('es') ||
        {};

    const finalTitle = safeStr(loc.title, safeStr(data.title, 'ManLore'));
    const finalBody  = safeStr(loc.body,  safeStr(data.body,  ''));
    const finalTag   = safeStr(data.tag, 'manlore-notification');

    const options = {
        body:    finalBody,
        icon:    './manlore-logo-192.png',
        badge:   './manlore-logo-96.png',
        vibrate: [100, 50, 100],
        data:    { tag: finalTag },
        tag:     finalTag,
    };

    event.waitUntil(self.registration.showNotification(finalTitle, options));
});

self.addEventListener('notificationclick', (event) => {
    event.notification.close();
    event.waitUntil(
        clients
            .matchAll({ type: 'window', includeUncontrolled: true })
            .then((clientList) => {
                for (const client of clientList) {
                    if (client.url && 'focus' in client) {
                        return client.focus();
                    }
                }
                if (clients.openWindow) {
                    return clients.openWindow('./');
                }
            })
    );
});

self.addEventListener('message', (event) => {
    if (!event.data || event.data.type !== 'SHOW_NOTIFICATION') return;

    const safeStr = (val, fallback) =>
        typeof val === 'string' ? val : fallback;

    const title = safeStr(event.data.title, 'ManLore');
    const body  = safeStr(event.data.body, '');
    const tag   = safeStr(event.data.tag, 'manlore-msg');

    self.registration.showNotification(title, {
        body,
        icon:  './manlore-logo-192.png',
        badge: './manlore-logo-96.png',
        tag,
    });
});

console.log('[SW] Service Worker v9.0.2 loaded');
