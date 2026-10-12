package me.xechoz.localekit.app.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import me.xechoz.localekit.app.R
import me.xechoz.localekit.app.ui.theme.LocaleKitTheme
import me.xechoz.localekit.app.viewmodel.SystemLanguageUiState

@Composable
fun SystemLanguageScreen(
    state: SystemLanguageUiState,
    onOpenSettings: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        ScreenIntro(
            title = stringResource(R.string.system_language_title),
            description = stringResource(R.string.system_language_desc),
        )
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Text(
                    text = stringResource(R.string.current_system_language),
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                if (state.displayNames.isEmpty()) {
                    Text(text = "—", style = MaterialTheme.typography.headlineSmall)
                } else {
                    state.displayNames.forEachIndexed { index, name ->
                        Text(
                            text = name,
                            style = if (index == 0) {
                                MaterialTheme.typography.headlineSmall
                            } else {
                                MaterialTheme.typography.bodyLarge
                            },
                        )
                    }
                }
            }
        }
        Text(
            text = stringResource(R.string.system_language_hint),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Button(onClick = onOpenSettings, modifier = Modifier.fillMaxWidth()) {
            Text(text = stringResource(R.string.open_language_settings))
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SystemLanguageScreenPreview() {
    LocaleKitTheme {
        SystemLanguageScreen(
            state = SystemLanguageUiState(displayNames = listOf("中文（中国）", "英语（美国）")),
            onOpenSettings = {},
        )
    }
}
