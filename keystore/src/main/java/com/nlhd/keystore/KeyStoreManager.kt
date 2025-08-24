package com.nlhd.keystore

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

val Context.dataStore by preferencesDataStore(name = "keystore")

object KeyStoreManager
{
    private val KEY_STORE = stringPreferencesKey("key_store")
    private val IS_ADMIN = booleanPreferencesKey("is_admin")

    suspend fun saveKeyStore(context: Context, keyStore: String) {
        context.dataStore.edit { preferences ->
            preferences[KEY_STORE] = keyStore
        }
    }

    fun getKeyStore(context: Context): Flow<String> {
        return context.dataStore.data.map { preferences->
            preferences[KEY_STORE] ?: "Nothing"
        }

    }

    suspend fun clearKeyStore(context: Context) {
        context.dataStore.edit { preferences->
            preferences.remove(KEY_STORE)
            preferences.remove(IS_ADMIN)
        }
    }

    suspend fun saveIsAdmin(context: Context, isAdmin: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[IS_ADMIN] = isAdmin
        }
    }

    // Lấy isAdmin
    fun getIsAdmin(context: Context): Flow<Boolean> {
        return context.dataStore.data.map { preferences ->
            preferences[IS_ADMIN] ?: false
        }
    }



}