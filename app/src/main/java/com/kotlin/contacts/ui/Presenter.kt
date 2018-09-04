package com.kotlin.contacts.ui

interface Presenter
{
    abstract fun onCreate()
    abstract fun onStart()
    abstract fun onPause()
    abstract fun onResume()
    abstract fun onDestroy()

}