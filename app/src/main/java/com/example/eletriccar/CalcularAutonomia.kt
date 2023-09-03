package com.example.eletriccar

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView

class CalcularAutonomia : AppCompatActivity() {
    lateinit var preco: EditText
    lateinit var kmPercorrido: EditText
    lateinit var resultado: TextView
    lateinit var btnCalcular: Button
    lateinit var imageClose: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calcular_autonomia)

        setupViews()
        setupListeners()
    }

    fun setupViews() {
        kmPercorrido = findViewById(R.id.edit_km_percorrido)
        preco = findViewById(R.id.edit_preco_Kwh)
        btnCalcular = findViewById(R.id.btn_calcular)
        resultado = findViewById(R.id.text_resultado)
        imageClose = findViewById(R.id.imageClose)
    }
    fun setupListeners() {
        btnCalcular.setOnClickListener {
            calcular()
        }
        imageClose.setOnClickListener{
            finish()
        }
    }
    fun calcular() {
        val preco = preco.text.toString().toFloat()
        val km = kmPercorrido.text.toString().toFloat()

        val result = preco / km

        resultado.text = result.toString()
    }
}
