package hu.pk.passworthy.data

import android.util.Log
import androidx.room.*

@Dao
interface ItemDao {
    @Query("SELECT * FROM item")
    fun getAll(): List<Item>

    @Insert
    fun insert(items: Item): Long

    @Update
    fun update(item: Item)

    @Delete
    fun deleteItem(item: Item)

    @Query("DELETE FROM item")
    fun deleteAll()

    @Query("SELECT COUNT(id) FROM item WHERE lastModified < :currMinusGiven")
    fun getNumbersOfOldPasswords(currMinusGiven: Long): Int

}