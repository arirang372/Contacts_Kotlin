package com.kotlin.contacts.ui.list

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.kotlin.contacts.R
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import java.util.List
import com.kotlin.contacts.models.Contact
import com.kotlin.contacts.models.Model
import com.kotlin.contacts.ui.details.ContactDetailsActivity
import com.kotlin.contacts.ui.details.ContactDetailsActivity.Companion.CONTACT
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionParameters
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter
import io.github.luizgrp.sectionedrecyclerviewadapter.StatelessSection
import me.zhanghai.android.materialprogressbar.MaterialProgressBar
import rx.subscriptions.CompositeSubscription
import java.io.Serializable

class ContactListActivity : AppCompatActivity()
{
    lateinit var presenter : ContactListPresenter
    lateinit var toolbar : Toolbar
    lateinit var progressBar : MaterialProgressBar
    lateinit var rv_contacts : RecyclerView
    lateinit var adapter: SectionedRecyclerViewAdapter
    lateinit var subscriptions: CompositeSubscription
    lateinit var favorites : MutableList<Contact>
    lateinit var otherContacts: MutableList<Contact>

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contact_list)

        init()
    }

    fun init()
    {
        presenter = ContactListPresenter(this, Model)

        with(this)
        {
            toolbar = findViewById(R.id.toolbar)
            setSupportActionBar(toolbar)
            progressBar = findViewById(R.id.progressbar)
            progressBar.visibility = View.INVISIBLE
            rv_contacts = findViewById(R.id.rv_contacts)
        }

        adapter = SectionedRecyclerViewAdapter()
        subscriptions = CompositeSubscription()
        presenter.onCreate()
    }


    fun setupUI()
    {
        adapter.addSection(ContactsSection("FAVORITE CONTACTS", this.favorites))
        adapter.addSection(ContactsSection("OTHER CONTACTS", this.otherContacts))

        with(rv_contacts)
        {
            layoutManager = LinearLayoutManager(this@ContactListActivity)
            adapter = this@ContactListActivity.adapter
        }
    }

    inner class ContactsSection(title: String, contacts : MutableList<Contact>
    ) : StatelessSection(SectionParameters.Builder(R.layout.single_contact_rv_item)
                        .headerResourceId(R.layout.section_header).build())
    {
        var title : String = title
        var contacts : MutableList<Contact> = contacts

        override fun getItemViewHolder(view: View?): RecyclerView.ViewHolder = ItemViewHolder(view)

        override fun getHeaderViewHolder(view: View?): RecyclerView.ViewHolder = HeaderViewHolder(view)

        override fun onBindHeaderViewHolder(holder: RecyclerView.ViewHolder?)
        {
            var vh : HeaderViewHolder = holder as HeaderViewHolder
            vh.txtSectionTitle.text = title
        }

        override fun onBindItemViewHolder(holder: RecyclerView.ViewHolder?, position: Int)
        {

            var viewHolder : ItemViewHolder = holder as ItemViewHolder
            var c : Contact = contacts.get(position)
            if(c != null)
            {
                var requestOptions = RequestOptions()
                requestOptions.run {
                    placeholder(R.drawable.user_small)
                    error(R.drawable.user_small)
                }

                Glide.with(this@ContactListActivity)
                        .setDefaultRequestOptions(requestOptions)
                        .load(c.smallImageURL)
                        .into(viewHolder.iv_contact)

                with(holder)
                {
                    iv_favorite.setImageResource(if(c.isFavorite) R.drawable.favorite_true
                    else R.drawable.favorite_false)
                    txtContactName.text = c.name
                    txtCompanyName.text = c.companyName
                    viewMain.setOnClickListener {
                        //start the details activity...
                        ContactDetailsActivity.startActivity(this@ContactListActivity, c as Serializable)
                    }
                }


            }
        }

        override fun getContentItemsTotal(): Int = contacts?.size


    }

    class HeaderViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView)
    {
        var txtSectionTitle : TextView

        init {
            txtSectionTitle = itemView?.findViewById<TextView>(R.id.txtSectionTitle) as TextView
        }
    }


    class ItemViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView)
    {
        var viewMain : RelativeLayout
        var iv_contact : ImageView
        var iv_favorite : ImageView
        var txtContactName : TextView
        var txtCompanyName : TextView

        init {
            viewMain = itemView?.findViewById<RelativeLayout>(R.id.viewMain) as RelativeLayout
            iv_contact = itemView?.findViewById<ImageView>(R.id.iv_contact) as ImageView
            iv_favorite = itemView?.findViewById<ImageView>(R.id.iv_favorite) as ImageView
            txtContactName = itemView?.findViewById<TextView>(R.id.txtContactName) as TextView
            txtCompanyName = itemView?.findViewById<TextView>(R.id.txtCompanyName) as TextView
        }
    }

    override fun onPause()
    {
        super.onPause()
        presenter.onPause()
    }

    override fun onResume()
    {
        super.onResume()
        presenter.onResume()
    }

    override fun onDestroy()
    {
        super.onDestroy()
        subscriptions?.unsubscribe()
    }

    fun showNetworkLoading(networkInUse: Boolean)
    {
        progressBar.visibility = if(networkInUse) View.VISIBLE else View.INVISIBLE
    }

}
