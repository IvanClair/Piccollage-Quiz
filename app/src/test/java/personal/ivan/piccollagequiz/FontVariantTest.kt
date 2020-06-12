package personal.ivan.piccollagequiz

import org.junit.Assert
import org.junit.Test
import personal.ivan.piccollagequiz.io.model.GoogleFontDetails
import personal.ivan.piccollagequiz.util.DownloadableFontUtil
import personal.ivan.piccollagequiz.util.getWeight
import personal.ivan.piccollagequiz.util.isItalic

class FontQueryTest {

    // region Weight

    @Test
    fun getWeight_shouldReturnWeight_whenVariantContainsWeight() {
        val expected = 700

        // pure weight
        var actual = "700".getWeight()
        Assert.assertEquals(expected, actual)

        // mixed
        actual = "700italic".getWeight()
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun getWeight_shouldReturnDefaultWeight_whenVariantDoesNotContainsWeight() {
        val expected = DownloadableFontUtil.DEFAULT_WEIGHT

        // regular only
        var actual = GoogleFontDetails.VARIANT_REGULAR.getWeight()
        Assert.assertEquals(expected, actual)

        // italic only
        actual = GoogleFontDetails.VARIANT_ITALIC.getWeight()
        Assert.assertEquals(expected, actual)

        // random
        actual = "random700".getWeight()
        Assert.assertEquals(expected, actual)
    }

    // endregion

    // region Italic

    @Test
    fun isItalic_shouldReturnTrue_whenVariantContainsItalic() {
        val expected = true

        // keyword only
        var actual = GoogleFontDetails.VARIANT_ITALIC.isItalic()
        Assert.assertEquals(expected, actual)

        // mixed
        actual = "700italic".isItalic()
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun isItalic_shouldReturnFalse_whenVariantDoesNotContainsItalic() {
        val expected = false

        // regular only
        var actual = GoogleFontDetails.VARIANT_REGULAR.isItalic()
        Assert.assertEquals(expected, actual)

        // weight only
        actual = "700".isItalic()
        Assert.assertEquals(expected, actual)

        // random
        actual = "random700".isItalic()
        Assert.assertEquals(expected, actual)
    }

    // endregion
}