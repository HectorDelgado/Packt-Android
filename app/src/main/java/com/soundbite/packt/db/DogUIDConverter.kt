package com.soundbite.packt.db

import androidx.room.TypeConverter

class DogUIDConverter {
    @TypeConverter
    fun uidListToString(uids: List<String>?): String? {
        return uids?.joinToString(separator = ":")
    }

    @TypeConverter
    fun stringToUidList(str: String?): List<String>? {
        return str?.split(":")
    }
}
