package app.utiltek.api_con_retrofit

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Url

//para acceder a la api mediante los métodos get,post, put, delete
interface APIService {
    //primera ruta creada, se pasa la url con las razas y la imagen razas/images
    @GET
    suspend fun getDogsByBreeds(@Url url:String):Response<Perro>//suspend se le coloca porque vamos a trabajar con una corrutina
}