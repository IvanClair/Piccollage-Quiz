package personal.ivan.piccollagequiz.binding_model

import personal.ivan.piccollagequiz.io.model.GoogleFontDetails

enum class FontStatus {
    WAITING, DOWNLOADING, FINISH, FAIL
}

data class FontVhBindingModel(
    val fontFamily: String,
    val style: String,
    val status: FontStatus,
    val downloadLink: String
) {
    constructor(
        data: GoogleFontDetails,
        variant: String
    ) : this(
        fontFamily = data.family ?: "",
        style = variant,
        status = FontStatus.WAITING,
        downloadLink = data.downloadUrlMap?.get(variant) ?: ""
    )
}