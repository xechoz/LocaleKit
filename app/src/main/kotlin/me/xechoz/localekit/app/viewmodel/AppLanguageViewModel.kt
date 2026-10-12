package me.xechoz.localekit.app.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import me.xechoz.localekit.AppLanguage
import me.xechoz.localekit.AppLanguagePreference
import me.xechoz.localekit.LanguageOption

data class AppLanguageUiState(
    val available: List<LanguageOption> = emptyList(),
    val selectedTag: String? = null,
)

class AppLanguageViewModel(application: Application) : AndroidViewModel(application) {

    private val _state = MutableStateFlow(load())
    val state: StateFlow<AppLanguageUiState> = _state.asStateFlow()

    fun followSystem() {
        AppLanguage.set(AppLanguagePreference.FollowSystem)
        _state.value = _state.value.copy(selectedTag = null)
    }

    fun select(option: LanguageOption) {
        AppLanguage.set(AppLanguagePreference.Explicit(option.locale))
        _state.value = _state.value.copy(selectedTag = option.locale.toLanguageTag())
    }

    private fun load(): AppLanguageUiState = AppLanguageUiState(
        available = AppLanguage.available(getApplication()),
        selectedTag = AppLanguage.current().tag,
    )
}

private val AppLanguagePreference.tag: String?
    get() = when (this) {
        AppLanguagePreference.FollowSystem -> null
        is AppLanguagePreference.Explicit -> locale.toLanguageTag()
    }
