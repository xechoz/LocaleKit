package me.xechoz.localekit

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.os.LocaleList
import java.util.Locale

/** 标签列表 → 去空、去重、保序的 [Locale] 列表（纯函数，可单测）。 */
internal fun localeTagsToLocales(tags: List<String>): List<Locale> =
    tags.map(String::trim)
        .filter(String::isNotEmpty)
        .distinct()
        .map(Locale::forLanguageTag)

internal fun Locale.displayNameIn(inLocale: Locale): String = getDisplayName(inLocale)

internal fun LocaleList.asLocaleList(): List<Locale> = List(size()) { get(it) }

/**
 * 在 I/O 边界收敛"目标 Activity 不存在"的异常。
 *
 * 统一附加 [Intent.FLAG_ACTIVITY_NEW_TASK]，使调用方可直接传 Application / ViewModel 持有的
 * Context 启动外部设置页。
 */
internal fun Context.tryStartActivity(intent: Intent): Boolean = try {
    startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
    true
} catch (_: ActivityNotFoundException) {
    false
}
