package app.utiltek.api_con_retrofit

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import app.utiltek.api_con_retrofit.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity(), SearchView.OnQueryTextListener {

    private lateinit var binding: ActivityMainBinding// para llamar a los componentes de la vista más facil
    private lateinit var adapter: DogAdapter
    private val dogImages =
        mutableListOf<String>()//cada que se hace una busqueda se pinta una imagen diferente en el recycler,por lo tanto la lista debe cambiar, mutable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //llamando al buscador:
        binding.svDog.setOnQueryTextListener(this)
        initRecyclerView()

    }

    private fun initRecyclerView() {
        binding.rvDogs.layoutManager = LinearLayoutManager(this)
        //llamando al adapter para pasarle el recycler:
        adapter = DogAdapter(dogImages)//el adapter toca pasarle una lista
        binding.rvDogs.adapter = adapter

    }

    //creando instancia del objeto retrofit, tendrá la url original
    private fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://dog.ceo/api/breed/")
            .addConverterFactory(GsonConverterFactory.create())//conversor de json a objeto como tal
            .build()
    }

    //usando corrutinas para hacer llamadas asincronas, sirven para hacer llamadas al servidor donde se puede demorar en en dar respuesta
    //busco la raza y ese string se lo paso por parámetro al metodo:
    private fun searchByName(raza: String) {

        CoroutineScope(Dispatchers.IO).launch {
            var call = getRetrofit().create(APIService::class.java)
                .getDogsByBreeds("$raza/images")//tengo una lista  de una raza en especial
            var cachorros =
                call.body()//aquí tengo el objeto como tal de la raza buscada, una lista con una raza determinada
            //todo lo que se ejecute aqui se muestra en el hilo principal de la app:
            runOnUiThread {
                if (call.isSuccessful) {
                    //añadiendo imagenes al recycler:
                    val images = cachorros?.images
                        ?: emptyList()//aquí si tengo la url de las razas buscadas, verifico que no esté vacia y no de null
                    dogImages.clear()//limpio la lista por si las moscas
                    dogImages.addAll(images)//adiciono las imagenes de determinada raza a la lista, segun busqueda
                    adapter.notifyDataSetChanged()//avisar que hubieron cambios
                } else {
                    showError()
                }

                //funcion para quitar el teclado del movil:
                hideKeyBoard()

            }


        }

    }

    //para ocultar el teclado
    private fun hideKeyBoard() {
        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(binding.viewRoot.windowToken, 0)

    }

    private fun showError() {
        Toast.makeText(this, "No se encontró la raza", Toast.LENGTH_SHORT).show()
    }

    //cuando se le da clic en buscar se ejetuta este metodo
    override fun onQueryTextSubmit(query: String?): Boolean {
        if (!query.isNullOrEmpty()) {
            searchByName(query.toLowerCase())

        }

        return true
    }

    //cada vez que se escriba algo en el buscador este método lo detecta
    override fun onQueryTextChange(newText: String?): Boolean {
        return true
    }


}