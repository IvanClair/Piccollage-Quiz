package personal.ivan.piccollagequiz.binding_model

import personal.ivan.piccollagequiz.io.model.GoogleFontDetails
import personal.ivan.piccollagequiz.util.getWeight
import personal.ivan.piccollagequiz.util.isItalic

data class FontVhBindingModel(
    val fontFamily: String,
    val variantName: String,
    val width: Int,
    val italic: Boolean
) {
    constructor(
        data: GoogleFontDetails,
        variant: String
    ) : this(
        fontFamily = data.family ?: "",
        variantName = variant,
        width = variant.getWeight(),
        italic = variant.isItalic()
    )
}

