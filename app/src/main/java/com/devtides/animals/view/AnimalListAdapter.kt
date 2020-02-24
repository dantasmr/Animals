package com.devtides.animals.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.devtides.animals.R
import com.devtides.animals.databinding.ItemAnimalBinding
import com.devtides.animals.model.Animal
import com.devtides.animals.util.getProgressDrawble
import com.devtides.animals.util.loadImage
import kotlinx.android.synthetic.main.item_animal.view.*

class AnimalListAdapter (private val animalList : ArrayList<Animal>) : RecyclerView.Adapter<AnimalListAdapter.AnimalViewHolder>(), AnimailClickListener{

    class AnimalViewHolder(var view : ItemAnimalBinding) : RecyclerView.ViewHolder(view.root)

    fun updateAnimalList(newAnimalList: List<Animal>){
        animalList.clear()
        animalList.addAll(newAnimalList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnimalViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = DataBindingUtil.inflate<ItemAnimalBinding>(inflater, R.layout.item_animal, parent, false)
        return AnimalViewHolder(view)
    }

    override fun getItemCount(): Int {
        return animalList.size
    }

    override fun onBindViewHolder(holder: AnimalViewHolder, position: Int) {
       holder.view.animal = animalList[position]
       holder.view.listener = this


/*       holder.view.animalLayout.setOnClickListener {
        val action = ListFragmentDirections.actionDetail(animalList[position])
        Navigation.findNavController(holder.view).navigate(action)
       }*/
    }

    override fun onClick(v: View) {

        for (animal in animalList) {
            if (v.tag == animal.name){
                val action = ListFragmentDirections.actionDetail(animal)
                Navigation.findNavController(v).navigate(action)
            }
        }
    }

}