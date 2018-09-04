package com.kotlin.contacts.rest

import com.kotlin.contacts.models.Contact
import rx.Observable
import retrofit2.http.GET

interface ContactService
{
    @GET("technical-challenge/v3/contacts.json")
    fun getContacts() : Observable<MutableList<Contact>>

}