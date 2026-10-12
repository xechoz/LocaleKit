package me.xechoz.localekit

import java.util.Locale
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class CommonLocalesTest {

    @Test
    fun `locales are unique`() {
        val tags = CommonLocales.all.map(Locale::toLanguageTag)

        assertEquals(tags.size, tags.distinct().size)
    }

    @Test
    fun `locales are well formed`() {
        assertTrue(CommonLocales.all.size >= 20)
        assertTrue(CommonLocales.all.none { it.language.isBlank() })
    }
}
