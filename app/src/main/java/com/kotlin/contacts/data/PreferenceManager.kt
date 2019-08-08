package com.kotlin.contacts.data

import android.content.Context
import android.content.SharedPreferences


class PreferenceManager(c: Context)
{

    init
    {
        pref = android.preference.PreferenceManager.getDefaultSharedPreferences(c)
        editor = pref.edit()
    }

    fun put(key: String, value: Boolean)
    {
        editor.putBoolean(key, value)
        editor.apply()
    }

    fun getStringValue(key:String) : String?
    {
        return pref.getString(key, "")
    }


    companion object
    {
        lateinit var editor : SharedPreferences.Editor
            private set
        lateinit var pref : SharedPreferences
            private set
    }
}