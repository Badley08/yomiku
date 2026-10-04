package eu.kanade.tachiyomi.ui.manlore

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.webkit.JavascriptInterface
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.MenuBook
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.viewinterop.AndroidView
import cafe.adriel.voyager.navigator.tab.TabOptions
import android.webkit.ConsoleMessage
import android.webkit.WebChromeClient
import eu.kanade.domain.manlore.ManLoreVaultManager
import eu.kanade.presentation.util.Tab
import logcat.LogPriority
import tachiyomi.core.common.util.system.logcat
import tachiyomi.presentation.core.components.material.Scaffold
import java.util.Locale

class YomikuWebBridge(private val vaultManager: ManLoreVaultManager = ManLoreVaultManager()) {
    @JavascriptInterface
    fun getVaultEntriesJson(): String = vaultManager.getVaultEntriesAsJson()

    @JavascriptInterface
    fun getDeviceLanguage(): String = Locale.getDefault().language

    @JavascriptInterface
    fun log(level: String?, tag: String?, message: String?) {
        val priority = when (level?.lowercase()) {
            "error", "err" -> LogPriority.ERROR
            "warn", "warning" -> LogPriority.WARN
            "debug" -> LogPriority.DEBUG
            else -> LogPriority.INFO
        }
        val logTag = tag?.takeIf { it.isNotBlank() } ?: "ManLore"
        logcat(logTag, priority) { message.orEmpty() }
    }
}

data object ManLoreTab : Tab {
    @Suppress("UnusedPrivateMember")
    private fun readResolve(): Any = ManLoreTab

    override val options: TabOptions
        @Composable
        get() {
            val title = "ManLore"
            val icon = rememberVectorPainter(Icons.Outlined.MenuBook)
            return remember {
                TabOptions(
                    index = 2u,
                    title = title,
                    icon = icon,
                )
            }
        }

    @SuppressLint("SetJavaScriptEnabled")
    @Composable
    override fun Content() {
        var isLoading by remember { mutableStateOf(true) }

        Scaffold { contentPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(contentPadding),
            ) {
                AndroidView(
                    factory = { context ->
                        WebView(context).apply {
                            settings.apply {
                                javaScriptEnabled = true
                                domStorageEnabled = true
                                databaseEnabled = true
                                allowFileAccess = true
                                allowContentAccess = true
                                mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
                            }
                            addJavascriptInterface(YomikuWebBridge(), "YomikuBridge")
                            webChromeClient = object : WebChromeClient() {
                                override fun onConsoleMessage(consoleMessage: ConsoleMessage?): Boolean {
                                    consoleMessage?.let {
                                        val priority = when (it.messageLevel()) {
                                            ConsoleMessage.MessageLevel.ERROR -> LogPriority.ERROR
                                            ConsoleMessage.MessageLevel.WARNING -> LogPriority.WARN
                                            ConsoleMessage.MessageLevel.DEBUG -> LogPriority.DEBUG
                                            else -> LogPriority.INFO
                                        }
                                        logcat("ManLoreJS", priority) {
                                            "${it.message()} [${it.sourceId()}:${it.lineNumber()}]"
                                        }
                                    }
                                    return true
                                }
                            }
                            webViewClient = object : WebViewClient() {
                                override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                                    super.onPageStarted(view, url, favicon)
                                    isLoading = true
                                }

                                override fun onPageFinished(view: WebView?, url: String?) {
                                    super.onPageFinished(view, url)
                                    isLoading = false
                                    view?.evaluateJavascript(
                                        """
                                        (function() {
                                            try {
                                                if (window.YomikuBridge && typeof window.syncFromYomikuUI === 'function') {
                                                    window.syncFromYomikuUI(window.YomikuBridge.getVaultEntriesJson());
                                                }
                                            } catch(e) { console.error('YomikuBridge error', e); }
                                        })();
                                        """.trimIndent(),
                                        null,
                                    )
                                }
                            }
                            loadUrl("file:///android_asset/manlore/index.html")
                        }
                    },
                    modifier = Modifier.fillMaxSize(),
                )

                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center),
                    )
                }
            }
        }
    }
}
