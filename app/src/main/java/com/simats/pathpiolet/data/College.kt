package com.simats.pathpiolet.data

import android.os.Parcelable


data class College(
    val rank: Int,
    val name: String,
    val city: String,
    val state: String,
    val score: Double,
    val nirfRank: Int,
    val fees: String = "₹ 2.5L - 5L/yr",
    val avgPackage: String = "₹ 8 LPA",
    val instituteId: String = "IR-O-U-XXXX",
    val matchScore: Int = 0,
    val tags: List<String> = emptyList(),
    var isSaved: Boolean = false,
    val facilities: List<String> = emptyList()
) : Parcelable {
    constructor(parcel: android.os.Parcel) : this(
        parcel.readInt(),
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readDouble(),
        parcel.readInt(),
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readInt(),
        parcel.createStringArrayList() ?: emptyList(),
        parcel.readByte() != 0.toByte(),
        parcel.createStringArrayList() ?: emptyList()
    )

    override fun writeToParcel(parcel: android.os.Parcel, flags: Int) {
        parcel.writeInt(rank)
        parcel.writeString(name)
        parcel.writeString(city)
        parcel.writeString(state)
        parcel.writeDouble(score)
        parcel.writeInt(nirfRank)
        parcel.writeString(fees)
        parcel.writeString(avgPackage)
        parcel.writeString(instituteId)
        parcel.writeInt(matchScore)
        parcel.writeStringList(tags)
        parcel.writeByte(if (isSaved) 1 else 0)
        parcel.writeStringList(facilities)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<College> {
        override fun createFromParcel(parcel: android.os.Parcel): College {
            return College(parcel)
        }

        override fun newArray(size: Int): Array<College?> {
            return arrayOfNulls(size)
        }
    }
}
