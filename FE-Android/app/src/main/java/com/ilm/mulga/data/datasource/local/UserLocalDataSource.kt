package com.ilm.mulga.data.datasource.local

import android.content.Context
import android.content.SharedPreferences

class UserLocalDataSource(private val context: Context) {
    private val preferences: SharedPreferences by lazy {
        context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
    }

    fun saveToken(token: String) {
        preferences.edit().putString("firebase_token", token).apply()
    }

    fun getToken(): String? {
        return preferences.getString("firebase_token", null)
    }
}