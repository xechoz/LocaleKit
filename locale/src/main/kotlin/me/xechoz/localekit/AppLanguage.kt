package me.xechoz.localekit

import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import java.util.Locale

/**
 * 本应用的语言偏好。
 *
 * 只表达"跟随系统"与"指定语言"两种领域状态，避免在公开签名中出现 null。
 */
sealed interface AppLanguagePreference {
    data object FollowSystem : AppLanguagePreference

    data class Explicit(val locale: Locale) : AppLanguagePreference
}

/** 语言选择器的一项：语言 + 按当前 App 语言渲染的显示名。 */
data class LanguageOption(val locale: Locale, val label: String)

/**
 * 本应用语言切换（AppCompatDelegate 官方兼容 API）。
 *
 * - Android 13+：同步写入系统"应用语言"设置，本 App 出现在系统语言列表（需在 manifest 声明
 *   `android:localeConfig`）。
 * - Android 12 及以下：由 AppCompat 持久化并重建 Activity（依赖库 manifest 合并的
 *   `AppLocalesMetadataHolderService`，使用方无需配置）。
 *
 * 使用方约束：宿主 Activity 必须继承 [androidx.appcompat.app.AppCompatActivity]。
 */
object AppLanguage {

    /** 当前语言偏好；未设置过则为 [AppLanguagePreference.FollowSystem]。 */
    fun current(): AppLanguagePreference {
        val primary = AppCompatDelegate.getApplicationLocales()[0]
        return if (primary == null) {
            AppLanguagePreference.FollowSystem
        } else {
            AppLanguagePreference.Explicit(primary)
        }
    }

    /**
     * 设置语言偏好。需在主线程调用；Android 12 及以下会重建 Activity。
     */
    fun set(preference: AppLanguagePreference) {
        val locales = when (preference) {
            AppLanguagePreference.FollowSystem -> LocaleListCompat.getEmptyLocaleList()
            is AppLanguagePreference.Explicit ->
                LocaleListCompat.forLanguageTags(preference.locale.toLanguageTag())
        }
        AppCompatDelegate.setApplicationLocales(locales)
    }

    /**
     * 本 App 支持的语言，来自 `@xml/locales_config`（资源名查找 + XmlPullParser，无反射）。
     * 未声明该资源时返回空列表。
     */
    fun available(context: Context): List<LanguageOption> {
        val uiLocale = Locale.getDefault()
        return LocaleConfigParser.load(context).map { locale ->
            LanguageOption(locale, locale.displayNameIn(uiLocale))
        }
    }
}
