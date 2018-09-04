package com.kotlin.contacts.models

import com.kotlin.contacts.data.DataManager
import rx.Observable
import rx.subjects.BehaviorSubject

/**
 *  This is a singleton object..
 */
object Model
{
    var contacts: MutableList<Contact>
    var dm : DataManager = DataManager()


    init {
       //var instance = this
        contacts = mutableListOf<Contact>()
    }

    fun getAllContacts() : Observable<MutableList<Contact>> = dm?.getAllContacts()

    fun isNetworkUsed()  = dm?.networkInUse().distinctUntilChanged()

    fun getNetworkLoading() : BehaviorSubject<Boolean> = dm.getNetworkLoadingSubject()
}
