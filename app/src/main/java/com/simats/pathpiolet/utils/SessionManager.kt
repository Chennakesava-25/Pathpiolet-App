package com.simats.pathpiolet.utils

import android.content.Context
import android.content.SharedPreferences

class SessionManager(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("user_session", Context.MODE_PRIVATE)

    companion object {
        private const val KEY_USER_ID = "user_id"
        private const val KEY_USERNAME = "username"
        private const val KEY_EMAIL = "email"
        private const val KEY_PHONE = "phone"
        private const val KEY_AGE = "age"
        private const val KEY_EDUCATION = "education_level"
        private const val KEY_INTERESTED = "interested_field"
        private const val KEY_PROFILE_PIC = "profile_picture"
        private const val KEY_NOTIFICATIONS = "notifications_enabled"
    }

    fun saveUser(userId: Int, username: String, email: String, phone: String? = null, age: Int? = null, education: String? = null, interested: String? = null, profilePic: String? = null) {
        prefs.edit().apply {
            putInt(KEY_USER_ID, userId)
            putString(KEY_USERNAME, username)
            putString(KEY_EMAIL, email)
            putString(KEY_PHONE, phone)
            age?.let { putInt(KEY_AGE, it) }
            putString(KEY_EDUCATION, education)
            putString(KEY_INTERESTED, interested)
            putString(KEY_PROFILE_PIC, profilePic)
            apply()
        }
    }

    fun updateProfile(username: String, phone: String, age: Int, education: String, interested: String) {
        prefs.edit().apply {
            putString(KEY_USERNAME, username)
            putString(KEY_PHONE, phone)
            putInt(KEY_AGE, age)
            putString(KEY_EDUCATION, education)
            putString(KEY_INTERESTED, interested)
            apply()
        }
    }

    fun updateUsername(username: String) {
        prefs.edit().putString(KEY_USERNAME, username).apply()
    }

    fun getUserId(): Int = prefs.getInt(KEY_USER_ID, -1)
    fun getUsername(): String = prefs.getString(KEY_USERNAME, "Student") ?: "Student"
    fun getEmail(): String = prefs.getString(KEY_EMAIL, "") ?: ""
    fun getPhone(): String = prefs.getString(KEY_PHONE, "") ?: ""
    fun getAge(): Int = prefs.getInt(KEY_AGE, 0)
    fun getEducation(): String = prefs.getString(KEY_EDUCATION, "") ?: ""
    fun getInterested(): String = prefs.getString(KEY_INTERESTED, "") ?: ""
    fun getProfilePicture(): String? = prefs.getString(KEY_PROFILE_PIC, null)

    fun saveProfilePicture(url: String) {
        prefs.edit().putString(KEY_PROFILE_PIC, url).apply()
    }

    fun setNotificationsEnabled(enabled: Boolean) {
        prefs.edit().putBoolean(KEY_NOTIFICATIONS, enabled).apply()
    }

    fun isNotificationsEnabled(): Boolean = prefs.getBoolean(KEY_NOTIFICATIONS, true)

    fun clearSession() {
        prefs.edit().clear().apply()
    }
}
