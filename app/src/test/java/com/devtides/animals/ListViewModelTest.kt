package com.devtides.animals

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.devtides.animals.di.ApiModule
import com.devtides.animals.di.AppModule
import com.devtides.animals.di.DaggerViewModelComponent
import com.devtides.animals.model.Animal
import com.devtides.animals.model.AnimalApiService
import com.devtides.animals.model.ApiKey
import com.devtides.animals.util.SharePreferenceHelper
import com.devtides.animals.viewmodel.ListViewModel
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.internal.schedulers.ExecutorScheduler
import io.reactivex.plugins.RxJavaPlugins
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import java.util.concurrent.Executor

class ListViewModelTest {

    @get:Rule
    var rule = InstantTaskExecutorRule()

    @Mock
    lateinit var animalService : AnimalApiService


    @Mock
    lateinit var prefs: SharePreferenceHelper

    val application = Mockito.mock(Application::class.java)

    val listViewModel = ListViewModel(application, true)

    val key : String = "api key"

    @Before
    fun setup(){

        MockitoAnnotations.initMocks(this)
        val testComponent = DaggerViewModelComponent.builder()
            .appModule(AppModule(application))
            .apiModule(ApiModuleTest(animalService))
            .prefsModule(PrefsModuleTest(prefs))
            .build()
            .inject(listViewModel)
    }



    @Before
    fun setupRxSchedulers(){
        val imediate = object : Scheduler(){
            override fun createWorker(): Worker {
                return ExecutorScheduler.ExecutorWorker(Executor{it.run()}, true)
            }
        }
        RxJavaPlugins.setInitNewThreadSchedulerHandler {scheduler -> imediate}
        RxAndroidPlugins.setInitMainThreadSchedulerHandler{scheduler -> imediate}

    }

    @Test
    fun getAnimalsSucess(){
        Mockito.`when`(prefs.getApiKey()).thenReturn(key)
        val animal = Animal("cow", null, null, null, null, null, null)
        val animalList = listOf(animal)
        val testSingle : Single<List<Animal>> = Single.just(animalList)
        Mockito.`when`(animalService.getAnimals(key)).thenReturn(testSingle)
        listViewModel.refresh()
        Assert.assertEquals(1, listViewModel.animals.value?.size)
        Assert.assertEquals(false, listViewModel.loadError.value)
        Assert.assertEquals(false, listViewModel.loading.value)
    }
    
    @Test
    fun getAnimalsFailure(){
        Mockito.`when`(prefs.getApiKey()).thenReturn(key)
        val testSingle = Single.error<List<Animal>>(Throwable())
        val keySingle = Single.just(ApiKey("Ok", key))
        Mockito.`when`(animalService.getAnimals(key)).thenReturn(testSingle)
        Mockito.`when`(animalService.getKey()).thenReturn(keySingle)
        listViewModel.refresh()
        Assert.assertEquals(null, listViewModel.animals.value)
        Assert.assertEquals(false, listViewModel.loading.value)
        Assert.assertEquals(true, listViewModel.loadError.value)
        
    }

    @Test
    fun getKeySucess(){
    Mockito.`when`(prefs.getApiKey()).thenReturn(null)
    val apiKey = ApiKey("OK", key)
    val keySingle = Single.just((apiKey))
    Mockito.`when`(animalService.getKey()).thenReturn(keySingle)
    val animal = Animal("cow", null, null, null, null, null, null)
    val animalList = listOf(animal)
    val testSingle : Single<List<Animal>> = Single.just(animalList)
    Mockito.`when`(animalService.getAnimals(key)).thenReturn(testSingle)
    listViewModel.refresh()
    Assert.assertEquals(1, listViewModel.animals.value?.size)
    Assert.assertEquals(false, listViewModel.loading.value)
    Assert.assertEquals(false, listViewModel.loadError.value)

    }

    @Test
    fun getKeyFailure(){
        Mockito.`when`(prefs.getApiKey()).thenReturn(null)
        val testSingle = Single.error<ApiKey>(Throwable())
        Mockito.`when`(animalService.getKey()).thenReturn(testSingle)
        listViewModel.refresh()
        Assert.assertEquals(null, listViewModel.animals.value)
        Assert.assertEquals(false, listViewModel.loading.value)
        Assert.assertEquals(true, listViewModel.loadError.value)
    }
}