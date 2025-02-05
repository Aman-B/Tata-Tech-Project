package com.example.tatatechproject.viemodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tatatechproject.data.Content
import com.example.tatatechproject.repository.ContentProviderRepository
import kotlinx.coroutines.launch

/*
* Viewmodel that takes [ContentProviderRepository] as an argument.
* */
class ContentProviderViewModel (private val repository: ContentProviderRepository) : ViewModel(){

    private lateinit var content :Content

    // vals for random string details.

    private val _randomString = MutableLiveData<String?>()
    val randomString: LiveData<String?> get() = _randomString

    private val _randomStringLength = MutableLiveData<String?>()
    val randomStringLength: LiveData<String?> get() = _randomStringLength

    private val _randomStringDate = MutableLiveData<String?>()
    val randomStringDate: LiveData<String?> get() = _randomStringDate

    //list for all the random generated strings
    private val _randomStringList = MutableLiveData<ArrayList<String>>()
    val randomStringList: LiveData<ArrayList<String>> get() = _randomStringList


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
                val updatedList = ArrayList(_randomStringList.value ?: emptyList())
                updatedList.add(_randomString.value) // Add new string
                _randomStringList.value = updatedList // Post updated list

            }
        }
    }

    // deletes one string from the list at position zero.
    fun deleteOneString() {
        val updatedList = ArrayList(_randomStringList.value ?: emptyList())
        if(updatedList.isNotEmpty()) {
            updatedList.removeAt(0)
            _randomStringList.value = updatedList
        }
    }

    // deletes all random generated strings.
    fun deleteAllStrings() {
        val updatedList = ArrayList(_randomStringList.value ?: emptyList())
        if(updatedList.isNotEmpty()) {
            updatedList.removeAt(0)
            _randomStringList.value = arrayListOf()
        }
    }

}