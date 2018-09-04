package com.kotlin.contacts.ui.list

import com.kotlin.contacts.models.Contact
import com.kotlin.contacts.models.Model
import com.kotlin.contacts.ui.Presenter
import java.util.Collections
import java.util.Comparator
import rx.Observable
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import rx.subscriptions.CompositeSubscription

class ContactListPresenter(activity: ContactListActivity, model: Model) : Presenter
{
    lateinit var loaderSubscription: Subscription
    lateinit var dataSubscription: Subscription
    var subscriptions : CompositeSubscription = CompositeSubscription()
    var view : ContactListActivity = activity
    var model : Model = model

    override fun onCreate()
    {
        if(!model.contacts?.isEmpty())
        {
            buildSections(model?.contacts)
        }
        else
        {
            model.getNetworkLoading().onNext(true)
            dataSubscription = model.getAllContacts()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe { contacts ->
                        run {
                            model.getNetworkLoading().onNext(false)
                            model.contacts = contacts
                            buildSections(contacts)
                        }
                    }
        }
    }

    private fun buildSections(contacts : MutableList<Contact>)
    {
        subscriptions.add(Observable.just<List<Contact>>(contacts)
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .flatMap { contacts ->
                                        Collections.sort(contacts) {
                                                c1, c2 -> c1.name.compareTo(c2.name)
                                        }
                                        Observable.from(contacts)
                                    }
                                    .filter {
                                        contact -> contact.isFavorite
                                    }
                                    .toList()
                                    .subscribe {
                                        contacts -> view.favorites = contacts
                                    })

        subscriptions.add(Observable.just<List<Contact>>(contacts)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap { contacts->
                        Collections.sort(contacts){
                             c1, c2-> c1.name.compareTo(c2.name)
                        }
                        Observable.from(contacts)
                }.filter {
                    contact-> !contact.isFavorite
                }.toList()
                .doOnCompleted {
                    view.setupUI()
                }
                .subscribe{
                    contacts -> view.otherContacts = contacts
                })
    }

    override fun onStart()
    {

    }

    override fun onPause()
    {
        loaderSubscription?.unsubscribe()
        subscriptions?.unsubscribe()
    }

    override fun onResume()
    {
        loaderSubscription = model.isNetworkUsed()
                .subscribe {
                        networkInUse -> view.showNetworkLoading(networkInUse)
                }
    }

    override fun onDestroy()
    {

    }

}