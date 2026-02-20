package com.simats.pathpiolet.data

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.simats.pathpiolet.data.College

object SavedCollegeManager {
    private const val PREF_NAME = "saved_colleges_pref"
    private const val KEY_SAVED_COLLEGES = "saved_colleges_list"

    private fun getPrefs(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    fun saveCollege(context: Context, college: College) {
        val savedList = getSavedColleges(context).toMutableList()
        // Check if already saved to avoid duplicates
        if (savedList.none { it.instituteId == college.instituteId && it.name == college.name }) {
             // Update the isSaved flag before saving
            val collegeToSave = college.copy(isSaved = true)
            savedList.add(collegeToSave)
            saveList(context, savedList)
        }
    }

    fun removeCollege(context: Context, college: College) {
        val savedList = getSavedColleges(context).toMutableList()
        savedList.removeAll { it.instituteId == college.instituteId && it.name == college.name }
        saveList(context, savedList)
    }
    
    fun isSaved(context: Context, college: College): Boolean {
         val savedList = getSavedColleges(context)
         return savedList.any { it.instituteId == college.instituteId && it.name == college.name }
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
}
