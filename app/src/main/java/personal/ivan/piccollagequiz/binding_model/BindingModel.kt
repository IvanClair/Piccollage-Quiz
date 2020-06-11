package personal.ivan.piccollagequiz.binding_model

import personal.ivan.piccollagequiz.io.model.GoogleFontDetails

enum class FontStatus {
    WAITING, DOWNLOADING, FINISH, FAIL
}

data class FontVhBindingModel(
    val fontFamily: String,
    // todo check download status
    val status: FontStatus
) {
    constructor(
        data: GoogleFontDetails,
        variantIndex: Int
    ) : this(
        fontFamily = "${data.family} ${data.variantList?.getOrElse(variantIndex) { "" }}",
        status = FontStatus.WAITING
    )
}