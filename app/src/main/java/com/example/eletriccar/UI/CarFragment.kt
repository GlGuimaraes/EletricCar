package com.example.eletriccar.UI

import android.content.Context
import android.content.Intent
import android.media.Image
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.util.Base64InputStream
import android.util.JsonToken
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.eletriccar.R
import com.example.eletriccar.UI.adapter.CarAdapter
import com.example.eletriccar.data.CarAPI
import com.example.eletriccar.data.CarFactory
import com.example.eletriccar.domain.Car
import com.google.android.material.floatingactionbutton.FloatingActionButton
import org.json.JSONArray
import org.json.JSONObject
import org.json.JSONTokener
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class CarFragment: Fragment() {

    lateinit var fab_calcular: FloatingActionButton
    lateinit var listaCaros: RecyclerView
    lateinit var progress: ProgressBar
    lateinit var noInternetImage: ImageView
    lateinit var noInternetText: TextView
    lateinit var carsApi: CarAPI

    var carArray : ArrayList<Car> = ArrayList()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.car_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRetrofit()
        setupView(view)
        setupListeners()
    }

    override fun onResume() {
        super.onResume()
        if(checkForInternet(context)){
            //callService() --> Esse é outra forma de chamar serviço
            getAllCars()
        } else {
            emptyState()
        }
    }

    fun setupRetrofit(){
        val retrofit = Retrofit.Builder()
            .baseUrl("https://glguimaraes.github.io/cars-api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        carsApi = retrofit.create(CarAPI::class.java)
    }

    fun getAllCars(){
        carsApi.getAllCars().enqueue(object : Callback<List<Car>>{
            override fun onResponse(call: Call<List<Car>>, response: Response<List<Car>>) {
                if (response.isSuccessful) {
                    progress.isVisible = false  // quando terminou ele fca invisible novamente
                    noInternetImage.isVisible = false
                    noInternetText.isVisible = false

                    response.body()?.let {
                        setupList(it)
                    }
                }else{
                        Toast.makeText(context, R.string.response_erro, Toast.LENGTH_LONG).show()
                    }
                }

            override fun onFailure(call: Call<List<Car>>, t: Throwable) {
                TODO("Not yet implemented")
            }
        })
    }
    fun emptyState(){
        progress.isVisible = false
        listaCaros.isVisible = false
        noInternetImage.isVisible = true
        noInternetText.isVisible = true
    }

    fun setupView(view: View){
        view.apply {
            fab_calcular = findViewById(R.id.fab_calcular)
            listaCaros = findViewById(R.id.list_informations)
            progress = findViewById(R.id.pb_loader)
            noInternetImage = findViewById(R.id.img_empty_state)
            noInternetText = findViewById(R.id.text_noWifi)
        }
    }
    fun setupList(lista : List<Car>){
        val carAdapter = CarAdapter(lista)
        listaCaros.apply {
            isVisible = true // COLOCANDO A LISTA DOS CARROS VISÍVEL
            adapter = carAdapter}

    }
    fun setupListeners() {
        fab_calcular.setOnClickListener {
            startActivity(Intent(context, CalcularAutonomia::class.java))
        }
    }

    fun callService(){
        val urlBase = "https://glguimaraes.github.io/cars-api/cars.json"
        progress.isVisible = true
        Mytask().execute(urlBase)
    }

    fun checkForInternet(context: Context?): Boolean{
        val connectivityManager = context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager // PEGANDO DENTRO DO NOSSO APP ANDROID O SERVIÇO DE CONECTIVIDADE FORA DO APP

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            val network = connectivityManager.activeNetwork?: return false // PEGNDO SE TEM INTERNET ATIVA

            val activeNetwork = connectivityManager.getNetworkCapabilities(network)?: return false // VENDO SE TEM A CAPACIDADE DE CONEXÃO, QUAL A CAPACIDADE DE CONEXÃO QUE O SO NOS DA

            return when {
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                else -> false
            }
        }
        else {
            @Suppress("DEPRECATION")
            val networkInfo = connectivityManager.activeNetworkInfo?: return false
            @Suppress("DEPRECATION")
            return networkInfo.isConnected
        }
    }
    inner class Mytask : AsyncTask<String, String, String>(){

        override fun onPreExecute() {
            super.onPreExecute()
            Log.d("MyTask", "Iniciando..") //  INICIANDO A BARRA DE PROGRESSO
        }
        override fun doInBackground(vararg url: String?): String {
            var urlConnection: HttpURLConnection? = null

            try {
                val urlBase = URL(url[0])


                urlConnection = urlBase.openConnection() as HttpURLConnection
                urlConnection.connectTimeout = 60000
                urlConnection.readTimeout = 60000

                // falando que esse tipo de aplicação só aceita /json
                urlConnection.setRequestProperty(
                    "Accept",
                    "Aplication/json"
                )
                //verificando se o serviço esta respondendo ok
                val responseCode = urlConnection.responseCode
                if(responseCode == HttpURLConnection.HTTP_OK){
                    var response = urlConnection.inputStream.bufferedReader().use { it.readText() }
                    publishProgress(response)
                }else {
                    Log.e("Erro", "Serviço indisponível no momento...")
                }

            }catch (ex: Exception){
                Log.e("Erro", "Erro ao realizar processamento ....")
            } finally {
                    urlConnection?.disconnect()
            }
            return ""
        }

        override fun onProgressUpdate(vararg values: String?) {
            try {
                val jsonArray = JSONTokener(values[0]).nextValue() as JSONArray
                for (i in 0 until jsonArray.length()){
                    val id = jsonArray.getJSONObject(i).getString("id")
                    Log.d("ID ->", id)

                    val preco = jsonArray.getJSONObject(i).getString("preco")
                    Log.d("Preço ->", preco)

                    val bateria = jsonArray.getJSONObject(i).getString("bateria")
                    Log.d("Bateria ->", bateria)

                    val potencia = jsonArray.getJSONObject(i).getString("potencia")
                    Log.d("Potencia ->", potencia)

                    val recarga = jsonArray.getJSONObject(i).getString("recarga")
                    Log.d("Recarga ->", recarga)
                    val urlPhoto = jsonArray.getJSONObject(i).getString("urlPhoto")
                    Log.d("UrlPhoto ->", urlPhoto)

                    val model = Car(
                        id = id.toInt(),
                        preco = preco,
                        bateria = bateria,
                        potencia = potencia,
                        recarga = recarga,
                        urlPhoto = urlPhoto
                    )

                    carArray.add(model)

                    Log.d("Model -> ", model.toString())
                }
                progress.isVisible = false  // quando terminou ele fca invisible novamente
                noInternetImage.isVisible = false
                noInternetText.isVisible = false
                //setupList()

            }catch (ex: Exception){
                Log.e("Erro ->", ex.message.toString())
            }
        }
    }
}