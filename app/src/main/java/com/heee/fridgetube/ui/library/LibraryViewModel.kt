package com.heee.fridgetube.ui.library

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.heee.fridgetube.data.entity.Library
import com.heee.fridgetube.data.CounterTop
import com.heee.fridgetube.data.repository.LibraryRepository
import com.heee.fridgetube.data.room.AppDatabase
import kotlinx.coroutines.launch

class LibraryViewModel(application: Application) : AndroidViewModel(application) {
    private val libraryRepository = LibraryRepository(application.applicationContext)

    private val _counterTopList = MutableLiveData<List<CounterTop>>()
    val recipes: LiveData<List<CounterTop>>
        get() = _counterTopList

    fun addLibrary(library: Library) {
        viewModelScope.launch {
            libraryRepository.addLibrary(library)
        }
    }

    fun fetchLibrary() {
        viewModelScope.launch {
            _counterTopList.value = libraryRepository.fetchLibrary()
        }
    }

    companion object {
        const val TAG = "LibraryViewModel"
    }
}