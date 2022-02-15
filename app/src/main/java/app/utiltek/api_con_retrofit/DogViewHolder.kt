package app.utiltek.api_con_retrofit

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import app.utiltek.api_con_retrofit.databinding.ItemDogBinding
import com.squareup.picasso.Picasso

//es donde se carga la interface que tendrá el recicler, en este caso se carga imageView
//las imágenes
class DogViewHolder (view:View):RecyclerView.ViewHolder(view){

    private  val binding=ItemDogBinding.bind(view)

    fun bind(image:String){
        //se recibe una url, y se necesita que se convierta  a una imagen (Picasso)
        Picasso.get().load(image).into(binding.ivDog)


    }

}