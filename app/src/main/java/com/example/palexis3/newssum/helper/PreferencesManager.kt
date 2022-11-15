package com.example.palexis3.newssum.helper

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.preferencesDataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PreferencesManager @Inject constructor(private val context: Context) {

    companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "preferences")
        val COUNTRY_PREF_KEY = stringPreferencesKey("country")
        val LANGUAGE_PREF_KEY = stringPreferencesKey("language")
    }

    val getCountry: Flow<String> = context.dataStore.data
        .map { preferences ->
            preferences[COUNTRY_PREF_KEY] ?: ""
        }

    val getLanguage: Flow<String> = context.dataStore.data
        .map { preferences ->
            preferences[LANGUAGE_PREF_KEY] ?: ""
        }

    suspend fun setCountry(country: String) {
        context.dataStore.edit { preferences ->
            preferences[COUNTRY_PREF_KEY] = country
        }
    }

    suspend fun setLanguage(language: String) {
        context.dataStore.edit { preferences ->
            preferences[LANGUAGE_PREF_KEY] = language
        }
    }
}