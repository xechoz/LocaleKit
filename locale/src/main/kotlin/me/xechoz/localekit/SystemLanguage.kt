package me.xechoz.localekit

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.os.Build
import android.os.LocaleList
import android.provider.Settings
import java.util.Locale

/**
 * 系统语言（只读 + 跳转设置页）。
 *
 * 本库不提供程序化修改系统语言：该能力在非 root 设备上无公开 API，实现不可靠。
 */
object SystemLanguage {

    /**
     * 当前系统语言，首个为主语言。
     *
     * 13+ 读取 [android.app.LocaleManager]（权威来源，不受本 App 的 per-app override 影响）；
     * 12 及以下读取系统 [Resources] 配置。
     */
    fun current(context: Context): List<Locale> =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            context.getSystemService(android.app.LocaleManager::class.java)
                .systemLocales
                .asLocaleList()
        } else {
            Resources.getSystem().configuration.locales.asLocaleList()
        }

    /** 语言显示名，用当前 App 语言渲染，如 "英语（美国）"。 */
    fun displayName(locale: Locale): String = locale.displayNameIn(Locale.getDefault())

    /**
     * 跳转系统语言设置页，返回是否成功打开。
     *
     * 13+ 使用 [Settings.ACTION_LOCALE_SETTINGS]；12 及以下依次探测常见
     * Settings 组件，均失败则回退到设置首页。
     */
    fun openSettings(context: Context): Boolean {
        for (intent in openCandidates()) {
            if (context.tryStartActivity(intent)) return true
        }
        return false
    }

    private fun openCandidates(): List<Intent> = buildList {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            add(Intent(Settings.ACTION_LOCALE_SETTINGS))
        } else {
            addAll(LEGACY_COMPONENTS.map { Intent().setComponent(it) })
        }
        add(Intent(Settings.ACTION_SETTINGS))
    }

    private val LEGACY_COMPONENTS = listOf(
        ComponentName("com.android.settings", "com.android.settings.Settings\$LocalePickerActivity"),
        ComponentName("com.android.settings", "com.android.settings.Settings\$LanguageSettingsActivity"),
        ComponentName(
            "com.android.settings",
            "com.android.settings.Settings\$LanguageAndInputSettingsActivity",
        ),
    )
}
