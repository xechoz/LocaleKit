package me.xechoz.localekit.app.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import me.xechoz.localekit.SystemLanguage

data class SystemLanguageUiState(
    val displayNames: List<String> = emptyList(),
)

class SystemLanguageViewModel(application: Application) : AndroidViewModel(application) {

    private val _state = MutableStateFlow(load())
    val state: StateFlow<SystemLanguageUiState> = _state.asStateFlow()

    fun refresh() {
        _state.value = load()
    }

    fun openSettings(): Boolean = SystemLanguage.openSettings(getApplication())

    private fun load(): SystemLanguageUiState = SystemLanguageUiState(
        displayNames = SystemLanguage.current(getApplication()).map(SystemLanguage::displayName),
    )
}
