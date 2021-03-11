package com.soundbite.packt.db

import androidx.room.TypeConverter

class DogUIDConverter {
//    @TypeConverter
//    fun uidListToString(uids: List<String>?): String? {
//        uids?.let {
//            if (it.isEmpty())
//                throw IllegalArgumentException("Error. Arg 'uids' must not be empty.")
//        }
//        return uids?.joinToString(separator = "#")
//    }
//
//    @TypeConverter
//    fun stringToUidList(str: String?): List<String>? {
//        str?.let {
//            if (it.isBlank())
//                throw IllegalArgumentException("Error. Arg 'str' must not be blank.")
//        }
//        return str?.split("#")
//    }

    @TypeConverter
    fun uidListToString(uids: List<String>): String {
        return if (uids.isEmpty()) {
            ""
        } else {
            uids.joinToString(separator = "#")
        }
    }

    @TypeConverter
    fun stringToUidList(str: String): List<String> {
        return if (str.isEmpty()) {
            emptyList()
        } else {
            str.split("#")
        }
    }
}
