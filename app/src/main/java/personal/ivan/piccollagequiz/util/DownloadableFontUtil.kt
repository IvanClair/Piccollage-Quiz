package personal.ivan.piccollagequiz.util

import android.content.Context
import android.graphics.Typeface
import android.os.Handler
import androidx.core.content.res.ResourcesCompat
import androidx.core.provider.FontRequest
import androidx.core.provider.FontsContractCompat
import androidx.lifecycle.MutableLiveData
import io.reactivex.Single
import personal.ivan.piccollagequiz.R
import personal.ivan.piccollagequiz.io.model.GoogleFontDetails
import personal.ivan.piccollagequiz.io.model.IoStatus
import javax.inject.Inject


class DownloadableFontUtil @Inject constructor(
    val context: Context,
    val handler: Handler
) {

    // Default Weight
    companion object {
        const val DEFAULT_WEIGHT = 400
    }

    /**
     * Start to download font by [FontsContractCompat]
     */
    fun start(
        fontFamily: String,
        weight: Int,
        italic: Boolean,
        ioStatus: MutableLiveData<IoStatus>,
        downloadedTypeface: MutableLiveData<Typeface>
    ) {
        // query
        val query = "name=$fontFamily&weight=$weight&italic=${if (italic) 1.0 else 0.0}"
        val request = FontRequest(
            "com.google.android.gms.fonts",
            "com.google.android.gms",
            query,
            R.array.com_google_android_gms_fonts_certs
        )

        // callback
        val callback = object : FontsContractCompat.FontRequestCallback() {
            override fun onTypefaceRetrieved(typeface: Typeface?) {
                super.onTypefaceRetrieved(typeface)
                ioStatus.value = IoStatus.SUCCESS
                typeface?.also { downloadedTypeface.value = it }
            }

            override fun onTypefaceRequestFailed(reason: Int) {
                super.onTypefaceRequestFailed(reason)
                ioStatus.value = IoStatus.FAIL
            }
        }

        // start request
        FontsContractCompat.requestFont(context, request, callback, handler)
    }

    /**
     * RxJava version
     */
    fun startRx(
        fontFamily: String,
        weight: Int,
        italic: Boolean
    ): Single<Typeface> = Single.create { emitter ->
        // query
        val query = "name=$fontFamily&weight=$weight&italic=${if (italic) 1.0 else 0.0}"
        val request = FontRequest(
            "com.google.android.gms.fonts",
            "com.google.android.gms",
            query,
            R.array.com_google_android_gms_fonts_certs
        )

        // callback
        val callback = object : FontsContractCompat.FontRequestCallback() {
            override fun onTypefaceRetrieved(typeface: Typeface?) {
                super.onTypefaceRetrieved(typeface)
                typeface?.let { emitter.onSuccess(it) }
            }

            override fun onTypefaceRequestFailed(reason: Int) {
                super.onTypefaceRequestFailed(reason)
                emitter.onError(Exception())
            }
        }

        // start request
        FontsContractCompat.requestFont(context, request, callback, handler)
    }
}

/**
 * get weight from variant
 */
fun String.getWeight(): Int {
    val replacedString = replace(GoogleFontDetails.VARIANT_REGULAR, "")
        .replace(GoogleFontDetails.VARIANT_ITALIC, "")
    return replacedString.toIntOrNull() ?: DownloadableFontUtil.DEFAULT_WEIGHT
}

/**
 * Check the variant is italic or not
 */
fun String.isItalic(): Boolean = contains(GoogleFontDetails.VARIANT_ITALIC)

/**
 * Get [Typeface] from resources by family name
 */
fun String.getTypeface(context: Context): Typeface? {
    val fontResId = context.resources.getIdentifier(
        this,
        "font",
        context.packageName
    )
    return try {
        ResourcesCompat.getFont(context, fontResId)
    } catch (e: Exception) {
        null
    }
}

