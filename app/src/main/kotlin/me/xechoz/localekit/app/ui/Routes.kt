package me.xechoz.localekit.app.ui

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.res.stringResource
import me.xechoz.localekit.app.R
import me.xechoz.localekit.app.viewmodel.AppLanguageViewModel
import me.xechoz.localekit.app.viewmodel.AppLocaleViewModel
import me.xechoz.localekit.app.viewmodel.SystemLanguageViewModel

@Composable
fun AppLanguageRoute(viewModel: AppLanguageViewModel = viewModel()) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    AppLanguageScreen(
        state = state,
        onFollowSystem = viewModel::followSystem,
        onSelect = viewModel::select,
    )
}

@Composable
fun SystemLanguageRoute(viewModel: SystemLanguageViewModel = viewModel()) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    LifecycleEventEffect(Lifecycle.Event.ON_RESUME) { viewModel.refresh() }
    SystemLanguageScreen(state = state, onOpenSettings = viewModel::openSettings)
}

@Composable
fun AppLocaleRoute(viewModel: AppLocaleViewModel = viewModel()) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val openFailedMessage = stringResource(R.string.app_locale_open_failed)
    AppLocaleScreen(
        state = state,
        onQueryChange = viewModel::setQuery,
        onShowSystemAppsChange = viewModel::setShowSystemApps,
        onAppClick = { app ->
            if (!viewModel.open(app.packageName)) {
                Toast.makeText(context, openFailedMessage, Toast.LENGTH_SHORT).show()
            }
        },
    )
}
