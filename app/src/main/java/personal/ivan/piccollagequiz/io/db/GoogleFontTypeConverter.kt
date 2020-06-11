package personal.ivan.piccollagequiz.io.db

import androidx.room.TypeConverter
import com.squareup.moshi.*

class GoogleFontTypeConverter {

    // region Public Functions

    @TypeConverter
    fun variantListToString(dataList: List<String>?): String =
        dataList
            ?.let { toJson(dataList = dataList) }
            ?: ""

    @TypeConverter
    fun stringToVariantList(data: String): List<String>? =
        fromJsonToList(data = data)

    @TypeConverter
    fun downloadUrlMapToString(dataMap: Map<String, String>?): String =
        dataMap
            ?.let { toJson(dataMap = dataMap) }
            ?: ""

    @TypeConverter
    fun stringToDownloadUrlMap(data: String): Map<String, String>? =
        fromJsonToMap(data = data)

    // endregion

    // region Private Functions

    @ToJson
    private inline fun <reified T> toJson(dataList: List<T>): String =
        createListAdapter<T>().toJson(dataList)

    @ToJson
    private inline fun <reified K, reified V> toJson(dataMap: Map<K, V>): String =
        createMapAdapter<K, V>().toJson(dataMap)

    @FromJson
    private inline fun <reified T> fromJsonToList(data: String): List<T>? =
        createListAdapter<T>().fromJson(data)

    @FromJson
    private inline fun <reified K, reified V> fromJsonToMap(data: String): Map<K, V>? =
        createMapAdapter<K, V>().fromJson(data)

    private inline fun <reified T> createListAdapter(): JsonAdapter<List<T>> =
        Moshi.Builder()
            .build()
            .adapter(Types.newParameterizedType(List::class.java, T::class.java))


    private inline fun <reified K, reified V> createMapAdapter(): JsonAdapter<Map<K, V>> {
        return Moshi.Builder()
            .build()
            .adapter(
                Types.newParameterizedType(
                    Map::class.java,
                    K::class.java,
                    V::class.java
                )
            )
    }

    // endregion
}