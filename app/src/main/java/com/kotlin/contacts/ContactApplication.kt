package com.kotlin.contacts

import android.app.Application
import android.content.Context

class ContactApplication : Application()
{
    override fun onCreate()
    {
        super.onCreate()
        instance = this
    }

    companion object {
        lateinit var instance: ContactApplication
          private set
    }
}