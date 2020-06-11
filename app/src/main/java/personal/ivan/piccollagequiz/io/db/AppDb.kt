package personal.ivan.piccollagequiz.io.db

import androidx.room.*
import personal.ivan.piccollagequiz.io.model.GoogleFontDetails

// region Database

@Database(
    entities = [GoogleFontDetails::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(GoogleFontTypeConverter::class)
abstract class AppDb : RoomDatabase() {
    abstract fun googleFontDao(): GoogleFontDao
}

// endregion

// region DAO

@Dao
interface GoogleFontDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(dataList: List<GoogleFontDetails>)

    @Query("SELECT * FROM GoogleFontDetails")
    suspend fun loadAll(): List<GoogleFontDetails>
}

// endregion