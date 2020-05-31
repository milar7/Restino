package  com.example.restino.data.remote.responceAllProduct

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Category(
    val description: String,
    val name: String
) : Parcelable