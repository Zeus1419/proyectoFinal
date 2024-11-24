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
import com.andres.navigationcomponentexample.databinding.FragmentRoomBinding
import com.andres.navigationcomponentexample.model.database.dao.UserDao
import com.andres.navigationcomponentexample.model.database.providers.UsuarioDatabaseProvider

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class Room : Fragment() {

    private var _binding: FragmentRoomBinding? = null
    private val binding get() = _binding!!

    private lateinit var usuarioAdapter: UsuarioAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRoomBinding.inflate(inflater, container, false)
        val view = binding.root

        val db = UsuarioDatabaseProvider.getDatabase(requireContext())
        val usuarioDao = db.getUserDao()

        // Obtener el EditText y el Button de la vista
        val etNombreEliminar = view.findViewById<EditText>(R.id.etNombreEliminar)
        val btnEliminarUsuario = view.findViewById<Button>(R.id.btnEliminarUsuario)
        val btnEliminarTodos = view.findViewById<Button>(R.id.btnEliminarTodos)

        // Inicializa el adaptador de usuarios y lo asigna al ListView
        usuarioAdapter = UsuarioAdapter(requireContext(), mutableListOf())
        binding.lvUsers.adapter = usuarioAdapter

        // Cargar los usuarios desde la base de datos
        cargarUsuarios()

        // Configura el botón para eliminar un usuario por nombre
        btnEliminarUsuario.setOnClickListener {
            val nombre = etNombreEliminar.text.toString().trim()

            if (nombre.isNotEmpty()) {
                eliminarUsuarioPorNombre(nombre, usuarioDao)
            } else {
                Toast.makeText(requireContext(), "Por favor, ingresa el nombre del usuario a eliminar", Toast.LENGTH_SHORT).show()
            }
        }

        // Configura el botón para eliminar todos los usuarios
        btnEliminarTodos.setOnClickListener {
            eliminarTodos(usuarioDao)
        }

        return view
    }

    // Método para cargar usuarios en el ListView
    private fun cargarUsuarios() {
        val usuarioDao = UsuarioDatabaseProvider.getDatabase(requireContext()).getUserDao()

        lifecycleScope.launch(Dispatchers.IO) {
            val usuarios = usuarioDao.getAllUsers()
            withContext(Dispatchers.Main) {
                usuarioAdapter.clear()
                usuarioAdapter.addAll(usuarios)
                usuarioAdapter.notifyDataSetChanged()
            }
        }
    }

    // Método para eliminar un usuario por nombre
    private fun eliminarUsuarioPorNombre(nombre: String, usuarioDao: UserDao) {
        lifecycleScope.launch(Dispatchers.IO) {
            val usuario = usuarioDao.getAllUsers().find { it.nombre == nombre }

            if (usuario != null) {
                usuarioDao.eliminar(usuario)  // Eliminar el usuario encontrado

                withContext(Dispatchers.Main) {
                    Toast.makeText(requireContext(), "Usuario $nombre eliminado", Toast.LENGTH_SHORT).show()
                    cargarUsuarios() // Recargar la lista de usuarios
                }
            } else {
                withContext(Dispatchers.Main) {
                    Toast.makeText(requireContext(), "Usuario $nombre no encontrado", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    // Método para eliminar todos los usuarios de la base de datos
    private fun eliminarTodos(usuarioDao: UserDao) {
        lifecycleScope.launch(Dispatchers.IO) {
            usuarioDao.eliminarTodos()

            withContext(Dispatchers.Main) {
                Toast.makeText(requireContext(), "Todos los usuarios han sido eliminados", Toast.LENGTH_SHORT).show()
                cargarUsuarios() // Recargar la lista de usuarios
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}