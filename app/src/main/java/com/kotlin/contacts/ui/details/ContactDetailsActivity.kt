package com.kotlin.contacts.ui.details

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.text.TextUtils
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.kotlin.contacts.R
import com.kotlin.contacts.models.Contact
import com.kotlin.contacts.models.Model
import com.kotlin.contacts.models.Phone
import java.io.Serializable


class ContactDetailsActivity : AppCompatActivity() {
    lateinit var presenter: ContactDetailsPresenter
    lateinit var toolBar: Toolbar

    lateinit var txtContactName: TextView
    lateinit var txtCompanyName: TextView

    lateinit var work_phone_layout: RelativeLayout
    lateinit var txtWorkPhoneNumber: TextView

    lateinit var home_phone_layout: RelativeLayout
    lateinit var txtHomePhoneNumber: TextView

    lateinit var mobile_phone_layout: RelativeLayout
    lateinit var txtMobilePhoneNumber: TextView

    lateinit var txtAddress: TextView
    lateinit var txtBirthDate: TextView
    lateinit var txtEmail: TextView
    lateinit var iv_contact: ImageView

    lateinit var btnFavorite: MenuItem
    lateinit var contact: Contact

    companion object {
        val CONTACT = "contact"
        fun startActivity(context: Activity, contact: Serializable) {
            var b = Bundle()
            b.putSerializable(CONTACT, contact)
            var intent = Intent(context, ContactDetailsActivity::class.java)
            intent.putExtras(b)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contact_details)
        init()
    }

    fun init() {
        var b = intent.extras
        contact = b?.getSerializable(CONTACT) as Contact
        presenter = ContactDetailsPresenter(this, Model, contact)

        with(this)
        {
            toolBar = findViewById<Toolbar>(R.id.toolbar)
            setSupportActionBar(toolBar)
            txtContactName = findViewById<TextView>(R.id.txtContactName)
            txtCompanyName = findViewById<TextView>(R.id.txtCompanyName)

            work_phone_layout = findViewById<RelativeLayout>(R.id.work_phone_layout)
            txtWorkPhoneNumber = findViewById<TextView>(R.id.txtWorkPhoneNumber)

            home_phone_layout = findViewById<RelativeLayout>(R.id.home_phone_layout)
            txtHomePhoneNumber = findViewById<TextView>(R.id.txtHomePhoneNumber)

            mobile_phone_layout = findViewById<RelativeLayout>(R.id.mobile_phone_layout)
            txtMobilePhoneNumber = findViewById<TextView>(R.id.txtMobilePhoneNumber)
            txtAddress = findViewById<TextView>(R.id.txtAddress)
            txtBirthDate = findViewById<TextView>(R.id.txtBirthDate)
            txtEmail = findViewById<TextView>(R.id.txtEmail)

            iv_contact = findViewById<ImageView>(R.id.iv_contact)
        }


        presenter.onCreate()
    }

    fun setupToolbar() {
        setSupportActionBar(toolBar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    fun bindData(contact: Contact) {
        var requestOptions = RequestOptions()
        requestOptions.run {
            placeholder(R.drawable.user_large)
            error(R.drawable.user_large)
        }

        Glide.with(this)
                .setDefaultRequestOptions(requestOptions)
                .load(contact.largeImageURL)
                .into(iv_contact)

        with(contact)
        {
            txtContactName.text = name
            txtCompanyName.text = companyName
            bindPhone(phone)

            txtAddress.text = String.format("%s %s, %s %s, US",
                    address.street, address.city, address.state, address.zipCode)

            txtBirthDate.text = birthdate
            txtEmail.text = emailAddress
        }
    }


    fun bindPhone(phone: Phone) {
        if (TextUtils.isEmpty(phone.work))
            work_phone_layout.visibility = View.GONE
        else {
            work_phone_layout.visibility = View.VISIBLE
            txtWorkPhoneNumber.text = phone.work
        }

        if (TextUtils.isEmpty(phone.home))
            home_phone_layout.visibility = View.GONE
        else {
            home_phone_layout.visibility = View.VISIBLE
            txtHomePhoneNumber.text = phone.home
        }

        if (TextUtils.isEmpty(phone.mobile)) {
            mobile_phone_layout.visibility = View.GONE
        } else {
            mobile_phone_layout.visibility = View.VISIBLE
            txtMobilePhoneNumber.text = phone.mobile
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.detail_menu, menu)
        btnFavorite = menu?.findItem(R.id.btnFavorite) as MenuItem
        btnFavorite.setIcon(if (contact.isFavorite) R.drawable.favorite_true else R.drawable.favorite_false)

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.btnFavorite -> {
                if (contact.isFavorite) {
                    contact.isFavorite = false;
                    btnFavorite.setIcon(R.drawable.favorite_false)
                } else {
                    contact.isFavorite = true
                    btnFavorite.setIcon(R.drawable.favorite_true)
                }
                presenter.updateFavorite(contact)
            }
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onPause() {
        super.onPause()
        presenter.onPause()
    }

    override fun onResume() {
        super.onResume()
        presenter.onResume()
    }

    override fun onDestroy() {
        super.onDestroy()


    }

}