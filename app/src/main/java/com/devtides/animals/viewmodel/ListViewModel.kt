package com.devtides.animals.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.devtides.animals.model.Animal
import com.devtides.animals.model.AnimalApiService
import com.devtides.animals.model.ApiKey
import com.devtides.animals.util.SharePreferenceHelper
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers

class ListViewModel (application : Application) : AndroidViewModel(application){

    //by lazy: O objeto so sera criado quando for necessario
    val animals by lazy { MutableLiveData<List<Animal>>()}
    val loadError by lazy {MutableLiveData<Boolean>()}
    val loading by lazy {MutableLiveData<Boolean>()}

    val disposable = CompositeDisposable();
    val apiService = AnimalApiService();
    val sharePreferenceHelper = SharePreferenceHelper(getApplication())
    var invalidApiKey = false


    fun refresh(){
        loading.value = true
        invalidApiKey = false
        val key = sharePreferenceHelper.getApiKey()
        if (key.isNullOrEmpty()) {
            getKey()
        }else{
            getAnimals(key)
        }
    }

    fun hardRefresh(){
        loading.value = true
        getKey()
    }


    private fun getKey(){
        disposable.add(
            apiService.getKey()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<ApiKey>(){
                    override fun onSuccess(key: ApiKey) {
                       if (key.key.isNullOrEmpty()){
                           loadError.value = true
                           loading.value = false
                       }else{
                           sharePreferenceHelper.saveApiKey(key.key)
                           getAnimals(key.key)
                       }
                    }

                    override fun onError(e: Throwable) {
                       e.printStackTrace()
                       loadError.value = true
                       loading.value = false
                    }

                })
        )
    }

    private fun getAnimals(key : String){

        disposable.add(
            apiService.getAnimals(key)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<List<Animal>>(){
                    override fun onSuccess(list: List<Animal>) {
                        loadError.value = false
                        loading.value = false
                        animals.value = list


                    }

                    override fun onError(e: Throwable) {
                        e.printStackTrace()
                        if (!invalidApiKey){
                            invalidApiKey = true
                            getKey()
                        }
                        loadError.value = true
                        loading.value = false
                        animals.value = null
                    }

                })
        )

    }

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }



}