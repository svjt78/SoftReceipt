package com.example.debalina.softreceipt;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Debalina on 8/30/2015.
 */
public class AccountProfile implements Parcelable {

    private String _password;

    private String _email;


    //blank constructor
    public AccountProfile() {

    }

    //constructor
    public AccountProfile(String password, String email) {

        this._password = password;
        this._email = email;
    }

    //getters

    public void setpassword(String password) {

        this._password = password;
    }
    public void setemail (String email) {

        this._email = email;
    }

    //getters
    public String getpassword() {

        return this._password;
    }
    public String getemail() {

        return this._email;
    }

    // Parcelling part
    public AccountProfile(Parcel in) {
        String[] data = new String[2];

        this._password = in.readString();
        this._email = in.readString();
    }
    @Override
    public void writeToParcel(Parcel dest, int flags) {

//        dest.writeInt(this._id);
        dest.writeString(this._password);
        dest.writeString(this._email);

    }
    @Override
    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public AccountProfile createFromParcel(Parcel in) {
            return new AccountProfile(in);
        }

        public AccountProfile[] newArray(int size) {
            return new AccountProfile[size];
        }
    };
}
