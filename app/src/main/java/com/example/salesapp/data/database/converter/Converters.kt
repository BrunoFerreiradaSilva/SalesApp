package com.example.salesapp.data.database.converter

import androidx.room.TypeConverter
import com.example.salesapp.model.Item
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


class Converters {
    private inline fun <reified T> Gson.fromJson(json: String) =
        fromJson<T>(json, object : TypeToken<T>() {}.type)

    @TypeConverter
    fun fromStringArrayList(value: List<Item>): String {
        return Gson().toJson(value)
    }

    @TypeConverter
    fun toStringArrayList(value: String): List<Item> {
        return try {
            Gson().fromJson<List<Item>>(value) //using extension function
        } catch (e: Exception) {
            arrayListOf()
        }
    }

}