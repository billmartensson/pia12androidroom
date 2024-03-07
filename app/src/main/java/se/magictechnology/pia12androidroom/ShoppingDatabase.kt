package se.magictechnology.pia12androidroom

import android.content.Context
import androidx.room.ColumnInfo
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Entity
data class Shopitem(
    @PrimaryKey(autoGenerate = true) val uid: Int,
    @ColumnInfo(name = "title") val title: String?,
    @ColumnInfo(name = "amount") val amount: Int?,
    @ColumnInfo(name = "bought") var bought: Int?
)

@Dao
interface ShopDao {
    @Query("SELECT * FROM shopitem")
    fun getAll(): List<Shopitem>

    @Query("SELECT * FROM shopitem ORDER BY title ASC")
    fun getAllflow(): Flow<List<Shopitem>>

    @Query("SELECT * FROM shopitem WHERE amount > 10")
    fun getMany(): List<Shopitem>

    @Query("SELECT * FROM shopitem WHERE bought = 0")
    fun getNotBought(): List<Shopitem>

    @Query("SELECT * FROM shopitem WHERE bought = 1")
    fun getBought(): List<Shopitem>

    @Query("SELECT * FROM shopitem WHERE title = :shopname LIMIT 1")
    fun getByName(shopname : String): Shopitem

    @Insert
    suspend fun insertAll(vararg shop: Shopitem)

    @Update
    suspend fun updateShop(vararg shop: Shopitem)

    @Delete
    fun delete(shop: Shopitem)

}

@Database(entities = [Shopitem::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun shopDao(): ShopDao

    companion object {
        // Singleton prevents multiple instances of database opening at the
        // same time.
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context?): AppDatabase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context!!,
                    AppDatabase::class.java,
                    "database-name"
                ).build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }

}
