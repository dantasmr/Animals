package com.devtides.animals.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.devtides.animals.model.Animal

class ListViewModel (application : Application) : AndroidViewModel(application){

    //by lazy: O objeto so sera criado quando for necessario
    val animals by lazy { MutableLiveData<List<Animal>>()}
    val loadError by lazy {MutableLiveData<Boolean>()}
    val loading by lazy {MutableLiveData<Boolean>()}


    fun refresh(){
        getAnimals()
    }

    private fun getAnimals(){

        val a1 = Animal("Sapo")
        val a2 = Animal("Cobra")
        val a3 = Animal("Rato")
        val a4 = Animal("Abelha")
        val a5 = Animal("Cavalo")
        val a6 = Animal("Gato")

        val animalList = listOf(a1, a2, a3, a4, a5)
        animals.value = animalList
        loadError.value = false
        loading.value = false

    }



}