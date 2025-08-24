package com.nlhd.keystore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.Json

private val Context.dataHistoryStore by preferencesDataStore(name = "history_search")

object HistorySearchManager {
    private val HISTORY_KEY = stringSetPreferencesKey("history_key")

    // Lưu 1 list
    suspend fun saveHistory(context: Context, list: List<String>) {
        context.dataStore.edit { prefs ->
            prefs[HISTORY_KEY] = list.toSet() // DataStore chỉ hỗ trợ Set<String>
        }
    }

    //Lấy list
    fun getHistory(context: Context): Flow<List<String>> {
        return context.dataHistoryStore.data.map { prefs ->
            prefs[HISTORY_KEY]?.toList() ?: emptyList()
        }
    }

    suspend fun addHistory(context: Context, item: String) {
        context.dataHistoryStore.edit { prefs->
            val current = prefs[HISTORY_KEY] ?: emptySet()
            prefs[HISTORY_KEY] = (setOf(item) + current).take(10).toSet()
        }
    }

    suspend fun removeHistoryItem(context: Context, item: String) {
        context.dataHistoryStore.edit { prefs ->
            val current = prefs[HISTORY_KEY] ?: emptySet()
            prefs[HISTORY_KEY] = current - item
        }
    }

    suspend fun clearHistory(context: Context) {
        context.dataHistoryStore.edit { prefs->
            prefs.remove(HISTORY_KEY)
        }
    }
}