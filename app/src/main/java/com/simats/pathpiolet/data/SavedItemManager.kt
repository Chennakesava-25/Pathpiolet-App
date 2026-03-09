package com.simats.pathpiolet.data

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.simats.pathpiolet.data.College

object SavedItemManager {
    private const val PREF_NAME = "saved_items_pref"
    private const val KEY_SAVED_COLLEGES = "saved_colleges_list"
    private const val KEY_SAVED_ROADMAPS = "saved_roadmaps_ids"
    private const val KEY_SAVED_CAREERS = "saved_careers_ids"

    private fun getPrefs(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    // --- Colleges (Legacy Support) ---

    fun saveCollege(context: Context, userId: Int, college: College) {
        val savedList = getSavedColleges(context).toMutableList()
        if (savedList.none { it.id == college.id }) {
            val collegeToSave = college.copy(isSaved = true)
            savedList.add(collegeToSave)
            saveList(context, savedList)
            
            // Sync with backend
            if (userId != -1) {
                com.simats.pathpiolet.api.RetrofitClient.instance.saveCollege(
                    com.simats.pathpiolet.api.SaveCollegeRequest(userId, college.id)
                ).enqueue(object : retrofit2.Callback<com.simats.pathpiolet.api.AuthResponse> {
                    override fun onResponse(call: retrofit2.Call<com.simats.pathpiolet.api.AuthResponse>, response: retrofit2.Response<com.simats.pathpiolet.api.AuthResponse>) {}
                    override fun onFailure(call: retrofit2.Call<com.simats.pathpiolet.api.AuthResponse>, t: Throwable) {}
                })
            }
        }
    }

    fun removeCollege(context: Context, userId: Int, college: College) {
        val savedList = getSavedColleges(context).toMutableList()
        savedList.removeAll { it.id == college.id }
        saveList(context, savedList)
        
        // Sync with backend
        if (userId != -1) {
            com.simats.pathpiolet.api.RetrofitClient.instance.unsaveCollege(userId, college.id)
                .enqueue(object : retrofit2.Callback<com.simats.pathpiolet.api.AuthResponse> {
                    override fun onResponse(call: retrofit2.Call<com.simats.pathpiolet.api.AuthResponse>, response: retrofit2.Response<com.simats.pathpiolet.api.AuthResponse>) {}
                    override fun onFailure(call: retrofit2.Call<com.simats.pathpiolet.api.AuthResponse>, t: Throwable) {}
                })
        }
    }
    
    fun isSaved(context: Context, college: College): Boolean {
         val savedList = getSavedColleges(context)
         return savedList.any { it.id == college.id }
    }

    fun getSavedColleges(context: Context): List<College> {
        val prefs = getPrefs(context)
        val json = prefs.getString(KEY_SAVED_COLLEGES, null)
        return if (json != null) {
            val type = object : TypeToken<List<College>>() {}.type
            Gson().fromJson(json, type)
        } else {
            emptyList()
        }
    }

    private fun saveList(context: Context, list: List<College>) {
        val prefs = getPrefs(context)
        val editor = prefs.edit()
        val json = Gson().toJson(list)
        editor.putString(KEY_SAVED_COLLEGES, json)
        editor.apply()
    }

    // --- Generic Items (Roadmaps, Careers) ---

    fun toggleLike(context: Context, type: String, itemId: String): Boolean {
        val key = when(type) {
            "Roadmap" -> KEY_SAVED_ROADMAPS
            "Career" -> KEY_SAVED_CAREERS
            else -> return false
        }
        val prefs = getPrefs(context)
        val savedSet = prefs.getStringSet(key, mutableSetOf())?.toMutableSet() ?: mutableSetOf()
        
        val isLiked = if (savedSet.contains(itemId)) {
            savedSet.remove(itemId)
            false
        } else {
            savedSet.add(itemId)
            true
        }
        
        prefs.edit().putStringSet(key, savedSet).apply()
        return isLiked
    }

    fun isLiked(context: Context, type: String, itemId: String): Boolean {
        val key = when(type) {
            "Roadmap" -> KEY_SAVED_ROADMAPS
            "Career" -> KEY_SAVED_CAREERS
            else -> return false
        }
        return getPrefs(context).getStringSet(key, emptySet())?.contains(itemId) == true
    }
}
