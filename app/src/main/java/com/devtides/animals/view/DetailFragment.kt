package com.devtides.animals.view


import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import androidx.palette.graphics.Palette
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition


import com.devtides.animals.R
import com.devtides.animals.databinding.FragmentDetailBinding
import com.devtides.animals.model.Animal
import com.devtides.animals.model.AnimalPallete
import com.devtides.animals.util.getProgressDrawble
import com.devtides.animals.util.loadImage
import kotlinx.android.synthetic.main.fragment_detail.*
import java.util.*


/**
 * A simple [Fragment] subclass.
 */
class DetailFragment : Fragment() {


    private lateinit var databinding : FragmentDetailBinding

    var animal : Animal? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        databinding = DataBindingUtil.inflate(inflater, R.layout.fragment_detail, container, false)
        return databinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let{

            animal = DetailFragmentArgs.fromBundle(it).animal
        }

        animal?.imageUrl?.let{
            setupBagroundColor(it)
        }

        databinding.animal = animal

    }


    private fun setupBagroundColor(url : String){
        Glide.with(this)
            .asBitmap()
            .load(url)
            .into(object : CustomTarget<Bitmap>(){
                override fun onLoadCleared(placeholder: Drawable?) {

                }

                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                   Palette.from(resource)
                       .generate(){pallete ->
                           val intColor = pallete?.lightMutedSwatch?.rgb ?: 0
                           databinding.pallete = AnimalPallete(intColor)
                       }
                }

            })
    }

}
