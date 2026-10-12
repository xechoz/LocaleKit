package me.xechoz.localekit.app.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.graphics.drawable.toBitmap
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import me.xechoz.localekit.app.R
import me.xechoz.localekit.app.data.InstalledApp
import me.xechoz.localekit.app.ui.icons.LocaleIcons
import me.xechoz.localekit.app.ui.theme.LocaleKitTheme
import me.xechoz.localekit.app.viewmodel.AppLocaleUiState

@Composable
fun AppLocaleScreen(
    state: AppLocaleUiState,
    onQueryChange: (String) -> Unit,
    onShowSystemAppsChange: (Boolean) -> Unit,
    onAppClick: (InstalledApp) -> Unit,
    modifier: Modifier = Modifier,
) {
    if (!state.supported) {
        UnsupportedContent(modifier)
        return
    }
    val visibleApps = remember(state.apps, state.query) {
        state.apps.filter { it.matches(state.query) }
    }
    Column(modifier = modifier.fillMaxSize()) {
        ScreenIntro(
            title = stringResource(R.string.app_locale_title),
            description = stringResource(R.string.app_locale_desc),
        )
        OutlinedTextField(
            value = state.query,
            onValueChange = onQueryChange,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            singleLine = true,
            leadingIcon = { Icon(LocaleIcons.Search, contentDescription = null) },
            placeholder = { Text(stringResource(R.string.app_locale_search)) },
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = stringResource(R.string.show_system_apps),
                modifier = Modifier.weight(1f),
                style = MaterialTheme.typography.bodyMedium,
            )
            Switch(checked = state.showSystemApps, onCheckedChange = onShowSystemAppsChange)
        }
        when {
            state.loading -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }

            visibleApps.isEmpty() -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(
                    text = stringResource(R.string.app_locale_empty),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }

            else -> LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(visibleApps, key = { it.packageName }) { app ->
                    AppRow(app = app, onClick = { onAppClick(app) })
                }
            }
        }
    }
}

@Composable
private fun AppRow(app: InstalledApp, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 20.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        AppIcon(app, Modifier.size(40.dp))
        Spacer(Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = app.label,
                style = MaterialTheme.typography.bodyLarge,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            Text(
                text = app.packageName,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
        Icon(
            imageVector = LocaleIcons.ChevronRight,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

@Composable
private fun AppIcon(app: InstalledApp, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val icon by produceState<ImageBitmap?>(initialValue = null, app.packageName) {
        value = withContext(Dispatchers.IO) {
            runCatching {
                context.packageManager.getApplicationIcon(app.packageName)
                    .toBitmap(128, 128)
                    .asImageBitmap()
            }.getOrNull()
        }
    }
    val bitmap = icon
    if (bitmap != null) {
        Image(
            bitmap = bitmap,
            contentDescription = null,
            modifier = modifier.clip(RoundedCornerShape(10.dp)),
        )
    } else {
        LetterAvatar(app.label, modifier)
    }
}

@Composable
private fun LetterAvatar(label: String, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .background(MaterialTheme.colorScheme.primaryContainer),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = label.take(1).uppercase(),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onPrimaryContainer,
        )
    }
}

@Composable
private fun UnsupportedContent(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Icon(
            imageVector = LocaleIcons.List,
            contentDescription = null,
            modifier = Modifier.size(48.dp),
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Spacer(Modifier.height(16.dp))
        Text(
            text = stringResource(R.string.app_locale_unsupported_title),
            style = MaterialTheme.typography.titleMedium,
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text = stringResource(R.string.app_locale_unsupported_desc),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
        )
    }
}

private fun InstalledApp.matches(query: String): Boolean {
    val keyword = query.trim()
    if (keyword.isEmpty()) return true
    return label.contains(keyword, ignoreCase = true) ||
        packageName.contains(keyword, ignoreCase = true)
}

@Preview(showBackground = true)
@Composable
private fun AppLocaleScreenPreview() {
    LocaleKitTheme {
        AppLocaleScreen(
            state = AppLocaleUiState(
                supported = true,
                apps = listOf(
                    InstalledApp("com.example.chat", "聊天", false),
                    InstalledApp("com.example.maps", "地图", false),
                    InstalledApp("com.android.settings", "设置", true),
                ),
                loading = false,
            ),
            onQueryChange = {},
            onShowSystemAppsChange = {},
            onAppClick = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun AppLocaleScreenEmptyPreview() {
    LocaleKitTheme {
        AppLocaleScreen(
            state = AppLocaleUiState(supported = true, apps = emptyList(), loading = false),
            onQueryChange = {},
            onShowSystemAppsChange = {},
            onAppClick = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun AppLocaleScreenUnsupportedPreview() {
    LocaleKitTheme {
        AppLocaleScreen(
            state = AppLocaleUiState(supported = false, loading = false),
            onQueryChange = {},
            onShowSystemAppsChange = {},
            onAppClick = {},
        )
    }
}
