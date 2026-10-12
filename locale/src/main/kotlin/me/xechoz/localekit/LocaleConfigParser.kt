package me.xechoz.localekit

import android.content.Context
import java.util.Locale
import org.xmlpull.v1.XmlPullParser

/**
 * 解析 `@xml/locales_config`（按资源名查找，无反射）。
 *
 * Android 的 xml 资源在 APK 中是编译后的二进制 XML，必须经 [android.content.res.Resources.getXml]
 * 读取，故这里用 XmlPullParser；标签到 [Locale] 的纯映射抽到 [localeTagsToLocales] 以便单测。
 */
internal object LocaleConfigParser {

    private const val RESOURCE_NAME = "locales_config"
    private const val ANDROID_NS = "http://schemas.android.com/apk/res/android"
    private const val TAG_LOCALE = "locale"
    private const val ATTR_NAME = "name"

    fun load(context: Context): List<Locale> {
        val resId = context.resources.getIdentifier(RESOURCE_NAME, "xml", context.packageName)
        if (resId == 0) return emptyList()
        return context.resources.getXml(resId).use(::parse)
    }

    internal fun parse(parser: XmlPullParser): List<Locale> {
        val tags = buildList {
            while (parser.next() != XmlPullParser.END_DOCUMENT) {
                if (parser.eventType != XmlPullParser.START_TAG || parser.name != TAG_LOCALE) continue
                val name = parser.getAttributeValue(ANDROID_NS, ATTR_NAME)
                    ?: parser.getAttributeValue(null, ATTR_NAME)
                if (name != null) add(name)
            }
        }
        return localeTagsToLocales(tags)
    }
}
