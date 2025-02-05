package com.example.tatatechproject.repository

import android.content.ContentResolver
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject

class ContentProviderRepository (private val context: Context) {
    //data repository for content provider

    private val CONTENT_URI = Uri.parse("content://com.iav.contestdataprovider/text")

    suspend fun fetchData(length : String): String? {
        return withContext(Dispatchers.IO) { // Use IO thread for database operations
            val projection = arrayOf("data")
            var result: String? = null

            val uriWithLength = Uri.parse("$CONTENT_URI")
            val bundle = Bundle().apply {
                putInt(ContentResolver.QUERY_ARG_LIMIT, length.toInt())  // Pass length
            }
            val cursor: Cursor? = context.contentResolver.query(
                uriWithLength,
                projection,
                bundle,null
            )

            cursor?.use {
                if (it.moveToFirst()) {
                    val jsonData = it.getString(it.getColumnIndexOrThrow("data"))
                    Log.d("ContentProvider", "Raw JSON: $jsonData")

                    // Parse JSON
                    val jsonObject = JSONObject(jsonData)
                    result = jsonObject.getJSONObject("randomText").getString("value")
                }
            }
            result // Return the extracted text
        }
    }

}