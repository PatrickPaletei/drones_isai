package id.ac.ukdw.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SharedViewModel : ViewModel() {
    private val _data = MutableLiveData<MutableMap<String, MutableList<String>>>()
    val data: MutableLiveData<MutableMap<String, MutableList<String>>> get() = _data

    fun setData(newData: MutableMap<String, MutableList<String>>) {
        _data.value = newData
    }
}

