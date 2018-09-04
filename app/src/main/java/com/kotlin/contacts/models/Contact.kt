package com.kotlin.contacts.models

import java.io.Serializable


data class Contact(var name:String,
                   var id: Int,
                   var companyName: String,
                   var isFavorite: Boolean,
                   var smallImageURL: String,
                   var largeImageURL: String,
                   var emailAddress: String,
                   var birthdate: String,
                   var phone: Phone,
                   var address: Address) : Serializable