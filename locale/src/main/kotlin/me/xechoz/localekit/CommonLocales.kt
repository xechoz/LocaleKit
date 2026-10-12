package me.xechoz.localekit

import java.util.Locale

/** 常用语言列表，作为语言选择器的默认数据源。 */
object CommonLocales {

    val all: List<Locale> = localeTagsToLocales(
        listOf(
            "zh-CN", "zh-TW", "en-US", "en-GB", "ja-JP", "ko-KR",
            "fr-FR", "de-DE", "es-ES", "pt-BR", "pt-PT", "ru-RU",
            "ar-SA", "it-IT", "nl-NL", "pl-PL", "tr-TR", "th-TH",
            "vi-VN", "id-ID", "ms-MY", "hi-IN", "bn-IN", "fa-IR",
            "uk-UA", "he-IL", "el-GR", "cs-CZ", "sv-SE", "da-DK",
            "fi-FI", "hu-HU", "ro-RO", "nb-NO",
        ),
    )
}
