package com.example.tatatechproject.repository

import android.content.ContentResolver
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.util.Log
import com.example.tatatechproject.data.Content
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ContentProviderRepository(private val context: Context) {
    //data repository for content provider

    private val CONTENT_URI = Uri.parse("content://com.iav.contestdataprovider/text")

    suspend fun fetchData(length: String): Content? {
        return withContext(Dispatchers.IO) { // Use IO thread for database operations
            val projection = arrayOf("data")
            var result: Content? = null

            val uriWithLength = Uri.parse("$CONTENT_URI")
            val bundle = Bundle().apply {
                putInt(ContentResolver.QUERY_ARG_LIMIT, length.toInt())  // Pass length
            }
            try {
                val cursor: Cursor? = context.contentResolver.query(
                    uriWithLength,
                    projection,
                    bundle, null
                )

                cursor?.use {
                    if (it.moveToFirst()) {
                        val jsonData = it.getString(it.getColumnIndexOrThrow("data"))
                        Log.d("ContentProvider", "Raw JSON: $jsonData")

                        // Parse JSON
                        val jsonObject = JSONObject(jsonData)
                        result = getFormattedData(jsonObject)
                    }
                }
                result // Return the extracted object
            } catch (e: UnsupportedOperationException) {
                Log.e("ContentProviderRepository", "UnsupportedOperationException")
                Content() //return blank object
            }
        }
    }

    //creates [Content] object from the [jsonObject]
    private fun getFormattedData(jsonObject: JSONObject): Content? {
        return Content(
            value = jsonObject.getJSONObject("randomText").getString("value"),
            length = jsonObject.getJSONObject("randomText").getString("length"),
            date = convertDate(jsonObject.getJSONObject("randomText").getString("created"))
        )
    }

    // converts date string to readable date format
    private fun convertDate(dateString: String): String? {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
        val outputFormat = SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault())

        return try {
            val date = inputFormat.parse(dateString)
            outputFormat.format(date ?: Date())
        } catch (e: Exception) {
            "Invalid date"
        }
    }

}