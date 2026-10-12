package me.xechoz.localekit.app.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import java.util.Locale
import me.xechoz.localekit.LanguageOption
import me.xechoz.localekit.app.R
import me.xechoz.localekit.app.ui.theme.LocaleKitTheme
import me.xechoz.localekit.app.viewmodel.AppLanguageUiState

@Composable
fun AppLanguageScreen(
    state: AppLanguageUiState,
    onFollowSystem: () -> Unit,
    onSelect: (LanguageOption) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(modifier = modifier.fillMaxSize()) {
        item {
            ScreenIntro(
                title = stringResource(R.string.app_language_title),
                description = stringResource(R.string.app_language_desc),
            )
        }
        item {
            SelectableRow(
                label = stringResource(R.string.follow_system),
                supporting = null,
                selected = state.selectedTag == null,
                onClick = onFollowSystem,
            )
        }
        items(state.available, key = { it.locale.toLanguageTag() }) { option ->
            SelectableRow(
                label = option.label,
                supporting = option.locale.toLanguageTag(),
                selected = state.selectedTag == option.locale.toLanguageTag(),
                onClick = { onSelect(option) },
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun AppLanguageScreenPreview() {
    LocaleKitTheme {
        AppLanguageScreen(
            state = AppLanguageUiState(
                available = listOf(
                    LanguageOption(Locale.forLanguageTag("zh-CN"), "中文（中国）"),
                    LanguageOption(Locale.forLanguageTag("en-US"), "英语（美国）"),
                    LanguageOption(Locale.forLanguageTag("ja-JP"), "日语（日本）"),
                ),
                selectedTag = "zh-CN",
            ),
            onFollowSystem = {},
            onSelect = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun AppLanguageScreenFollowSystemPreview() {
    LocaleKitTheme {
        AppLanguageScreen(
            state = AppLanguageUiState(
                available = listOf(
                    LanguageOption(Locale.forLanguageTag("en"), "英语"),
                ),
                selectedTag = null,
            ),
            onFollowSystem = {},
            onSelect = {},
        )
    }
}
