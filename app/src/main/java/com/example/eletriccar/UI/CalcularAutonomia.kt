package com.example.eletriccar.UI

import android.content.Context
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import com.example.eletriccar.R
import java.net.HttpURLConnection
import java.net.URL

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
        setupCacheResult    ()
    }

    private fun setupCacheResult() {
        val valorCalculado = getSharerdPref()
        resultado.text = valorCalculado.toString()
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
        saveSharedPref(result)
    }

    fun saveSharedPref(resultado: Float){
        val  sharedPref = getPreferences(Context.MODE_PRIVATE) ?: return
        with(sharedPref.edit()){
            putFloat(getString(R.string.salved_calc), resultado)
            apply()
        }
    }

    fun getSharerdPref(): Float{
        val sharedPref = getPreferences(Context.MODE_PRIVATE)
        return sharedPref.getFloat(getString(R.string.salved_calc), 0.0f)

    }
}
