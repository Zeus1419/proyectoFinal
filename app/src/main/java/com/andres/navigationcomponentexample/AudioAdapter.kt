package com.andres.navigationcomponentexample

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.io.File

// Adapter para mostrar los archivos de audio grabados
class AudioAdapter(
    private val context: Context,
    private val audioFiles: List<File>,
    private val onAudioSelected: (File) -> Unit // Acción al seleccionar un archivo
) : RecyclerView.Adapter<AudioAdapter.AudioViewHolder>() {

    // ViewHolder para cada item del RecyclerView
    inner class AudioViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val audioName: TextView = view.findViewById(android.R.id.text1) // Usamos un simple TextView para mostrar el nombre

        // Vinculamos cada archivo de audio a su vista
        fun bind(file: File) {
            audioName.text = file.name
            itemView.setOnClickListener {
                onAudioSelected(file) // Llamamos al callback cuando se selecciona el archivo
            }
        }
    }

    // Inflamos el layout para cada item
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AudioViewHolder {
        val view = LayoutInflater.from(context).inflate(android.R.layout.simple_list_item_1, parent, false)
        return AudioViewHolder(view)
    }

    // Enlazamos cada archivo de audio al ViewHolder
    override fun onBindViewHolder(holder: AudioViewHolder, position: Int) {
        holder.bind(audioFiles[position])
    }

    // Retornamos el número de archivos de audio
    override fun getItemCount(): Int = audioFiles.size
}
