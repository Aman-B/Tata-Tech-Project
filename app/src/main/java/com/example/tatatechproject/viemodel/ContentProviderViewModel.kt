package com.example.tatatechproject.viemodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tatatechproject.repository.ContentProviderRepository
import kotlinx.coroutines.launch

class ContentProviderViewModel (private val repository: ContentProviderRepository) : ViewModel(){

    private val _randomText = MutableLiveData<String?>()
    val randomText: LiveData<String?> get() = _randomText

    private val _randomStringList = MutableLiveData<List<String>>()
    val randomStringList: LiveData<List<String>> get() = _randomStringList

    //declare a randomStringData --this will contain all the data returned by the json.

    // get json data by passing the string length given by user
    fun loadData(length: String) {
        viewModelScope.launch {
            _randomText.value = repository.fetchData(length)
        }
    }
    //get random string
    // fun getLengthRandomString(): String {
        //if length is good
                // call getRandomString function
                // add it to randomStringList
                // return the randomstringList
         //if invalid
                //return error message


}