package com.kotlin.contacts.models

import java.io.Serializable


data class Address(var street: String,
                   var city: String,
                   var state: String,
                   var country: String,
                   var zipCode: String) : Serializable
