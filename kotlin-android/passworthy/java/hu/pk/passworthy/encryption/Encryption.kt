package hu.pk.passworthy.encryption

import com.scottyab.aescrypt.AESCrypt
import hu.pk.passworthy.data.Item
import java.math.BigInteger
import java.security.MessageDigest

class Encryption {
    companion object {
        lateinit var encDecKey: String

        fun initEncDecKey(str: String){
            if(str.length < 16){
                val partialKey = "?f5(bH!pYQ"
                val remaining = 16 - str.length
                encDecKey = str+partialKey.dropLast(partialKey.length-remaining)
            } else if(str.length > 16) {
                encDecKey = str.dropLast(str.length-16)
            } else {
                encDecKey = str
            }
        }

        fun encrypt(data: String): String{
            return AESCrypt.encrypt(encDecKey, data)
        }

        fun decrypt(data: String): String{
            return AESCrypt.decrypt(encDecKey, data)
        }

        fun encryptItem(item: Item): Item{
            item.name = encrypt(item.name)
            item.description = encrypt(item.description)
            item.username = encrypt(item.username)
            item.password = encrypt(item.password)
            item.extraInfo = encrypt(item.extraInfo)
            return item
        }

        fun decryptItem(item: Item): Item{
            item.name = decrypt(item.name)
            item.description = decrypt(item.description)
            item.username = decrypt(item.username)
            item.password = decrypt(item.password)
            item.extraInfo = decrypt(item.extraInfo)
            return item
        }

        fun decryptAllItems(items: List<Item>){
            for(item in items){
                item.name = decrypt(item.name)
                item.description = decrypt(item.description)
                item.username = decrypt(item.username)
                item.password = decrypt(item.password)
                item.extraInfo = decrypt(item.extraInfo)
            }
        }

        fun toSHA256(textToConvert: String): String{
            val MD = MessageDigest.getInstance("SHA-256")
            MD.update(textToConvert.toByteArray())
            val outputData =  MD.digest()
            return BigInteger(1, outputData).toString(16)
        }
    }
}