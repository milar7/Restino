package  com.example.restino.data.remote.responceAllProduct

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ProductsItem(
    val brand: Brand,
    val categories: List<Category>,
    val description: String,
    val id: Int,
    val image: String,
    val inventory: Int,
    val max_limit: Int,
    val min_limit: Int,
    val name: String,
    val price: Int,
    val properties: List<Property>
) : Parcelable