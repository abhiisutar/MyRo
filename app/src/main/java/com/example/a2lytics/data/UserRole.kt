package com.example.a2lytics.data

import android.content.Context

object UserRole {
    private const val PREF_NAME = "user_preferences"
    private const val KEY_USER_ROLE = "user_role"

    fun setUserRole(context: Context, isStudent: Boolean) {
        val sharedPrefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        sharedPrefs.edit().putBoolean(KEY_USER_ROLE, isStudent).apply()
    }

    fun isStudent(context: Context): Boolean {
        val sharedPrefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return sharedPrefs.getBoolean(KEY_USER_ROLE, true) // Default to student for safety
    }
}
