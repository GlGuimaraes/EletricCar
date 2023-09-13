package com.example.eletriccar.UI

import android.media.CamcorderProfile.getAll
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.eletriccar.R
import com.example.eletriccar.UI.adapter.CarAdapter
import com.example.eletriccar.data.local.CarRepository
import com.example.eletriccar.domain.Car

class FavoriteFragment: Fragment() {

    lateinit var listaCarosFavorites: RecyclerView
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.favorite_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView(view)
        setupList()

    }

    private fun getCarsOnLocalDb(): List<Car> {
        val repository = CarRepository(requireContext())
        val carList = repository.getAll()
        return carList
    }

    fun setupView(view: View){
        view.apply {
            listaCarosFavorites = findViewById(R.id.list_informations_favorite)
        }
    }
    fun setupList(){
        val cars = getCarsOnLocalDb()
        val carAdapter = CarAdapter(cars, isFavoriteScreen = true)
        listaCarosFavorites.apply {
            isVisible = true // COLOCANDO A LISTA DOS CARROS VISÍVEL
            adapter = carAdapter
        }
        carAdapter.carItemListenner = { carro ->

            //val isSaved = CarRepository(requireContext()).saveIfNotExiste(carro)
            // @TODO IMPLEMENTAR O DELETE NO BANCO DE DADOS
        }
    }

}