package id.ac.ukdw.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SharedFilterViewModel : ViewModel() {
    private val _buttonState = MutableLiveData<Boolean>()
    val buttonState: LiveData<Boolean> get() = _buttonState

    private val _selectedTahun = MutableLiveData<String>()
    val tahunValue: LiveData<String> get() = _selectedTahun
    private val _selectedLokasi = MutableLiveData<String>()
    val lokasiValue: LiveData<String> get() = _selectedLokasi
    private val _selectedComodity = MutableLiveData<String>()
    val comodityValue: LiveData<String> get() = _selectedComodity

    fun setButtonStateAndValue(pressed: Boolean, valueTahun: String, valueLokasi: String, valueComodity: String) {
        _buttonState.value = pressed
        _selectedTahun.value = valueTahun
        _selectedLokasi.value = valueLokasi
        _selectedComodity.value = valueComodity
    }
    fun setButtonClearFilter(pressed: Boolean){
        _buttonState.value = pressed
    }
}
