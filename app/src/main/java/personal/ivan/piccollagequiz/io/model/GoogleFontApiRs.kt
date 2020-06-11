package personal.ivan.piccollagequiz.io.model

import com.squareup.moshi.Json

data class GoogleFontApiRs(@field:Json(name = "items") val fontList: List<GoogleFontDetails>?)

/**
 * @param downloadUrlMap key : item in [variantList]
 */
data class GoogleFontDetails(
    val family: String?,
    val category: String?,
    @field:Json(name = "variants") val variantList: List<String>?,
    @field:Json(name = "files") val downloadUrlMap: Map<String, String>?
)