package com.example.trendmart.restapi

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.trendmart.login.AuthRepository
import com.example.trendmart.login.User
import com.example.trendmart.roomdatabase.Fav
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainViewModel(private val repository: Repository,private val repo: AuthRepository) : ViewModel() {
    private val _allProdect = MutableStateFlow<ResultState<List<ProdectItem>>>(ResultState.Loading)
    val allProdect: StateFlow<ResultState<List<ProdectItem>>> get() = _allProdect

    private val _allFav = MutableStateFlow<ResultState<List<Fav>>>(ResultState.Loading)
    val allFav: StateFlow<ResultState<List<Fav>>> = _allFav.asStateFlow()

    private val _allInsert = MutableStateFlow<ResultState<Unit>>(ResultState.Loading)
    val allInsert: StateFlow<ResultState<Unit>> = _allInsert.asStateFlow()



    fun getAllFav() {
        viewModelScope.launch {
            _allFav.value = ResultState.Loading
            try {
                val response = repository.getAllFav()
                _allFav.value = ResultState.Success(response)
            } catch (e: Exception) {
                _allFav.value = ResultState.Error(e)
            }
        }
    }

    fun Insert(fav: Fav) {
        viewModelScope.launch {
            _allInsert.value = ResultState.Loading
            try {
                repository.Insert(fav)
                _allInsert.value = ResultState.Success(Unit)
            } catch (e: Exception) {
                _allInsert.value = ResultState.Error(e)
            }
        }
    }

    fun getAllProduct() {
        viewModelScope.launch {
            _allProdect.value = ResultState.Loading
            try {
                val products = repository.getAllProduct()
                _allProdect.value = ResultState.Success(products)
            } catch (e: Exception) {
                _allProdect.value = ResultState.Error(e)
            }
        }
    }

    fun signUp(user: User)=repo.signUp(user)

    fun login(user: User)=repo.login(user)
}

