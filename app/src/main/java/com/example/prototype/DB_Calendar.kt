package com.example.prototype

import android.os.Parcel
import android.os.Parcelable

class DB_Calendar(var year : String? , var month : String? , var day : String? , var formatted : String?) : Parcelable{
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(year)
        parcel.writeString(month)
        parcel.writeString(day)
        parcel.writeString(formatted)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<DB_Calendar> {
        override fun createFromParcel(parcel: Parcel): DB_Calendar {
            return DB_Calendar(parcel)
        }

        override fun newArray(size: Int): Array<DB_Calendar?> {
            return arrayOfNulls(size)
        }
    }
}
