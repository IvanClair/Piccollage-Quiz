package personal.ivan.piccollagequiz

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import personal.ivan.piccollagequiz.util.getTypeface

@RunWith(AndroidJUnit4::class)
class TypefaceTest {

    // Context
    private val context = InstrumentationRegistry.getInstrumentation().targetContext

    // region font exist in resources

    @Test
    fun getTypeface_shouldNotBeNull_whenFontExistInResources() {
        val expected = null
        val actual = "abeezee".getTypeface(context = context)
        Assert.assertNotEquals(expected, actual)
    }

    // endregion

    // region font does not exist in resources

    @Test
    fun getTypeface_shouldBeNull_whenFontDoesNotExistInResources() {
        val expected = null
        val actual = "123".getTypeface(context = context)
        Assert.assertEquals(expected, actual)
    }

    // endregion
}