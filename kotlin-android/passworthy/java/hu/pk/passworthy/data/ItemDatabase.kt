package hu.pk.passworthy.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Item::class], version = 1)
abstract class ItemDatabase : RoomDatabase() {
    abstract fun itemDao(): ItemDao

    companion object {
        fun getDatabase(applicationContext: Context) : ItemDatabase{
            return Room.databaseBuilder(
                applicationContext,
                ItemDatabase::class.java,
                "item-list"
            ).build()
        }
    }
}