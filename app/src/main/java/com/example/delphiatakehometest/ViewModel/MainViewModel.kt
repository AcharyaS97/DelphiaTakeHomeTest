package com.example.delphiatakehometest.ViewModel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.delphiatakehometest.Models.ExchangeRateResponse
import com.example.delphiatakehometest.Network.ExchangeRateRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/*
    This ViewModel class encapsulates business logic for the state of the UI
 */
class MainViewModel @ViewModelInject constructor(private val repository: ExchangeRateRepository): ViewModel() {

    var _baseCurrency = "AUD"

    private val _result = MutableLiveData<ExchangeRateResponse>()
    val exchangeRateRateResult : LiveData<ExchangeRateResponse>
        get() = _result

    fun setBaseCurrency(currency : String){
        _baseCurrency = currency
    }

    fun getBaseCurrency() : String {return _baseCurrency}

    fun getExchangeRatesWithBase() {
        viewModelScope.launch(Dispatchers.IO){
            _result.postValue(repository.getRate(_baseCurrency))
        }
    }
}

