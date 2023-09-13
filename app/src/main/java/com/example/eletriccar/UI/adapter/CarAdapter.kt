package com.example.eletriccar.UI.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.eletriccar.R
import com.example.eletriccar.domain.Car

class CarAdapter(private val carros: List<Car>, private val isFavoriteScreen: Boolean = false):
    RecyclerView.Adapter<CarAdapter.ViewHolder>() {

        var carItemListenner : (Car) -> Unit = {

        }

    // CRIA UMA NOVA VIEW
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.activity_car_item, parent, false)

        return ViewHolder(view)
    }

    // PEGA O CONTEUDO DA VIEW E TROCA PELA INFORMAÇÃO DE ITEM DE UMA LISTA
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.preco.text = carros[position].preco
        holder.bateria.text = carros[position].bateria
        holder.potencia.text = carros[position].potencia
        holder.recarga.text = carros[position].recarga
        if (isFavoriteScreen){
            holder.favorite.setImageResource(R.drawable.ic_star_selected)
        } else {
            holder.favorite.setImageResource(R.drawable.ic_star)
        }
        holder.favorite.setOnClickListener{
            val carro = carros[position]
            carItemListenner(carro)
            carro.isFavorite = !carro.isFavorite
            setupFavorite(carro, holder)

        }
    }

    private fun setupFavorite(
        carro: Car,
        holder: ViewHolder
    ) {
        if (carro.isFavorite) {
            holder.favorite.setImageResource(R.drawable.ic_star_selected)
        } else {
            holder.favorite.setImageResource(R.drawable.ic_star)
        }
    }

    // PEGA A QUANTIDADE DE CARROS DA LISTA
    override fun getItemCount(): Int = carros.size
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view){

        val preco: TextView
        val bateria: TextView
        val potencia: TextView
        val recarga: TextView
        val favorite: ImageView
        init {
            view.apply {
                preco = findViewById(R.id.text_preco_value)
                bateria = findViewById(R.id.text_bateria_value)
                potencia = findViewById(R.id.text_potencia_value)
                recarga = findViewById(R.id.text_recarga_value)
                favorite = findViewById(R.id.img_favorite)
            }
        }
    }
}
