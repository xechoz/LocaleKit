package me.xechoz.localekit.app.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import me.xechoz.localekit.AppLocaleSettings
import me.xechoz.localekit.app.data.InstalledApp
import me.xechoz.localekit.app.data.InstalledApps

data class AppLocaleUiState(
    val supported: Boolean = AppLocaleSettings.isSupported,
    val apps: List<InstalledApp> = emptyList(),
    val query: String = "",
    val showSystemApps: Boolean = false,
    val loading: Boolean = true,
)

class AppLocaleViewModel(application: Application) : AndroidViewModel(application) {

    private val _state = MutableStateFlow(AppLocaleUiState())
    val state: StateFlow<AppLocaleUiState> = _state.asStateFlow()

    init {
        if (_state.value.supported) loadApps(includeSystem = false) else finishLoading()
    }

    fun setQuery(query: String) {
        _state.update { it.copy(query = query) }
    }

    fun setShowSystemApps(includeSystem: Boolean) {
        _state.update { it.copy(showSystemApps = includeSystem) }
        if (_state.value.supported) loadApps(includeSystem) else finishLoading()
    }

    fun open(packageName: String): Boolean = AppLocaleSettings.open(getApplication(), packageName)

    private fun loadApps(includeSystem: Boolean) {
        viewModelScope.launch {
            _state.update { it.copy(loading = true) }
            val apps = withContext(Dispatchers.Default) {
                InstalledApps.load(getApplication(), includeSystem)
            }
            _state.update { it.copy(apps = apps, loading = false) }
        }
    }

    private fun finishLoading() {
        _state.update { it.copy(loading = false) }
    }
}
