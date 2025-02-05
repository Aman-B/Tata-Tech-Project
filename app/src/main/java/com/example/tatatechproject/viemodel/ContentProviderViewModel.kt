package com.example.tatatechproject.viemodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tatatechproject.data.Content
import com.example.tatatechproject.repository.ContentProviderRepository
import kotlinx.coroutines.launch

class ContentProviderViewModel (private val repository: ContentProviderRepository) : ViewModel(){

    private lateinit var content :Content

    private val _randomString = MutableLiveData<String?>()
    val randomString: LiveData<String?> get() = _randomString

    private val _randomStringLength = MutableLiveData<String?>()
    val randomStringLength: LiveData<String?> get() = _randomStringLength

    private val _randomStringDate = MutableLiveData<String?>()
    val randomStringDate: LiveData<String?> get() = _randomStringDate

    private val _randomStringList = MutableLiveData<ArrayList<String>>()
    val randomStringList: LiveData<ArrayList<String>> get() = _randomStringList

    //declare a randomStringData --this will contain all the data returned by the json.

    // get json data by passing the string length given by user
    fun loadData(length: String) {
        viewModelScope.launch {
            content = repository.fetchData(length)!!
            if (content.value.isNullOrBlank()) {
                _randomString.value = "Some error occured."
            } else {
                _randomString.value = content.value
                _randomStringLength.value = content.length
                _randomStringDate.value = content.date

                //add to random generated string list
                val updatedList = ArrayList(_randomStringList.value ?: emptyList()) // Ensure it's mutable                _randomStringList.value = updatedList + _randomStringList
                updatedList.add(_randomString.value) // Add new string
                _randomStringList.value = updatedList // Post updated list

            }
        }
    }

}