package com.kirtanlabs.nammaapartments;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;

import java.util.Objects;

/**
 * KirtanLabs Pvt. Ltd.
 * Created by Roshan Halwai on 6/22/2018
 */
public class ContactPicker {

    /* ------------------------------------------------------------- *
     * Private Members
     * ------------------------------------------------------------- */

    private Uri uriContact;
    private Context context;

    /* ------------------------------------------------------------- *
     * Constructor
     * ------------------------------------------------------------- */

    public ContactPicker(Context context, Uri uriContact) {
        this.context = context;
        this.uriContact = uriContact;
    }

    /**
     * The contact number of person selected by the user from the contacts list
     *
     * @return contact number
     */
    public String retrieveContactNumber() {
        String contactNumber = null;
        Cursor cursorPhone = context.getContentResolver().query(uriContact, null, null, null, null);
        if (Objects.requireNonNull(cursorPhone).moveToFirst()) {
            contactNumber = cursorPhone.getString(cursorPhone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
        }
        cursorPhone.close();
        String formattedPhoneNumber = Objects.requireNonNull(contactNumber).replaceAll("\\D+", "");
        if (formattedPhoneNumber.startsWith("91") && formattedPhoneNumber.length() > 10) {
            formattedPhoneNumber = formattedPhoneNumber.substring(2, 12);
        }
        return formattedPhoneNumber;
    }

    /**
     * The contact name of person selected by the user from the contacts list
     *
     * @return contact name
     */
    public String retrieveContactName() {
        String contactName = null;
        Cursor cursor = context.getContentResolver().query(uriContact, null, null, null, null);
        if (Objects.requireNonNull(cursor).moveToFirst()) {
            contactName = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
        }
        cursor.close();
        return contactName;
    }

}
