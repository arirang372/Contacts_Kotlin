package com.kotlin.contacts.utils

import android.util.Log
import com.kotlin.contacts.BuildConfig


object LogUtils
{
    var LOG_PREFIX = "log";
    var LOG_PREFIX_LENGTH = LOG_PREFIX.length;
    var MAX_LOG_TAG_LENGTH = 23;

    fun LOGD(tag: String, message: String)
    {
        if(BuildConfig.DEBUG)
        {
            Log.d(tag, message);
        }
    }

    fun LOGD(tag: String, message: String, cause: Throwable)
    {
        if(BuildConfig.DEBUG)
        {
            Log.d(tag, message, cause);
        }
    }
}