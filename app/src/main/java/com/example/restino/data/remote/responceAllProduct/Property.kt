package  com.example.restino.data.remote.responceAllProduct

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Property(
    val name: String,
    val value: String
) : Parcelable