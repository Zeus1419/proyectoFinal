package com.andres.navigationcomponentexample.view
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.andres.navigationcomponentexample.R
import com.andres.navigationcomponentexample.model.database.entities.UserEntity
import com.andres.navigationcomponentexample.model.database.providers.UsuarioDatabaseProvider

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class Formulario : Fragment() {

    private lateinit var etNombre: EditText
    private lateinit var etApellido: EditText
    private lateinit var btnAgregar: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Infla el layout para este fragmento
        val view = inflater.inflate(R.layout.fragment_formulario, container, false)

        // Vincula las vistas
        etNombre = view.findViewById(R.id.et_nombre)
        etApellido = view.findViewById(R.id.et_apellido)
        btnAgregar = view.findViewById(R.id.btn_agregar)

        // Configura el botón para guardar en la base de datos
        btnAgregar.setOnClickListener {
            val nombre = etNombre.text.toString().trim()
            val apellido = etApellido.text.toString().trim()

            if (nombre.isNotEmpty() && apellido.isNotEmpty()) {
                val nuevoUsuario = UserEntity(nombre = nombre, apellido = apellido)

                // Usar el UsuarioDatabaseProvider para obtener la instancia de la base de datos
                val database = UsuarioDatabaseProvider.getDatabase(requireContext())

                lifecycleScope.launch(Dispatchers.IO) {
                    // Inserta el usuario en la base de datos usando Room
                    database.getUserDao().insertar(nuevoUsuario)

                    // Muestra un mensaje en el hilo principal
                    withContext(Dispatchers.Main) {
                        Toast.makeText(requireContext(), "Usuario guardado", Toast.LENGTH_SHORT).show()
                        etNombre.text.clear()
                        etApellido.text.clear()
                    }
                }
            } else {
                Toast.makeText(requireContext(), "Por favor, completa ambos campos", Toast.LENGTH_SHORT).show()
            }
        }

        return view
    }
}