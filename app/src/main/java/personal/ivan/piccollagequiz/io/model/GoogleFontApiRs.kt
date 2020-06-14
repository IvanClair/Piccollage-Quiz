package personal.ivan.piccollagequiz.io.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json

data class GoogleFontApiRs(@field:Json(name = "items") val fontList: List<GoogleFontDetails>?)

/**
 * @param downloadUrlMap key : item in [variantList]
 */
@Entity
data class GoogleFontDetails(
    @PrimaryKey val family: String,
    val category: String?,
    @field:Json(name = "variants") val variantList: List<String>?,
    @field:Json(name = "files") val downloadUrlMap: Map<String, String>?
) {

    companion object {
        const val VARIANT_REGULAR = "regular"
        const val VARIANT_ITALIC = "italic"
    }
}