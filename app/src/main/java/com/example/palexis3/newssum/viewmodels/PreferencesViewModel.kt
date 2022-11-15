package com.example.palexis3.newssum.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.palexis3.newssum.helper.PreferencesManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PreferencesViewModel @Inject constructor(
    private val preferencesManager: PreferencesManager
) : ViewModel() {

    val countryMap = mapOf(
        "Argentina" to "ar",
        "Austria" to "at",
        "Australia" to "au",
        "Belgium" to "be",
        "Bulgaria" to "bg",
        "Brazil" to "br",
        "Canada" to "ca",
        "China" to "cn",
        "Colombia" to "co",
        "Cuba" to "cu",
        "Czech Republic" to "cz",
        "Egypt" to "eg",
        "France" to "fr",
        "Greece" to "gr",
        "Hong Kong" to "hk",
        "Hungary" to "hu",
        "Indonesia" to "id",
        "Ireland" to "ie",
        "Israel" to "il",
        "India" to "in",
        "Italy" to "it",
        "Japan" to "jp",
        "Lithuania" to "lt",
        "Morocco" to "ma",
        "Mexico" to "mx",
        "Malaysia" to "my",
        "Nigeria" to "ng",
        "Netherland" to "nl",
        "Norway" to "no",
        "New Zealand" to "nz",
        "Philippines" to "ph",
        "Poland" to "pl",
        "Portugal" to "pt",
        "Romania" to "ro",
        "Serbia" to "rs",
        "Russia" to "ru",
        "Saudia Arabia" to "sa",
        "Sweden" to "se",
        "Singapore" to "sg",
        "Slovenia" to "si",
        "Slovakia" to "sk",
        "Thailand" to "th",
        "Turkey" to "tr",
        "Taiwan" to "tw",
        "Ukraine" to "ua",
        "United States" to "us",
        "Venezuela" to "ve",
        "South Africa" to "za"
    )

    val languageMap = mapOf(
        "Arabic" to "ar",
        "German" to "de",
        "English" to "en",
        "Spanish" to "es",
        "French" to "fr",
        "Hebrew" to "he",
        "Italian" to "it",
        "Dutch" to "nl",
        "Norwegian" to "no",
        "Portuguese" to "pt",
        "Russian" to "ru",
        "Swedish" to "sv",
        "Chinese" to "zh"
    )

    val country: StateFlow<String?> = preferencesManager.getCountry
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )

    val language: StateFlow<String?> = preferencesManager.getLanguage
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )

    fun setCountry(country: String) {
        viewModelScope.launch(Dispatchers.IO) {
            preferencesManager.setCountry(country)
        }
    }

    fun setLanguage(language: String) {
        viewModelScope.launch(Dispatchers.IO) {
            preferencesManager.setLanguage(language)
        }
    }
}
