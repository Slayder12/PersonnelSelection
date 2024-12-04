package com.example.personnelselection

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class PersonViewModel: ViewModel() {
    val personLiveData: MutableLiveData<MutableList<Person>> = MutableLiveData(mutableListOf())
    val filteredPersonLiveData: MutableLiveData<MutableList<Person>> = MutableLiveData(mutableListOf())
}
