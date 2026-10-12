package me.xechoz.localekit

import java.util.Locale
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class LocalesTest {

    @Test
    fun `trims blanks and dedupes while preserving order`() {
        val locales = localeTagsToLocales(listOf(" en-US ", "", "  ", "en-US", "zh-CN"))

        assertEquals(listOf("en-US", "zh-CN"), locales.map(Locale::toLanguageTag))
    }

    @Test
    fun `maps bcp47 tags to locales`() {
        val locales = localeTagsToLocales(listOf("zh-Hans-CN", "pt-BR"))

        assertEquals("zh-Hans-CN", locales[0].toLanguageTag())
        assertEquals("pt-BR", locales[1].toLanguageTag())
    }

    @Test
    fun `displayNameIn uses provided locale`() {
        val name = Locale.forLanguageTag("en-US").displayNameIn(Locale.ENGLISH)

        assertTrue(name.contains("English"))
    }
}
