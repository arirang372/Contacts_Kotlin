package com.kotlin.contacts.data

import com.kotlin.contacts.ContactApplication
import com.kotlin.contacts.models.Contact
import com.kotlin.contacts.rest.RemoteDataLoader
import rx.Observable
import rx.subjects.BehaviorSubject

class DataManager
{
    lateinit var dataLoader : RemoteDataLoader
    var networkLoading : BehaviorSubject<Boolean> = BehaviorSubject.create()
    val TAG : String = javaClass.simpleName
    lateinit var preferenceManager: PreferenceManager

    init{
        dataLoader = RemoteDataLoader()
        preferenceManager = PreferenceManager( ContactApplication.instance)
    }

    fun networkInUse() : Observable<Boolean> = networkLoading.asObservable()

    fun getNetworkLoadingSubject() : BehaviorSubject<Boolean> = networkLoading

    fun getAllContacts() : Observable<MutableList<Contact>>
            = dataLoader.loadAllContacts(this, networkLoading)
}