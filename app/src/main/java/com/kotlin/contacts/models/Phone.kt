package com.kotlin.contacts.models

import java.io.Serializable

data class Phone(var work:String,
                 var home: String,
                 var mobile: String) : Serializable