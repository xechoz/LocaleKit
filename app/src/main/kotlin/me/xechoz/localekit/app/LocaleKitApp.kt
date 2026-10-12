package me.xechoz.localekit.app

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import me.xechoz.localekit.app.ui.AppLanguageRoute
import me.xechoz.localekit.app.ui.AppLocaleRoute
import me.xechoz.localekit.app.ui.SystemLanguageRoute
import me.xechoz.localekit.app.ui.icons.LocaleIcons
import me.xechoz.localekit.app.ui.theme.LocaleKitTheme

private enum class Destination(@StringRes val labelRes: Int, val icon: ImageVector) {
    SystemLanguage(R.string.nav_system_language, LocaleIcons.Globe),
    AppLocale(R.string.nav_app_locale, LocaleIcons.List),
    AppLanguage(R.string.nav_app_language, LocaleIcons.Languages),
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LocaleKitApp() {
    LocaleKitTheme {
        var destination by rememberSaveable { mutableStateOf(Destination.SystemLanguage) }
        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(title = { Text(stringResource(destination.labelRes)) })
            },
            bottomBar = {
                NavigationBar {
                    Destination.entries.forEach { item ->
                        NavigationBarItem(
                            selected = item == destination,
                            onClick = { destination = item },
                            icon = { Icon(item.icon, contentDescription = null) },
                            label = { Text(stringResource(item.labelRes)) },
                        )
                    }
                }
            },
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
            ) {
                when (destination) {
                    Destination.AppLanguage -> AppLanguageRoute()
                    Destination.SystemLanguage -> SystemLanguageRoute()
                    Destination.AppLocale -> AppLocaleRoute()
                }
            }
        }
    }
}
