package com.andres.navigationcomponentexample.view

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.andres.navigationcomponentexample.R
import com.andres.navigationcomponentexample.model.database.entities.UserEntity


class UsuarioAdapter(context: Context, usuarios: List<UserEntity>):
    ArrayAdapter<UserEntity>(context, 0, usuarios) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        // Obtener la vista existente o inflar una nueva
        val view = convertView ?: LayoutInflater.from(context).inflate(
            R.layout.item_usuario, parent, false
        )

        // Obtener el usuario en la posición actual
        val usuario = getItem(position)

        // Obtener las vistas de los TextViews
        val nombreTextView = view.findViewById<TextView>(R.id.textViewNombre)
        val apellidoTextView = view.findViewById<TextView>(R.id.textViewApellido)

        // Asignar los valores del usuario a los TextViews
        nombreTextView.text = "Nombre: ${usuario?.nombre}"
        apellidoTextView.text = "Apellido: ${usuario?.apellido}"

        return view
    }
}