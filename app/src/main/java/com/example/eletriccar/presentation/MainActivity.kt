package com.example.eletriccar.presentation

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.TextView
import com.example.eletriccar.CalcularAutonomia
import com.example.eletriccar.R

class MainActivity : AppCompatActivity() {
    lateinit var btnCalcular: Button
    lateinit var list: ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupViews()
        setupListeners()
        setupList()
    }
    fun setupViews() {
        btnCalcular = findViewById(R.id.btn_calcular)
        list = findViewById(R.id.list_informations)
    }

    fun setupList(){
        var dados = arrayOf(
            "teste", "dasdasda", "dsadsad", "sdadsadsa"
        )
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, dados)
        list.adapter = adapter
    }

    fun setupListeners() {
        btnCalcular.setOnClickListener {
            startActivity(Intent(this, CalcularAutonomia::class.java))
        }
    }
}