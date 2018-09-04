package com.kotlin.contacts.ui.details

import com.kotlin.contacts.models.Contact
import com.kotlin.contacts.models.Model
import com.kotlin.contacts.ui.Presenter


class ContactDetailsPresenter(activity: ContactDetailsActivity, model: Model, contact: Contact)
    : Presenter
{

    val view : ContactDetailsActivity = activity
    val model : Model = model
    val contact : Contact = contact

    fun updateFavorite(contact: Contact)
    {
        var contacts : List<Contact> = model.contacts
        if(!contacts?.isEmpty())
        {
            var indexToUpdate = 0
            for( (index, value) in contacts.withIndex())
            {
                if(value.id == contact.id)
                {
                    indexToUpdate = index
                    break
                }
            }

            model.contacts.set(indexToUpdate, contact)
        }
    }


    override fun onCreate()
    {
        view.setupToolbar()
        view.bindData(contact)
    }


    override fun onStart()
    {

    }


    override fun onResume()
    {

    }


    override fun onPause()
    {

    }

    override fun onDestroy()
    {

    }


}