package com.devtides.animals.view


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager

import com.devtides.animals.R
import com.devtides.animals.model.Animal
import com.devtides.animals.viewmodel.ListViewModel
import kotlinx.android.synthetic.main.fragment_list.*

class ListFragment : Fragment() {

    private lateinit var viewModel : ListViewModel
    private val listAdapter = AnimalListAdapter(arrayListOf())

    private val animalListDataObservable = Observer<List<Animal>>{list ->
        list?.let {
            animalList.visibility = View.VISIBLE
            listAdapter.updateAnimalList(list)
        }
    }

    private val loadingLiveDataObservable = Observer<Boolean>{isloading ->
        loadingView.visibility = if (isloading) View.VISIBLE else View.GONE
        if (isloading){
            listError.visibility = View.GONE
            animalList.visibility = View.GONE        }
    }

    private val errorLiveDataObservable = Observer<Boolean>{isError ->
        listError.visibility = if (isError) View.VISIBLE else View.GONE
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(ListViewModel::class.java)
        viewModel.animals.observe(this, animalListDataObservable)
        viewModel.loading.observe(this, loadingLiveDataObservable)
        viewModel.loadError.observe(this, errorLiveDataObservable)
        viewModel.refresh()

        animalList.apply {
            layoutManager = GridLayoutManager(context, 2)
            adapter = listAdapter
        }

    }


}
